package us.thehealthyway.healthywayadminandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CreateNewAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // return to AdminAcitivity
        Intent intent = new Intent();
        intent.putExtra(HealthyWayAdminActivities.HealthyWayViews.VIEW_CREATE_NEW_ACCOUNT.getName(),
                HealthyWayAdminActivities.ADMIN_ACTIVITY);
        setResult(Activity.RESULT_OK, intent);
//        finish();
    }

    public  static Intent makeIntent(Context context){
        return new Intent(context, CreateNewAccount.class);
    }


}
