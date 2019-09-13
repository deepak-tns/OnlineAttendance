package com.india.lhq.onlineattendance.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.fragment.AttendanceApprovalFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceFixFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceRegularityFragment;
import com.india.lhq.onlineattendance.fragment.AttendanceRemoteFragment;
import com.india.lhq.onlineattendance.fragment.HomeFragment;
import com.india.lhq.onlineattendance.fragment.InfraAuditDetailsFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    private TextView tv_attendancereomte;
    private TextView tv_attendancefix;
    private TextView tv_attendanceregulerty;
    private TextView tv_infraauditdetail;
    private TextView tv_attendance_approval;

    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@203.122.13.203:1521:orcl";
    private static final String DEFAULT_USERNAME = "lqapp";
    private static final String DEFAULT_PASSWORD = "lqapp";

    TextView tv_Title ;


    private Connection connection;
   int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
         tv_Title =(TextView)toolbar.findViewById(R.id.tv_toolbar);


        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new HomeFragment()).commit();
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationdrawer();
        findIds();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

   /*    if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            this.connection = createConnection();
            Toast.makeText(MainActivity.this, "Connected",
                    Toast.LENGTH_SHORT).show();
            Statement stmt=connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
            ResultSet rs=stmt.executeQuery("select * from customers");
         // ResultSet rs=stmt.executeQuery("insert into customers values(101,'deepak','delhi')");

           *//* while(rs.next()) {

                stringBuffer.append( rs.getString(1)+"\n");
                tv_attendancefix.setText(rs.getString(1)+"\n");
                tv_attendancereomte.setText(rs.getString(2)+"\n");
            }*//*

            ArrayList<Customer> list = new ArrayList<>();

            while (rs.next()) {
                    Customer data = new Customer();
                    //only one column
                    data.setId(rs.getInt(1));  //REMOTE ATT_REMOTE_INCRI

                    data.setName(rs.getString(2));// ATT_REMOTE_EMPID
                    data.setCity(rs.getString(3));// ATT_REMOTE_CURRENTDATE

                    //you could add additional columns here..
                    list.add(data);
                }



            Log.v("buffer",stringBuffer.toString());
            Toast.makeText(MainActivity.this, stringBuffer.toString(),
                    Toast.LENGTH_SHORT).show();
            connection.close();
        }
        catch (Exception e) {

            Toast.makeText(MainActivity.this, ""+e,
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/


    }
    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }
    private void findIds(){
        tv_attendancereomte=(TextView)findViewById(R.id.tv_attendancereomte);
        tv_attendancefix=(TextView)findViewById(R.id.tv_attendancefix);
        tv_attendanceregulerty=(TextView)findViewById(R.id.tv_attendanceregulerty);
        tv_infraauditdetail=(TextView)findViewById(R.id.tv_infraauditdetail);
        tv_attendance_approval=(TextView)findViewById(R.id.tv_attendance_approval);

        tv_attendancereomte.setOnClickListener(this);
        tv_attendancefix.setOnClickListener(this);
        tv_attendanceregulerty.setOnClickListener(this);
        tv_infraauditdetail.setOnClickListener(this);
        tv_attendance_approval.setOnClickListener(this);

    }

    private void navigationdrawer() {
        mDrawerPane = (LinearLayout) findViewById(R.id.drawerPane);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        );

    }

    @Override
    public void onClick(View v) {

   /*     if( v == linear_att_anywhere){
            startActivity(new Intent(this,AttendanceRemoteActivity.class));
        }
        if( v == linear_att_fix){
            startActivity(new Intent(this,AttendanceFixActivity.class));
        }*/

        if(v == tv_attendancereomte){

            //  getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,SiteSurveyFragment.newInstance(1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new AttendanceRemoteFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            tv_Title.setText("Attendance Remote");
            // startActivity(new Intent (this,RegisterActivity.class));

        }  if(v == tv_attendancefix){

            //  getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,SiteSurveyFragment.newInstance(1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new AttendanceFixFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            // startActivity(new Intent (this,RegisterActivity.class));
            tv_Title.setText("Attendance Fix");
        }

        if(v == tv_attendanceregulerty){

            //  getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,SiteSurveyFragment.newInstance(1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, AttendanceRegularityFragment.newInstance(1)).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            // startActivity(new Intent (this,RegisterActivity.class));
            tv_Title.setText("Attendance Regularization");
        } if(v == tv_infraauditdetail){

            //  getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,SiteSurveyFragment.newInstance(1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new InfraAuditDetailsFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            // startActivity(new Intent (this,RegisterActivity.class));
            tv_Title.setText("Infra Audit Detail");

        }
        if(v == tv_attendance_approval){

            //  getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag,SiteSurveyFragment.newInstance(1)).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_home_frag, new AttendanceApprovalFragment()).addToBackStack(null).commit();
            mDrawerLayout.closeDrawer(mDrawerPane);
            // startActivity(new Intent (this,RegisterActivity.class));
            tv_Title.setText("Attendance Approval");

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
