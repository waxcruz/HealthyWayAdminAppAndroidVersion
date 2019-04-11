package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.security.keystore.KeyNotYetValidException;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;


import static us.thehealthyway.healthywayadminandroid.AppData.DEBUG;

public class ClientView extends AppCompatActivity {
    private static final String TAG = "HW.ClientView";

    // UI references
    private EditText clientEmail;
    private TextView message;
    private ImageButton mailboxButton;
    private ImageButton staffSettingButton;
    private ImageButton clearButton;
    private TextView journalHTML;
    private WebView journalWebView;


    private LineChart weightChart;
    private ListView listOfClients;
    // Model
    private Model model;

    // Model data
    private String firebaseEmails[];
    private ArrayList<String> sortedEmailsList;
    private ArrayList<String> filteredEmailsList;


    // keyed data
    private String clientEmailKeyed;

    // listview
    ArrayAdapter clientAdapter;

    // Global variables for use between methods.
    private double maxTotals[];     // limits
    private double totals[];        // consumed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: debugging");
        maxTotals = new double[5];
        totals = new double[5];

        setContentView(R.layout.activity_client_view);
        // find Model Controller
        model = Model.getInstance();
        // find view fields
        clientEmail = (EditText) findViewById(R.id.client_email);
        message = (TextView) findViewById(R.id.client_message);
        mailboxButton = (ImageButton) findViewById(R.id.mailboxButton);
        staffSettingButton = (ImageButton) findViewById(R.id.staff_setting);
        clearButton = (ImageButton) findViewById(R.id.clearButton);
        journalHTML = (TextView) findViewById(R.id.journal_html);
        journalWebView = (WebView) findViewById(R.id.journal_webview);
        weightChart = (LineChart) findViewById(R.id.weight_chart);
        listOfClients = (ListView) findViewById(R.id.list_of_clients);
        // wire button listeners
        clientEmail.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener() {
              @Override
              public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                  clientEmailKeyed = textView.getText().toString();
                  displayClient();
                  listOfClients.setVisibility(View.INVISIBLE);
                  InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                  imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                  return true;
              }
          }
        );

        clientEmail.addTextChangedListener(new EditTextListener());

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

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientEmail.setText("");
                clientEmailKeyed = "";
                listOfClients.setVisibility(View.INVISIBLE);
                weightChart.setVisibility(View.INVISIBLE);
            }
        });

        // load emails for searching
        Map<String, Object> masterList = model.getNodeEmails(
                (message)-> {failureGetEmailsNode(message); },
                ()-> {successGetEmailsNode(); });
        // wire listview
        filteredEmailsList = new ArrayList<String>();
        clientAdapter = new ArrayAdapter<String>(this, R.layout.activity_client_search_list, filteredEmailsList);
        listOfClients.setAdapter(clientAdapter);
        listOfClients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clientEmailKeyed = listOfClients.getItemAtPosition(position).toString();
                clientEmail.setText(clientEmailKeyed);
                listOfClients.setVisibility(View.INVISIBLE);
                displayClient();
            }
        });


}


    private void failureGetEmailsNode(String message){
        if (DEBUG) {
            Log.d(TAG, "loginFailure: "+ message);
        }
        this.message.setText("Failed to load list of client emails. " + message.toString());
    }


    private void successGetEmailsNode(){
        if (DEBUG) {
            Log.d(TAG, "loadEmailsSuccess: loaded");
        }
        firebaseEmails = model.getEmailsList();
        ArrayList<String> emailsList = new ArrayList<String>();
        // a firebase email contains commas instead of periods. Restore format for display
        for (String email : firebaseEmails) {
            email = (email.replace(',','.')).toLowerCase();
            emailsList.add(email);
        }
        // sort the full list
        Collections.sort(emailsList);
        // save
        sortedEmailsList = new ArrayList<String>();
        sortedEmailsList.addAll(emailsList);
    }


private class EditTextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            journalWebView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            displayClients(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ClientView.class);
    }


    void sendEmailToStaff() {
        if (DEBUG) {
            Log.d(TAG, "sendEmailToStaff: send client journal to staff");
        }
        // build journal
        String htmlBodyMail = formatJournal(model.getClientNode(), true);
        // save html to local file
        String journalFileName = "journalAttachment2.html";
        String pathToJournalAttachment = saveHtmlFile(journalFileName, htmlBodyMail);
        // try Journal attachment
//
//        File journalFile = getPublicDocumentsDirNewFile(journalFileName);
//        pathToJournalAttachment = journalFile.getPath();
//        try {
//            FileInputStream checkAttachment = new FileInputStream(journalFile);
//            int c;
//            String attachment = "";
//            while ((c = checkAttachment.read()) != -1) {
//                attachment += Character.toString((char) c);
//            }
//            Log.i(TAG, "Attachment size:" + attachment.length());
//            checkAttachment.close();
//        } catch (Exception e) {
//            Log.i(TAG, "sendEmailToStaff: ", e);
// e       }

        // address email
        String[] TO = {model.getSignedInEmail()};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Journal");
        // prep journal for attaching to email

        emailIntent.putExtra(emailIntent.EXTRA_STREAM,
                Uri.fromFile(new File(pathToJournalAttachment)));

        emailIntent.putExtra(emailIntent.EXTRA_TEXT, "Open attachment to see the Healthy Way journal for you.");
        emailIntent.addFlags(emailIntent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i(TAG, "sendEmailToStaff: Done.");
            message.setText("Journal sent attached to email");
        } catch (android.content.ActivityNotFoundException ex) {
            message.setText("There is no email client installed.");
        }
    }

    private String saveHtmlFile(String journalFileName, String journal) {
        String returnThePath = "";
        if (isExternalStorageWritable()) {
            Log.i(TAG, "saveHtmlFile: Storage ready");
        } else
            if (isExternalStorageReadable()) {
                Log.i(TAG, "saveHtmlFile: Read only storage");
            } else {
                message.setText("No external storage available");
            }

        String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS).toString();
        try {
            FileOutputStream outputStream = new FileOutputStream(root+"/"+journalFileName);
            outputStream.write(journal.getBytes());
            outputStream.close();
            returnThePath = root + "/" + journalFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnThePath;
    }



    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    void routeToSettings() {
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CLIENT_VIEW.getName(),
                HealthyWayAdminActivities.SETTINGS);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    void displayClients(String token) {
        if (DEBUG) {
            Log.d(TAG, "displayClients: Enter");
        }
        filteredEmailsList.clear();
        for (String email : sortedEmailsList) {
            if (email.startsWith(token)) {
                filteredEmailsList.add(email);
            }
        }
        if (filteredEmailsList.size() == 0) {
            Log.i(TAG, "displayClients: zero entries");
            return;
        }
        listOfClients.setVisibility(View.VISIBLE);
        clientAdapter.notifyDataSetChanged();
        Log.i(TAG, "displayClients: filteredEmailsList size: " + filteredEmailsList.size());
        InputMethodManager imm = (InputMethodManager)clientEmail.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(clientEmail.getWindowToken(), 0);

        if (DEBUG) {
            Log.d(TAG, "displayClients: Exit");
        }
    }

    void displayClient() {
        if (DEBUG) {
            Log.d(TAG, "displayClient: Enter");
        }
        // make journal and chart visible
        journalWebView.setVisibility(View.VISIBLE);
        weightChart.setVisibility(View.VISIBLE);
        message.setText(" ");
        // get the client node
        model.getNodeOfClient(clientEmailKeyed,
                (errorMessage)->{failureGetClientNode(errorMessage);},
                ()->{successGetClientNode();});



        if (DEBUG) {
            Log.d(TAG, "displayClient: Exit");
        }
    }

    private void failureGetClientNode(String message){
        if (DEBUG) {
            Log.d(TAG, "loginFailure: "+ message);
        }
        mailboxButton.setVisibility(View.INVISIBLE);
        weightChart.setVisibility(View.INVISIBLE);
        this.message.setText("Failed getNodeOfClient. " + message.toString());
    }


    private void successGetClientNode(){
        if (DEBUG) {
            Log.d(TAG, "loadEmailsSuccess: loaded");
        }
        journalHTML.setText("");
        weightChart.clear();
        if (model.getClientNode() == null) {
            mailboxButton.setVisibility(View.INVISIBLE);
            message.setText("No data for client");
        } else {
            String journalEntries = formatJournal(model.getClientNode(), false);
            journalWebView.loadDataWithBaseURL(null, journalEntries, "text/html", "utf-8", null);
        }
        // display weight chart
        lineChartUdate(model.getClientNode());
    }


    private String formatJournal(Map<String, Object> node, boolean isEmail) {
        // client Firebase node with 3 child nodes: Settings, Journal, and MealContents
        if (node.size() == 0) {
            return " ";
        }
        Map<String, Object> nodeSettings = (Map<String, Object>)node.get(KeysForFirebase.NODE_SETTINGS);
        Map<String, Object>  nodeJournal = (Map<String, Object>) node.get(KeysForFirebase.NODE_JOURNAL);
        Map<String, Object> nodeMealContents = (Map<String, Object>) node.get(KeysForFirebase.NODE_MEAL_CONTENTS);
        String journalMockup = buildJournalHeader(isEmail);
        if (nodeJournal == null) {
            return " ";
        }
        if (nodeSettings == null) {
            return " ";
        }
        // sort the journal dates for display in descending order
        Set<String> datesOfConsumpation = nodeJournal.keySet();
        ArrayList<String> sortedKeyDates = new ArrayList<String>(datesOfConsumpation);
        Collections.sort(sortedKeyDates);
        Collections.reverse(sortedKeyDates);
        // display journaly details for date
        if (nodeMealContents != null)
        {
            for (String mealdate : sortedKeyDates) {
                totals = new double[5]; // clear totals for new date
                journalMockup += buildJournalDailyTotalsRow(mealdate, nodeSettings);
                Map<String, Object> meal = (Map<String, Object>)nodeMealContents.get(mealdate);
                if (meal != null) {
                    // extract details of consumption
                    Map<String, Object> breakfast = (Map<String, Object>)meal.get(KeysForFirebase.BREAKFAST_MEAL_KEY);
                    Map<String, Object> morningSnack = (Map<String, Object>)meal.get(KeysForFirebase.MORNING_SNACK_MEAL_KEY);
                    Map<String, Object> lunch = (Map<String, Object>)meal.get(KeysForFirebase.LUNCH_MEAL_KEY);
                    Map<String, Object> afternoonSnack = (Map<String, Object>)meal.get(KeysForFirebase.AFTERNOON_SNACK_MEAL_KEY);
                    Map<String, Object> dinner = (Map<String, Object>)meal.get(KeysForFirebase.DINNER_MEAL_KEY);
                    Map<String, Object> eveningSnack = (Map<String, Object>)meal.get(KeysForFirebase.EVENING_SNACK_MEAL_KEY);
                    if (breakfast != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.BREAKFAST_MEAL_KEY, breakfast);
                    }
                    if (morningSnack != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.MORNING_SNACK_MEAL_KEY, morningSnack);
                    }
                    if (lunch != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.LUNCH_MEAL_KEY, lunch);
                    }
                    if (afternoonSnack != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.AFTERNOON_SNACK_MEAL_KEY, afternoonSnack);
                    }
                    if (dinner != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.DINNER_MEAL_KEY, dinner);
                    }
                    if (eveningSnack != null) {
                        journalMockup += buildJournalMealRow(KeysForFirebase.EVENING_SNACK_MEAL_KEY, eveningSnack);
                    }
                }
                journalMockup += buildJournalDateTotals();
                journalMockup += buildJournalDateStats(mealdate, nodeJournal);
                journalMockup += buildJournalDateComments(mealdate, nodeJournal);
            }
        }
        journalMockup += buildJournalDateTrailer();
        return journalMockup;
    }

    private String buildJournalHeader(boolean isEmail) {
        // construct the heading for the client's journal
        String message = "";
        String template = ConstantsHTML.JOURNAL_DAY_HEADER;
        if (isEmail) {
            message = ConstantsHTML.LANDSCAPE;
        }
        template = template.replaceAll("HW_EMAIL_INSTRUCTION", message);
        return template;
    }

    protected String buildJournalDailyTotalsRow(String displayDate,
                                                Map<String, Object> node) {
        // Java doesn't have a mechanism similar to Swift's inout so I use a global for maxTotals
        maxTotals = new double[5]; // start fresh with no old information
        String template = ConstantsHTML.JOURNAL_DAILY_TOTALS_ROW;
        double amount = 0.0;
        if (node == null) { // no daily limits
            return " ";
        }

        template = template.replaceAll("HW_RECORDED_DATE",displayDate);
        if (node.containsKey(KeysForFirebase.LIMIT_PROTEIN_LOW)) {
            amount = ((Number) node.get(KeysForFirebase.LIMIT_PROTEIN_LOW)).doubleValue();
        } else {
            amount = 0.00;
        }
        maxTotals[0] = amount;
        template = template.replaceAll("HW_DAILY_TOTAL_PROTEIN_VALUE", Double.toString(amount));
        if (node.containsKey(KeysForFirebase.LIMIT_STARCH)) {
            amount = ((Number) node.get(KeysForFirebase.LIMIT_STARCH)).doubleValue();
        } else {
            amount = 0.00;
        }
        maxTotals[1] = amount;
        template = template.replaceAll("HW_DAILY_TOTAL_STARCH_VALUE", Double.toString(amount));
        amount = 3.0; // Chel wanted a hard coded limit for veggies
        maxTotals[2] = amount;
        template = template.replaceAll("HW_DAILY_TOTAL_VEGGIES_VALUE", "3.0");
        if (node.containsKey(KeysForFirebase.LIMIT_FRUIT)) {
            amount = ((Number) node.get(KeysForFirebase.LIMIT_FRUIT)).doubleValue();
        } else {
            amount = 0.00;
        }
        maxTotals[3] = amount;
        template = template.replaceAll("HW_DAILY_TOTAL_FRUIT_VALUE", Double.toString(amount));
        if (node.containsKey(KeysForFirebase.LIMIT_FAT)) {
            amount = ((Number) node.get(KeysForFirebase.LIMIT_FAT)).doubleValue();
        } else {
            amount = 0.00;
        }
        maxTotals[4] = amount;
        template = template.replaceAll("HW_DAILY_TOTAL_FAT_VALUE", Double.toString(amount));
        return template;
    }

    protected  String buildJournalMealRow(String name,
                                          Map<String, Object> meal) {
        // Java doesn't have a mechanism similar to Swift's inout so I use a global for totals
        totals = new double[5];
        String template = ConstantsHTML.JOURNAL_MEAL_ROW;
        double amount = 0.0;

        template = template.replaceAll("HW_MEAL_NAME", name);
        if (meal.containsKey(KeysForFirebase.MEAL_DESCRIPTION)) {
            template = template.replaceAll("HW_MEAL_CONTENTS_DESCRIPTION",
                    (String) meal.get(KeysForFirebase.MEAL_DESCRIPTION));
        } else {
            template = template.replaceAll("HW_MEAL_CONTENTS_DESCRIPTION", " ");
        }
        if (meal.containsKey(KeysForFirebase.MEAL_PROTEIN_QUANTITY)) {
            amount = ((Number) meal.get(KeysForFirebase.MEAL_PROTEIN_QUANTITY)).doubleValue();
        } else {
            amount = 0.00;
        }
        totals[0] = amount;
        template = template.replaceAll("HW_MEAL_PROTEIN_COUNT", Double.toString(amount));
        if (meal.containsKey(KeysForFirebase.MEAL_STARCH_QUANTITY)) {
            amount = ((Number) meal.get(KeysForFirebase.MEAL_STARCH_QUANTITY)).doubleValue();
        } else {
            amount = 0.00;
        }
        totals[1] = amount;
        template = template.replaceAll("HW_MEAL_STARCH_COUNT", Double.toString(amount));
        if (meal.containsKey(KeysForFirebase.MEAL_VEGGIES_QUANTITY)) {
            amount = ((Number) meal.get(KeysForFirebase.MEAL_VEGGIES_QUANTITY)).doubleValue();
        } else {
            amount = 0.00;
        }
        totals[2] = amount;
        template = template.replaceAll("HW_MEAL_VEGGIES_COUNT", Double.toString(amount));
        if (meal.containsKey(KeysForFirebase.MEAL_FRUIT_QUANTITY)) {
            amount = ((Number) meal.get(KeysForFirebase.MEAL_FRUIT_QUANTITY)).doubleValue();
        } else {
            amount = 0.00;
        }
        totals[3] = amount;
        template = template.replaceAll("HW_MEAL_FRUIT_COUNT", Double.toString(amount));
        if (meal.containsKey(KeysForFirebase.MEAL_FAT_QUANTITY)) {
            amount = ((Number) meal.get(KeysForFirebase.MEAL_FAT_QUANTITY)).doubleValue();
        } else {
            amount = 0.00;
        }
        totals[4] = amount;
        template = template.replaceAll("HW_MEAL_FAT_COUNT", Double.toString(amount));
        if (meal.containsKey(KeysForFirebase.MEAL_COMMENTS)) {
            template = template.replaceAll("HW_MEAL_COMMENTS",
                    (String) meal.get(KeysForFirebase.MEAL_COMMENTS));
        } else {
            template = template.replaceAll("HW_MEAL_COMMENTS",
                    " ");
        }
        return template;
    }

    protected String buildJournalDateTotals() {
        String  template = ConstantsHTML.JOURNAL_DATE_TOTALS;
        // post consumption on date
        template = template.replaceAll("HW_DATE_TOTAL_PROTEIN", Double.toString(totals[0]));
        template = template.replaceAll("HW_DATE_TOTAL_STARCH", Double.toString(totals[1]));
        if (maxTotals[1] < totals[1]) {
            template = template.replaceAll("HW_TOTAL_STARCH_COLOR", "red");
        } else {
            template = template.replaceAll("HW_TOTAL_STARCH_COLOR", "black");
        }
        template = template.replaceAll("HW_DATE_TOTAL_VEGGIES", Double.toString(totals[2]));
        template = template.replaceAll("HW_DATE_TOTAL_FRUIT", Double.toString(totals[3]));
        if (maxTotals[3] < totals[3]) {
            template = template.replaceAll("HW_TOTAL_FRUIT_COLOR", "red");
        } else {
            template = template.replaceAll("HW_TOTAL_FRUIT_COLOR", "black");
        }
        template = template.replaceAll("HW_DATE_TOTAL_FAT", Double.toString(totals[4]));
        if (maxTotals[4] < totals[4]) {
            template = template.replaceAll("HW_TOTAL_FAT_COLOR", "red");
        } else {
            template = template.replaceAll("HW_TOTAL_FAT_COLOR", "black");
        }
        return template;
    }


    protected String buildJournalDateStats(String date, Map<String, Object> node) {

        int waterCheckCount = 0;
        int supplementCheckCount = 0;
        int exerciseCheckCount = 0;
        String template = ConstantsHTML.JOURNAL_DATE_STATS;
        Map<String, Object> journalDetails =  (Map<String, Object>) node.get(date);
        if (journalDetails != null) {
            if (journalDetails.get(KeysForFirebase.GLASSES_OF_WATER) != null) {
                waterCheckCount = ((Number)(journalDetails.get(KeysForFirebase.GLASSES_OF_WATER))).intValue();
            }
            if (journalDetails.get(KeysForFirebase.SUPPLEMENTS) != null) {
                supplementCheckCount = ((Number)(journalDetails.get(KeysForFirebase.SUPPLEMENTS))).intValue();
            }
            if (journalDetails.get(KeysForFirebase.EXERCISED) != null){
                exerciseCheckCount = ((Number)(journalDetails.get(KeysForFirebase.EXERCISED))).intValue();
            }
        }
        String checkMarks = TextUtils.join("",Collections.nCopies(waterCheckCount,"&#x2714; "));
        template = template.replaceAll("HW_DATE_WATER_CHECKS", checkMarks);
        checkMarks = TextUtils.join("",Collections.nCopies(supplementCheckCount,"&#x2714; "));
        template = template.replaceAll("HW_DATE_SUPPLEMENTS_CHECKS ", checkMarks);
        if (exerciseCheckCount > 0) {
            template = template.replaceAll("HW_DATE_EXERCISE_CHECKS ", "&#x2714; ");
        } else {
            template = template.replaceAll("HW_DATE_EXERCISE_CHECKS ", " ︎");
        }

        return template;
    }

    protected  String buildJournalDateComments(String date,
                                               Map<String, Object> node) {
        String template = ConstantsHTML.JOURNAL_DATE_COMMENTS;
        if (node.containsKey(date)) {
            Map<String, Object> journalDetails = (Map<String, Object>)node.get(date);
            if (journalDetails.containsKey(KeysForFirebase.NOTES)) {
                template = template.replaceAll("HW_COMMENTS", (String) journalDetails.get(KeysForFirebase.NOTES));
            } else {
                template = template.replaceAll("HW_COMMENTS", " ");
            }
        } else {
            template = template.replaceAll("HW_COMMENTS", " ");

        }
        Map<String, Object> journalDetails = (Map<String, Object>)node.get(date);
        return template;
    }


    protected  String buildJournalDateTrailer() {
        return ConstantsHTML.JOURNAL_DATE_TRAILER;
    }




    // Charting
    void lineChartUdate(Map<String, Object> node) {
        Map<String, Object> nodeJournal;
        if (DEBUG) {
            Log.d(TAG, "displayJournalAndWeight: display client journal and weight chart");
        }
        mailboxButton.setVisibility(View.VISIBLE);
        message.setText("");
        if (node == null){
            return;
        }
        if (node.containsKey(KeysForFirebase.NODE_JOURNAL)){
            nodeJournal = (Map<String, Object>) node.get(KeysForFirebase.NODE_JOURNAL);
        } else {
            return;
        }
        Set<String> datesOfConsumpation = nodeJournal.keySet();
        ArrayList<String> sortedKeyDates = new ArrayList<String>(datesOfConsumpation);
        Collections.sort(sortedKeyDates);
        String startDate = "";
        Double startWeight = 0.0;
        Map<String, Double> chartDataPoint = new HashMap<String, Double>();
        Map<String, Object> nodeDate;
        double weight = 0f;
        for (String weightDate : sortedKeyDates) {
            if (nodeJournal.containsKey(weightDate)) {
                nodeDate = (Map<String, Object>) nodeJournal.get(weightDate);
            } else {
                nodeDate = new HashMap<String, Object>();
            }
            if (nodeDate.containsKey(KeysForFirebase.WEIGHED)) {
                weight = ((Number) nodeDate.get(KeysForFirebase.WEIGHED)).doubleValue();
                if (weight == 0f) {
                    continue; // skip days with zero weight
                }
            } else {
                continue; // skip days with missing weight
            }
            if (startDate.equals("")) {
                startWeight = weight;
                startDate = weightDate; // earliest date
                chartDataPoint.put(weightDate, 0.0);
            } else {
                chartDataPoint.put(weightDate, Double.valueOf(weight - startWeight));
            }

        }
        if (chartDataPoint.size() <= 1) {
            return; // no line to show
        }
        // now format chart series
        float xValue;
        float yValue;
        List<Entry> chartSeries = new ArrayList<Entry>();
        ArrayList<String> chartLabels = new ArrayList<String>();
        Entry point = new Entry();
        xValue = 0.0f;
        yValue = 0.0f;
        // change sort order
        for (String weightDate : sortedKeyDates) {
            point = new Entry();
            point.setX(xValue);
            if (chartDataPoint.containsKey(weightDate)) {
                point.setY(((Number) chartDataPoint.get(weightDate)).floatValue());
            } else {
                continue;
            }
            chartSeries.add(point);
            chartLabels.add(mmddDisplay(weightDate));
            xValue += 1.0;
        }
        Log.i(TAG, "lineChartUdate: chartSeries: " + chartSeries.size());
        Log.i(TAG, "lineChartUdate: chartLabels: " + chartLabels.size());
        LineDataSet weightDataSet = new LineDataSet(chartSeries, "Weight Loss/Gain");
        weightDataSet.setColor(ContextCompat.getColor(this, R.color.hw_green));
        weightDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.hw_green));
        weightDataSet.setCircleColors(ContextCompat.getColor(this,R.color.hw_green));
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override

            public String getFormattedValue(float value, AxisBase axis) {
                Log.i(TAG, "getFormattedValue: value = " + value);
                Log.i(TAG, "getFormattedValue: axis = " + axis.toString());
                if (((int) value ) >= chartLabels.size() || ((int) value) < 0) {
                    Log.i(TAG, "getFormattedValue: out of bounds index" + ((int) value));
                    return "bad label at " + value;
                } else {
                    Log.i(TAG, "getFormattedValue: getFormattedValue =" + chartLabels.get((int) value));
                    return  chartLabels.get((int) value);                }

            }
        };
        XAxis xAxis = weightChart.getXAxis();
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelRotationAngle(-45f);
        xAxis.setGranularity(1f);
        Description chartTitle = weightChart.getDescription();
        chartTitle.setText("The Healthy Way Maintenance Chart");
        LineData weightData = new LineData(weightDataSet);
        weightChart.setData(weightData);
        weightChart.setVisibility(View.VISIBLE);
        weightChart.invalidate();
    }

    private String mmddDisplay(String firebaseDate){
        String mmDD = "";
        if (firebaseDate == null) {
            return "??/??";
        } else {
            mmDD = firebaseDate.substring(5,7)+"/"+firebaseDate.substring(8,10);
        }
        return mmDD;
    }
}

