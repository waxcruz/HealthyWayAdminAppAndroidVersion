package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
// Application Singleton Model
import com.google.firebase.FirebaseApp;

import java.security.DigestException;
import java.util.Dictionary;
import java.util.Map;

import static us.thehealthyway.healthywayadminandroid.AppData.DEBUG;




public class AdminActivity extends AppCompatActivity implements ReturnFromFirebase {
    private static final String TAG = "HW.AdminActivity";
    private Context context;
    private HealthyWayAdminActivities viewControllers; // activities
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this.context;
        TextView copyright = findViewById(R.id.copyrightText);
        FirebaseApp.initializeApp(context);
        if (DEBUG) Log.d(TAG, "onCreate");
        model = Model.getInstance();
        copyright.setText(Model.makeCopyRight());
        model.startModel(this, this);
        switchActivities(HealthyWayAdminActivities.LOGIN_ACTIVITY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){


        switch (requestCode) {
            case HealthyWayAdminActivities.ADMIN_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Returned from AdminActivity, somethings wrong!" );
                } else {
                    Log.d(TAG, "Returned from AdminActivity with cancel");
                }
                break;
            case HealthyWayAdminActivities.CHANGE_PASSWORD_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_CHANGE_PASSWORD_ACTIVITY.getName()));
                    String check = data.getStringExtra("ChangePassword");
                    Log.d(TAG, String.format("Returned: %s",data.getStringExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CHANGE_PASSWORD_ACTIVITY.getName())));
                } else {
                    Log.d(TAG, "Returned from AdminActivity with cancel");
                }
                break;
            case HealthyWayAdminActivities.CLIENT_VIEW:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_CLIENT_VIEW.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_CLIENT_VIEW.getName()));
                }
                break;
            case HealthyWayAdminActivities.CREATE_NEW_ACCOUNT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_CREATE_NEW_ACCOUNT.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_CREATE_NEW_ACCOUNT.getName()));
                }
                break;
            case HealthyWayAdminActivities.FORGOTTEN_PASSWORD:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_FORGOTTEN_PASSWORD.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_FORGOTTEN_PASSWORD.getName()));
                }
                break;
            case HealthyWayAdminActivities.LOADING_PLEASE_WAIT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_LOADING_PLEASE_WAIT.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_LOADING_PLEASE_WAIT.getName()));
                }
                break;
            case HealthyWayAdminActivities.LOGIN_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_LOGIN_ACTIVITY.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_LOGIN_ACTIVITY.getName()));
                }
                break;
            case HealthyWayAdminActivities.SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, String.format("Returned from %s", HealthyWayAdminActivities.HealthyWayViews.VIEW_SETTINGS.getName()));
                } else {
                    Log.d(TAG, String.format("Returned from %s with Cancel", HealthyWayAdminActivities.HealthyWayViews.VIEW_SETTINGS.getName()));
                }
                break;
            default:
                Log.i(TAG, "Bad requestCode from onActivityResult" + requestCode);
                break;
        }
    }

    protected void switchActivities(int segue){
        Intent intent;
        switch (segue) {
            case HealthyWayAdminActivities.CHANGE_PASSWORD_ACTIVITY:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.CHANGE_PASSWORD_ACTIVITY);
                break;
            case HealthyWayAdminActivities.CLIENT_VIEW:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.CLIENT_VIEW);
                break;
            case HealthyWayAdminActivities.CREATE_NEW_ACCOUNT:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.CREATE_NEW_ACCOUNT);
                break;
            case HealthyWayAdminActivities.FORGOTTEN_PASSWORD:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.FORGOTTEN_PASSWORD);
                break;
            case HealthyWayAdminActivities.LOADING_PLEASE_WAIT:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.LOADING_PLEASE_WAIT);
                break;
            case HealthyWayAdminActivities.LOGIN_ACTIVITY:
                intent = LoginActivity.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.LOGIN_ACTIVITY);
                break;
            case HealthyWayAdminActivities.SETTINGS:
                intent = ChangePassword.makeIntent(AdminActivity.this);
                startActivityForResult(intent, HealthyWayAdminActivities.SETTINGS);
                break;
            default:
                Log.i(TAG, "Bad segue from switchActivities" + segue);
                break;
        }
    }



    // my Firebase handlers

    public void successStartingModel(){
        if (DEBUG) {
            Log.d(TAG, "is Admin signed in?: " + model.getIsAdminSignedIn());
            Log.d(TAG, "UID: " + model.getSignedInUID());
            Log.d(TAG, "Email: " + model.getSignedInEmail());
        }

        Map<String, Object> masterList = model.getNodeEmails(this, this);
    }

    public void failureStartingModel(String errorMessage) {

        if (DEBUG) {
            Log.d(TAG, errorMessage);

        }
    }

    public void successCreateAdmin(){
        if (DEBUG) {
            Log.d(TAG, "success resetting password");
        }
    }

    public void failureCreateAdmin(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure updating password");
        }
        Log.e(TAG, "failure in password reset" + errorMessage);
    }

    public void successGetEmailsNode(){
        if (DEBUG) {
            Log.d(TAG, "success getting emails node");
        }
    }

    public void failureGetEmailsNode(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, errorMessage);
        }
    }

    public void successPasswordReset(){
        if (DEBUG) {
            Log.d(TAG, "success resetting password");
        }
    }

    public void failurePasswordReset(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure resettng password");
        }
        Log.e(TAG, "failure in password reset" + errorMessage);
    }


    public void successPasswordUpdate(){
        if (DEBUG) {
            Log.d(TAG, "success resetting password");
        }
    }

    public void failurePasswordUpdate(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure updating password");
        }
        Log.e(TAG, "failure in password reset" + errorMessage);
    }


    public void successCreateEmail(){
        if (DEBUG) {
            Log.d(TAG, "success creating email");
        }
    }

    public void failureCreateEmail(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure creating email" + errorMessage);
        }
        Log.e(TAG, "failure creating email");
    }

    public void successCreateUser(){
        if (DEBUG) {
            Log.d(TAG, "success creating user");
        }
    }

    public void failureCreateUser(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure creating user" + errorMessage);
        }
        Log.e(TAG, "failure creating user");
    }

    public void successGetUserDataNode(){
        if (DEBUG) {
            Log.d(TAG, "success getting user data node");
        }
    }

    public void failureGetUserDataNode(String errorMessage) {
        if (DEBUG) {
            Log.d(TAG, "failure getting  user data node" + errorMessage);
        }
        Log.e(TAG, "failure getting user data node");
    }


    public void connectionStatus(String status) {
        if (DEBUG) {
            Log.d(TAG, "Connection status is " + status);
        }
    }



}
