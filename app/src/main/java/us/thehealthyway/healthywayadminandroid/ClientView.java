package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

public class ClientView extends AppCompatActivity {
    private static final String TAG = "HW.ClientView";

    // UI references
    private EditText clientEmail;
    private TextView message;
    private ImageButton mailboxButton;
    private ImageButton staffSettingButton;
    private TextView journalHTML;
    private LineChart weightChart;
    private ListView listOfClients;
    // Model
    private Model model;

    // keyed data
    private String clientEmailKeyed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: debugging");
        setContentView(R.layout.activity_client_view);
        // find Model Controller
        model = Model.getInstance();
        // find view fields
        clientEmail = (EditText) findViewById(R.id.client_email);
        message = (TextView) findViewById(R.id.client_message);
        mailboxButton = (ImageButton) findViewById(R.id.mailboxButton);
        staffSettingButton = (ImageButton) findViewById(R.id.staff_setting);
        journalHTML = (TextView) findViewById(R.id.journal_html);
        // weightChart = (LineChart) findViewById(R.id.weight_chart);
        listOfClients = (ListView) findViewById(R.id.list_of_clients);
        // wire button listeners
        clientEmail.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
              @Override
              public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                  clientEmailKeyed = textView.toString();
                  displayJournalAndWeight();
                  InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                  imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                  return true;
              }
          }
        );

        staffSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routeToSettings();
            }
        });

        mailboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailToStaff();
            }
        });


    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ClientView.class);
    }


    void sendEmailToStaff() {
        Log.d(TAG, "sendEmailToStaff: send client journal to staff");
        message.setText("Mail will be sent in future, test only");
    }

    void routeToSettings() {
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CLIENT_VIEW.getName(),
                HealthyWayAdminActivities.SETTINGS);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    void displayJournalAndWeight() {
        Log.d(TAG, "displayJournalAndWeight: display client journal and weight chart");
        mailboxButton.setVisibility(View.VISIBLE);
        message.setText("In future, journal and weight chart will be displayed");
    }
}
