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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = "HW.ChangePassword";
    // UI references
    private EditText staff_old_password;
    private EditText staff_new_password;
    private EditText staff_confirm_password;
    private TextView client_message;
    private TextView copyright;
    private Button change_password_button;
    private Button cancel_button;
    // Model
    private Model model;

    // keyed data
    private String staffOldPasswordlKeyed;
    private String staffNewPasswordKeyed;
    private String staffConfirmPasswordKeyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ChangePassword Activity debugging");
        setContentView(R.layout.activity_change_password);
        // find Model Controller
        model = Model.getInstance();
        client_message = (TextView) findViewById(R.id.client_message);
        staff_old_password = (EditText) findViewById(R.id.staff_old_password);
        staff_new_password = (EditText) findViewById(R.id.staff_new_password);
        staff_confirm_password = (EditText) findViewById(R.id.staff_confirm_password);
        change_password_button = (Button) findViewById(R.id.change_password_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);
        // wire button listeners
        change_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffOldPasswordlKeyed = staff_old_password.getText().toString();
                staffNewPasswordKeyed = staff_new_password.getText().toString();
                staffConfirmPasswordKeyed = staff_confirm_password.getText().toString();
                changePassword();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequested();
            }
        });

        copyright = findViewById(R.id.copyrightText);
        copyright.setText(Model.makeCopyRight());
        client_message = findViewById(R.id.client_message);
        client_message.setText(""); // clear message
    }


    public  static Intent makeIntent(Context context){
        return new Intent(context, ChangePassword.class);
    }

    void changePassword(){
        Log.d(TAG, "changePassword: Enter");
        // add change password logic
        // route to client view
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CHANGE_PASSWORD_ACTIVITY.getName(),
                HealthyWayAdminActivities.CLIENT_VIEW);
        setResult(Activity.RESULT_OK, intent);
        Log.d(TAG, "changePassword: Exit");
        finish();
    }
    
    void cancelRequested() {
        Log.d(TAG, "cancelRequested: Enter");
        // return to caller
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CHANGE_PASSWORD_ACTIVITY.getName(),
                HealthyWayAdminActivities.SETTINGS);
        setResult(Activity.RESULT_OK, intent);
        Log.d(TAG, "cancelRequested: Exit");
        finish();
    }



}

