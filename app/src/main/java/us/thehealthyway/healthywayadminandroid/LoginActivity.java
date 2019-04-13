package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static us.thehealthyway.healthywayadminandroid.AppData.DEBUG;

/**
 * A login screen that offers login via staffEmail/staffPassword.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "HW.LoginActivity";


    // UI references.
    private EditText staffEmail;
    private EditText staffPassword;
    private Button loginButton;
    private Button cancelButton;
    private Button createAccountButton;
    private Button forgottenPasswordButton;
    private TextView copyright;
    private TextView message;
    private Model model;
    private String staffEmailKeyed;
    private String staffPasswordKeyed;
    private InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // find Model Controller
        model = Model.getInstance();
        // Set up the login form.
        staffEmail = (EditText) findViewById(R.id.staff_old_password);
        staffEmail.setText("wmyronw@yahoo.com");
        staffEmail.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                staffEmailKeyed = textView.getText().toString();
                InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                return true;
            }

        });
//        staffEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    staffEmailKeyed = staffEmail.getText().toString();
//                }
//            }
//        });


        staffPassword = (EditText) findViewById(R.id.staff_new_password);
        staffPassword.setText("");
        staffPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                staffPasswordKeyed = textView.getText().toString();
                InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                return true;
            }
        });
//        staffPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    staffPasswordKeyed = staffPassword.getText().toString();
//                }
//            }
//        });
        loginButton = (Button) findViewById(R.id.change_password_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                staffEmailKeyed = staffEmail.getText().toString();
                staffPasswordKeyed = staffPassword.getText().toString();
                if (staffEmailKeyed.equals("")) {
                    message.setText("No email address entered.");
                    return;
                }
                if (staffPasswordKeyed.equals("")) {
                    message.setText("No password entered");
                    return;
                }

                attemptLogin();
            }
        });

        createAccountButton = (Button) findViewById(R.id.sign_out_button);
        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                routeToCreateNewAccount();
            }
        });


        forgottenPasswordButton = (Button) findViewById(R.id.cancel_button);
        forgottenPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                routeToForgottenPassword();
            }
        });


        copyright = findViewById(R.id.copyrightText);
        copyright.setText(Model.makeCopyRight());
        message = findViewById(R.id.client_message);
        message.setText(""); // clear message

    }






    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BundleKeys.BundleKeyValues.BundleKeyEmail.getName(), staffEmailKeyed);
        outState.putString(BundleKeys.BundleKeyValues.BundleKeyPassword.getName(), staffPasswordKeyed);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        staffEmailKeyed = savedInstanceState.getString(BundleKeys.BundleKeyValues.BundleKeyEmail.getName());
        staffPasswordKeyed = savedInstanceState.getString(BundleKeys.BundleKeyValues.BundleKeyPassword.getName());
    }

    public  static Intent makeIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }


    private void attemptLogin(){
        model.loginUser(staffEmailKeyed, staffPasswordKeyed, (message)-> {loginFailure(message); }, ()-> {loginSuccess(); });
    }


    private void loginFailure(String message){
        if (DEBUG) {
            Log.d(TAG, "loginFailure: "+ message);
        }
        this.message.setText(message.toString());
    }


    private void loginSuccess(){
        if (DEBUG) {
            Log.d(TAG, "loginSuccess: " + staffEmailKeyed);
        }
        // route to ClientView because staff logged into app
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_LOGIN_ACTIVITY.getName(),
                HealthyWayAdminActivities.CLIENT_VIEW);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private void routeToCreateNewAccount(){
        if (DEBUG) {
            Log.d(TAG, "routeToCreateNewAccount: button clicked");
        }
        // route to CreateNewAccount because staff wants to create new staff account
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_LOGIN_ACTIVITY.getName(),
                HealthyWayAdminActivities.CREATE_NEW_ACCOUNT);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void routeToForgottenPassword(){
        if (DEBUG) {
            Log.d(TAG, "routeToForgottenPassword: button clicked");
        }
        // route to CreateNewAccount because staff wants to create new staff account
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_LOGIN_ACTIVITY.getName(),
                HealthyWayAdminActivities.FORGOTTEN_PASSWORD);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

