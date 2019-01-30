package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateNewAccount extends AppCompatActivity {
    private static final String TAG = "HW.CreateNewAccount";
    // UI References
    private EditText createMailAccount;
    private EditText createAccountPassword;
    private EditText createAccountPasswordConfirmation;
    private Button signinButton;
    private Button cancelButton;
    private TextView copyright;
    private TextView message;
    private Model model;
    private String createEmailKeyed;
    private String createAccountPasswordKeyed;
    private String createAccountPasswordConfirmationKeyed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        // find model controller
        model = Model.getInstance();
        // setup create form
        createMailAccount = (EditText) findViewById(R.id.create_account_email);
        createAccountPassword = (EditText) findViewById(R.id.create_account_password);
        createAccountPasswordConfirmation = (EditText) findViewById(R.id.create_account_password_confirmation);
        signinButton = (Button) findViewById(R.id.sign_in_button);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        // wire listeners
        createMailAccount.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                createEmailKeyed = v.getText().toString();
                hideMyKeyboard(v);
                return true;
            }
        });
        createAccountPassword.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                createAccountPasswordKeyed =v.getText().toString();
                hideMyKeyboard(v);
                return true;
            }
        });
        createAccountPasswordConfirmation.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                createAccountPasswordConfirmationKeyed = v.getText().toString();
                hideMyKeyboard(v);
                return true;
            }
        });
        // wire buttons
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmailKeyed = createMailAccount.getText().toString();
                createAccountPasswordKeyed = createAccountPassword.getText().toString();
                createAccountPasswordConfirmationKeyed = createAccountPasswordConfirmation.getText().toString();
                createLogin();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequested();
            }
        });

        copyright = findViewById(R.id.copyrightText);
        copyright.setText(Model.makeCopyRight());
        message = findViewById(R.id.client_message);
        message.setText(""); // clear message
    }

    void createLogin(){
        Log.d(TAG, "createLogin: Enter");
        // add Firebase create account logic

        Log.d(TAG, "createLogin: Exit");
    }


    void cancelRequested(){
        Log.d(TAG, "cancelRequested: Enter");
        // return to login
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CREATE_NEW_ACCOUNT.getName(),
                HealthyWayAdminActivities.LOGIN_ACTIVITY);
        Log.d(TAG, "cancelRequested: Exit");
        finish();
    }
    void hideMyKeyboard(TextView v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public  static Intent makeIntent(Context context){
        return new Intent(context, CreateNewAccount.class);
    }
}
