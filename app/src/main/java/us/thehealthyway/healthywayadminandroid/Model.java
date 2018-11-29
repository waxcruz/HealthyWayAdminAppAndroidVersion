package us.thehealthyway.healthywayadminandroid;

import android.content.Context;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Helper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
// My Framework
// import us.thehealthyway.framework.*;
import us.thehealthyway.framework.firebase.Helpers;
import us.thehealthyway.framework.firebase.KeysForFirebase;
// My Android constants
import static android.support.constraint.Constraints.TAG;
import static us.thehealthyway.healthywayadminandroid.AppData.DEBUG;



interface ReturnFromFirebase {
    public void successStartingModel();
    public void failureStartingModel(String errorMessage);
    public void successGetEmailsNode();
    public void failureGetEmailsNode(String errorMessage);
    public void successPasswordReset();
    public void failurePasswordReset(String errorMessage);
    public void successPasswordUpdate();
    public void failurePasswordUpdate(String errorMessage);
    public void connectionStatus(String status);
    public void failureGetUserDataNode(String errorMessage);
    public void successGetUserDataNode();
    public void successCreateAdmin();
    public void failureCreateAdmin(String errorMessage);
    public void successCreateUser();
    public void failureCreateUser(String errorMessage);
    public void successCreateEmail();
    public void failureCreateEmail(String errorMessage);



}


public class Model {
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase healthywaysc;
    private DatabaseReference ref;
    private ValueEventListener valueListener;
    private ChildEventListener childListener;
    // Keys and content
    private String firebaseDateKey;
    private Map<String, Object> settingsInFirebase;
        public Map<String, Object> getSettingsInFirebase() {
            return settingsInFirebase;
        }
        public void setSettingsInFirebase(Map<String, Object> settingsInFirebase) {
            this.settingsInFirebase = settingsInFirebase;
        }
    private Map<String, Object> journalInFirebase;
        public Map<String, Object> getJournalInFirebase() {
            return journalInFirebase;
        }
        public void setJournalInFirebase(Map<String, Object> journalInFirebase) {
            this.journalInFirebase = journalInFirebase;
        }
    private Map<String, Object> mealContentsInFirebase;
        public Map<String, Object> getMealContentsInFirebase() {
            return mealContentsInFirebase;
        }
        public void setMealContentsInFirebase(Map<String, Object> mealContentsInFirebase) {
            this.mealContentsInFirebase = mealContentsInFirebase;
        }
    private Map<String, Object> emailsInFirebase;
        public Map<String, Object> getEmailsInFirebase() {
            return emailsInFirebase;
        }
        public void setEmailsInFirebase(Map<String, Object> emailsInFirebase) {
            this.emailsInFirebase = emailsInFirebase;
        }
    private Map<String, Object> clientNode;
        public Map<String, Object> getClientNode() {
            return clientNode;
        }
        public void setClientNode(Map<String, Object> clientNode) {
            this.clientNode = clientNode;
        }
    private String clientErrorMessage;
        public String getClientErrorMessage() {
            return clientErrorMessage;
        }
        public void setClientErrorMessage(String clientErrorMessage) {
            this.clientErrorMessage = clientErrorMessage;
        }
    private String emailsList[];
        public String[] getEmailsList() {
            return emailsList;
        }
        public void setEmailsList(String[] emailsList) {
            this.emailsList = emailsList;
        }
    // Firebase attributes
    private boolean isAdminSignedIn;
    private String signedInUID;
    private String signedInEmail;
    private String signedInError;

    /*
        Singleton model
     */
    private static Model instance;


    private Model() { }
    /*
        Private to prevent anyone else from instantiating the model
     */

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }


    public void startModel(final ReturnFromFirebase failureHandler, final ReturnFromFirebase successHandler) {
        healthywaysc = FirebaseDatabase.getInstance();
        ref = healthywaysc.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        signedInUID = null;
        signedInEmail = null;
        signedInError = null;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseAuth.signOut();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            mFirebaseAuth.signInWithEmailAndPassword("wmyronw@yahoo.com", "waxwax")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "onComplete");
                            if (task.isSuccessful()) {
                                Log.d(TAG,"Successful login");
                                isAdminSignedIn = true;
                                signedInUID = mFirebaseAuth.getCurrentUser().getUid();
                                signedInEmail = mFirebaseAuth.getCurrentUser().getEmail();
                                healthywaysc = FirebaseDatabase.getInstance();
                                ref = healthywaysc.getReference();
                                successHandler.successStartingModel();
                            } else {
                                Log.d(TAG, "Failed to sign in. Error is " + task.getException());
                                signedInError = task.getException().toString();
                                isAdminSignedIn = false;
                                failureHandler.failureStartingModel("Failed to sign in. Error: " + task.getException().getLocalizedMessage());
                            }
                        }
                    });

            return;
        } else {
            signedInUID = mFirebaseUser.getUid();
            signedInEmail = mFirebaseUser.getEmail();
            isAdminSignedIn = true;
        }


    }

    public void stopModel() {

        ref.removeEventListener(childListener);
    }


    // Helper methods for Firebase

    public void updateChildOfRecordInFirebase(String table, String recordID, String path, Object value) {
        String fullFirebasePath = table + "/" + recordID + path;
        if (fullFirebasePath != null) {
            ref.child(fullFirebasePath).setValue(value);
        } else {
            Log.d(TAG, "error in updateChildOfRecordInFirebase");
        }
    }

    // Authentication
    public void loginUser( String email, String password, final ReturnFromFirebase errorHandler, final ReturnFromFirebase handler) {
        // Initialize Firebase Auth
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete");
                        if (task.isSuccessful()) {
                            Log.d(TAG,"Successful login");
                            isAdminSignedIn = true;
                            signedInUID = mFirebaseAuth.getCurrentUser().getUid();
                            signedInEmail = mFirebaseAuth.getCurrentUser().getEmail();
                            signedInError = null;
                            handler.successStartingModel();
                        } else {
                            Log.d(TAG, "Failed to sign in. Error is " + task.getException());
                            signedInError = task.getException().toString();
                            isAdminSignedIn = false;
                            errorHandler.failureStartingModel("Failed to sign in. Error is " + task.getException().getMessage());
                        }
                    }
                });


    }

    public  void signoutHandler(final ReturnFromFirebase errorHandler) {
        if (mFirebaseAuth.getCurrentUser().getDisplayName() != null) {
            mFirebaseAuth.signOut();
            signedInUID = null;
            signedInEmail = null;
            signedInError = null;
        }

    }



    public void signoutHandler(final  ReturnFromFirebase errorHandler, final ReturnFromFirebase handler) {
        if (mFirebaseAuth.getCurrentUser().getUid() != null) {
            mFirebaseAuth.signOut();
            signedInUID = null;
            signedInEmail = null;
            signedInError = null;
        }
    }



    public void passwordReset(String email, final ReturnFromFirebase errorHandler, final ReturnFromFirebase handler) {
        mFirebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete in passwordReset");
                        if (task.isSuccessful()) {
                            handler.successPasswordReset();
                        } else {
                            errorHandler.failurePasswordReset("failure in password reset: " + task.getException().getLocalizedMessage());
                        }
                    }
                 });
    }

    public void updatePassword(String newPassword, final ReturnFromFirebase errorHandler, final ReturnFromFirebase handler) {
        mFirebaseAuth.getCurrentUser().updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (DEBUG) {
                            Log.d(TAG, "updatePassword completed");
                        }
                        if (task.isSuccessful()) {
                            if (DEBUG) {
                                Log.d(TAG, "updatePassword successful");
                            }
                            handler.successPasswordUpdate();
                        } else {
                            if (DEBUG) {
                                Log.d(TAG, "updatePassowrd failed");
                            }
                            handler.failurePasswordUpdate(task.getException().getMessage());
                        }
                    }
                });
    }


    public void checkFirebaseConnected(final ReturnFromFirebase handler) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                } else {
                    System.out.println("not connected");
                }
                handler.connectionStatus(connected ? "connected" : "not connected");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    void getNodeOfClient(final String email, final ReturnFromFirebase errorHandler, final ReturnFromFirebase handler) {
        // emails-->users-->userData

        clientNode.clear();
        clientErrorMessage = "";
        // find the email in node emails
        String firebaseMail = Helpers.makeFirebaseEmailKey(email);
        DatabaseReference emailsRef = ref.child(KeysForFirebase.NODE_EMAILS).child(firebaseMail);
        ValueEventListener clientEvent = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Map<String, Object> nodeEmailsValue = (Map<String, Object>) dataSnapshot.getValue();
                final String  clientUID =  (String) nodeEmailsValue.get("uid");
                if (clientUID == null) {
                    clientErrorMessage = "No Client found with that email address";
                    errorHandler.failureGetEmailsNode(clientErrorMessage);
                    return;
                }
                // use emails secondary key, UID, to verify client email is cross linked to users UID
                DatabaseReference userRef = ref.child(KeysForFirebase.NODE_USERS).child(clientUID);
                ValueEventListener userEvent = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> nodeUsersValue = (Map<String, Object>) dataSnapshot.getValue();
                        if (nodeEmailsValue == null) {
                            clientErrorMessage = "Encountered error searching for client UID";
                            errorHandler.failureGetEmailsNode(clientErrorMessage);
                            return;
                        }
                        String checkEmail = (String) nodeUsersValue.get("email");
                        if (checkEmail == email) {
                            // retrieve the client data
                            DatabaseReference userDataRef = ref.child(KeysForFirebase.NODE_USERDATA).child(clientUID);
                            ValueEventListener userDataEvent = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    clientNode = (Map<String, Object>) dataSnapshot.getValue();
                                    clientErrorMessage = "";
                                    handler.successGetUserDataNode();
                                    return;
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    clientNode = null;
                                    clientErrorMessage = "Encountered error searching for client data";
                                    errorHandler.failureGetEmailsNode(clientErrorMessage);
                                    return;
                                }
                            };
                            userDataRef.addListenerForSingleValueEvent(userDataEvent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                userRef.addListenerForSingleValueEvent(userEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        emailsRef.addListenerForSingleValueEvent(clientEvent);
    }


    void createAuthUserNode(String email, String password, final ReturnFromFirebase  errorHandler, final ReturnFromFirebase handler) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signedInUID = task.getResult().getUser().getUid();
                            signedInEmail = task.getResult().getUser().getEmail();
                            if (signedInUID == null) {
                                errorHandler.failureCreateAdmin("Account creation failure");
                                return;
                            } else {
                                handler.successCreateAdmin();
                                return;
                            }
                        } else {
                            errorHandler.failureCreateAdmin(task.getException().getLocalizedMessage());
                            return;
                        }
                    }
                });
    }

    void createUserInUsersNode(String uid, String email, final ReturnFromFirebase  errorHandler, final ReturnFromFirebase handler) {
        // create admin user in users node
        Map<String, Object> newUser = new HashMap<String, Object>();
        newUser.put("email", email);
        newUser.put("isAdmin", true);
        DatabaseReference userRef = ref.child(KeysForFirebase.NODE_USERS).child(uid);
        userRef.setValue(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        handler.successCreateUser();
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        errorHandler.failureCreateUser("Account creation failed for Users Node");
                    }
                });
    }

    void createEmailInEmailsNode(String uid, String email, final ReturnFromFirebase  errorHandler, final ReturnFromFirebase handler) {
        // create admin user in users node
        Map<String, Object> newEmail = new HashMap<String, Object>();
        newEmail.put("uid", uid);
        newEmail.put("isAdmin", true);
        String keyEmail = Helpers.makeFirebaseEmailKey(email);
        DatabaseReference emailsRef = ref.child(KeysForFirebase.NODE_EMAILS).child(keyEmail);
        emailsRef.setValue(newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        handler.successCreateEmail();
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        errorHandler.failureCreateEmail("Account creation failed for Email Node");
                    }
                });
    }




    public String getSignedInUID() {
        return signedInUID;
    }

    public String getSignedInEmail() {
        return signedInEmail;
    }

    public String getIsAdminSignedIn() {
        if (isAdminSignedIn)
            return "true";
        else
            return "false";
    }

    public String getSignedInError() {
        return signedInError;
    }
  // list methods
    public Map<String, Object> getNodeEmails(final ReturnFromFirebase failureHandler, final ReturnFromFirebase successHandler) {
        DatabaseReference emailsNode = ref.child("emails");
        ValueEventListener emailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                emailsInFirebase = (Map<String, Object>) dataSnapshot.getValue();
                successHandler.successGetEmailsNode();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                failureHandler.failureGetEmailsNode("Database error reading emails node with message: " + databaseError.toString());
            }

        };
        emailsNode.addListenerForSingleValueEvent(emailsListener);

        return null;
    }



    // create copyright string
    public static String makeCopyRight() {
        Calendar calendar = Calendar.getInstance();
        return String.format("Copyright @ %s The Healthy Way", calendar.get(Calendar.YEAR));
    }
}
