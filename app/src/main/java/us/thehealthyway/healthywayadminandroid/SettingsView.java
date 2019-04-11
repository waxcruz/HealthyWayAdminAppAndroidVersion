package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import static us.thehealthyway.healthywayadminandroid.AppData.DEBUG;

public class SettingsView extends AppCompatActivity {
    private static final String TAG = "HW.SettingsView";

    // UI references
    private TextView message;
    private Button changePasswordButton;
    private Button signOutButton;
    private Button cancelButton;

    // Model
    private Model model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            Log.d(TAG, "onCreate: debugging");
        }
        setContentView(R.layout.activity_settings_view);
        // find Model Controller
        model = Model.getInstance();
        // setup button listeners
        changePasswordButton = (Button) findViewById(R.id.change_password_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        // wire button listeners
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEBUG) {
                    Log.d(TAG, "onClick: change button pressed");
                }
                routeToChangePassword();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEBUG) {
                    Log.d(TAG, "onClick: signoff button pressed");
                }
                signoffStaff();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEBUG) {
                    Log.d(TAG, "onClick: cancel button pressed");
                }
                cancelAction();
            }
        });

    }



// Actions
    void routeToChangePassword() {
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_SETTINGS.getName(),
                HealthyWayAdminActivities.CHANGE_PASSWORD_ACTIVITY);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    void signoffStaff(){
        // add Firebase signoff code
        if (DEBUG) {
            Log.d(TAG, "signoffStaff: add signoff code here");
        }
        // route to login view
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_SETTINGS.getName(),
                HealthyWayAdminActivities.LOGIN_ACTIVITY);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    void cancelAction(){
        if (DEBUG) {
            Log.d(TAG, "cancelAction: returning to ClientView");
        }
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_SETTINGS.getName(),
                HealthyWayAdminActivities.CLIENT_VIEW);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    public  static Intent makeIntent(Context context){
        return new Intent(context, SettingsView.class);
    }
}
