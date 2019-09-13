package com.india.lhq.onlineattendance.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.fragment.AttendanceApprovalFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceFixFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceRegularityFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceRemoteFragment;
import com.india.lhq.onlineattendance.fragment.InfraAuditDetailsFragment;

public class AllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        Toolbar toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle(null);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if(savedInstanceState == null){
            String message = getIntent().getStringExtra("MESSAGENAME");

            if(message.equalsIgnoreCase("AttendanceRemote")){
                getSupportFragmentManager().beginTransaction().add(R.id.li_all, new AttendanceRemoteFragment()).commit();
            }
            if(message.equalsIgnoreCase("AttendanceFix")){
                getSupportFragmentManager().beginTransaction().add(R.id.li_all, new AttendanceFixFragment()).commit();
            }
            if(message.equalsIgnoreCase("AttendanceRegularization")){
                getSupportFragmentManager().beginTransaction().add(R.id.li_all, new AttendanceRegularityFragment()).commit();
            }
            if(message.equalsIgnoreCase("AttendanceApproval")){
                getSupportFragmentManager().beginTransaction().add(R.id.li_all, new AttendanceApprovalFragment()).commit();
            }
            if(message.equalsIgnoreCase("InfraDetail")){
                getSupportFragmentManager().beginTransaction().add(R.id.li_all, new InfraAuditDetailsFragment()).commit();
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
