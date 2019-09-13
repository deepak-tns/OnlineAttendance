package com.india.lhq.onlineattendance.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.AppSingleton;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.constants.JDBCConnection;
import com.india.lhq.onlineattendance.constants.Validator;
import com.india.lhq.onlineattendance.database.DatabaseHandler;
import com.india.lhq.onlineattendance.database.Emp_Job_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.india.lhq.onlineattendance.constants.AppConstants.ALERT_TYPE_NO_NETWORK;
import static com.india.lhq.onlineattendance.constants.JDBCConnection.createConnection;


public class LoginActivity extends AppCompatActivity {
    String[] permissions = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.READ_PHONE_STATE",
            "android.permission.CAMERA"
    };
    public static final int MULTIPLE_PERMISSIONS = 10;
    private String emino;
    private EditText edt_loginid;
    private EditText edt_password;
    private Button btn_login;
    private SharedPreferenceUtils sharedPreferences;
    private Connection connection ;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(getApplicationContext());
        sharedPreferences = SharedPreferenceUtils.getInstance();
        sharedPreferences.setContext(getApplicationContext());
      //  networkUsage();
      //  allAppsNetworkUsage();

        findIds();
        checkPermissions();
        //JSonobjParameter("18459","18459");

    }

    void networkUsage() {
        // Get running processes
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningApp : runningApps) {
            long received = TrafficStats.getUidRxBytes(runningApp.uid);
            long sent = TrafficStats.getUidTxBytes(runningApp.uid);
            Log.d("network", String.format(Locale.getDefault(),
                    "uid: %1d - name: %s: Sent = %1d, Rcvd = %1d", runningApp.uid, runningApp.processName, sent, received));
        }
    }

    void allAppsNetworkUsage() {
        final PackageManager pm = getPackageManager();
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        for (int i = 0; i < appProcesses.size(); i++) {
            Log.d("Executed app", "Application executed : " + appProcesses.get(i).processName + "\t\t ID: " + appProcesses.get(i).pid + "");
            //  String packageName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            //String packageName = appProcesses.get(i)..getPackageName();
            ApplicationInfo app = null;
            try {
                app = pm.getApplicationInfo(appProcesses.get(i).processName, 0);
                if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
                    //it's a system app, not interested
                } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //Discard this one
                    //in this case, it should be a user-installed app
                } else {
                    // tx = TrafficStats.getUidTxBytes(app.uid);
                    //rx = TrafficStats.getUidRxBytes(app.uid);
                    long delta_rx = TrafficStats.getUidRxBytes(app.uid) ;

                    long delta_tx = TrafficStats.getUidTxBytes(app.uid);

                    Log.v("networkall" + app, "Send :" + delta_tx + ", Received :" + delta_rx);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


        }

    }
    public  String getIMEINumber(Context context) {
        String tmDeviceId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
             tmDeviceId = tm.getDeviceId();
        }else{
             tmDeviceId = tm.getDeviceId();
        }

        return tmDeviceId;
    }

    private void findIds() {
        edt_loginid = findViewById(R.id.editEmail);
        edt_password = findViewById(R.id.editPassword);
        btn_login = findViewById(R.id.buttonLogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emino = getIMEINumber(LoginActivity.this);
                Log.v("imeno", emino);
                validatelogin();
            }
        });

    //    getnotificationcount();
    }

    private void validatelogin() {
        if (Validator.isNetworkAvailable(this)) {
            if (isMandatoryFields()) {
                // toHideKeyboard();
                //initLoginModel();
              //  toLogin(edt_loginid.getText().toString(), edt_password.getText().toString());
               // startActivity(new Intent(this, MainActivity.class));

                sendDataonServer();
            }
        } else {
            alert(this, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

        }
    }

    private boolean isMandatoryFields() {
        edt_loginid.setError(null);
        edt_password.setError(null);
        if (edt_loginid.getText().toString().equals("")) {
            edt_loginid.requestFocus();
            edt_loginid.setError("Enter LoginID");
            return false;
        } else if (edt_password.getText().toString().isEmpty()) {
            edt_password.requestFocus();
            edt_password.setError(getResources().getString(R.string.error_password_empty));
            return false;
        }

        return true;
    }

    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }

        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }
        builder.show();
    }

   /* private void toLogin(String empId, String empPassword) {
        //  +"?Loginid="+empId+"&password="+empPassword+"&imeno="+"1234567890"
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                AppConstants.VERIFYLOGINURL, JSonobjParameter(empId, empPassword),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseSettingResponse(response);
                        Log.v("login response", response.toString());
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("login response error", error.toString());
                pDialog.hide();
            }

        });
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        AppSingleton.getInstance(this).addToRequestQueue(jsonObjReq, null);
    }
    private void parseSettingResponse(JSONArray response) {
        try {
            JSONArray jsonArray = new JSONArray(response.toString());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String status = jsonObject.getString("Status");

            if (status.equals("1")) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
                sharedPreferences = SharedPreferenceUtils.getInstance();
                sharedPreferences.setContext(getApplicationContext());
                sharedPreferences.putString(AppConstants.EMPID, edt_loginid.getText().toString());

            } else {
               // startActivity(new Intent(this, MainActivity.class));
                Toast.makeText(this, "Invalid loginid or passsword", Toast.LENGTH_SHORT).show();

            }
            // String password = jsonObject.getString("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private JSONObject JSonobjParameter(String loginname, String password) {
        JSONObject jsonObject = new JSONObject();
        try {

       *//* JSONArray jsonArrayParameter = new JSONArray();
          jsonArrayParameter.put(loginname);
          jsonArrayParameter.put(password);*//*

            //jsonObject.put( "SqlQuery","select top 1 * from tbl_opreg");

            // jsonObject.put("DatabaseName", "TNS_HR");
            //  jsonObject.put("ServerName", "bkp-server");
            //  jsonObject.put("UserId", "sanjay");
            // jsonObject.put("Password", "tnssoft");
            //  jsonObject.put("spName", "USP_login");

            jsonObject.put("LoginId", loginname);
            jsonObject.put("password", password);
            jsonObject.put("imeno", emino);

        Log.v("imeno",emino);
            // jsonObject.put("ParameterList",jsonArrayParameter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }*/

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions)
        {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else
                {
                    // no permissions granted.
                    Toast.makeText(this, "permission not Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void getnotificationcount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://13.229.8.149/wmsapiinv/api/login/GetDashboard",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) {

                        } else {
                            String s = response.trim();
                            System.out.println("msgmainactivity:::" + s);
                            Log.v("data",response);

                            try {
                                JSONArray mJsonArray = new JSONArray(s);
                                JSONObject mJsonObject = mJsonArray.getJSONObject(0);
                                String dashtatus = mJsonObject.getString("dashboardstatus");
                                String inward = mJsonObject.getString("inward");

                             //   Log.v("data",dashtatus);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_password_empty), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("projectcode","DICWHR1800000");
                params.put("whcode", "WHSHA01");
                params.put("empcode", "OPS1103");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json");
                // params.put("Synchronized32!_89ioHeader", "Synchronized75!_89ioAppid:Synchronized75!_89ioAppkey");
                return params;
            }
        };

        AppSingleton.getInstance(this).addToRequestQueue(stringRequest, null);

    }

    private void sendDataonServer() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = createConnection();
            //   Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            String query ="select * from LQAPP_EMP_JOB_DETAILS where emp_id = '"+edt_loginid.getText().toString() + "'"+ "and employee_password = '"+edt_password.getText().toString()+"'" ;

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            while (rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                Log.v("onejobstatus",rs.getString(7)+"\n");
                Log.v("oneprojectsubloc",rs.getString(18)+"\n");
                Log.v("oneresourcetype",rs.getString(19)+"\n");
                Log.v("oneclientid",rs.getString(24)+"\n");
                Log.v("oneprojectid",rs.getString(25)+"\n");
                Log.v("onelocationid",rs.getString(26)+"\n");
                Log.v("onedesigid",rs.getString(27)+"\n");
                Log.v("onedepid",rs.getString(28)+"\n");
                Log.v("onereportingcordinateid",rs.getString(81)+"\n");
                Log.v("onereportingmanagerid",rs.getString(82)+"\n");
                Log.v("oneprojectheadid",rs.getString(83)+"");

                Toast.makeText(this,"All Records Save Successfully",
                        Toast.LENGTH_SHORT).show();
                sharedPreferences.putString(AppConstants.EMPID, edt_loginid.getText().toString());



                db.insertEMPJOBDETAIL(new Emp_Job_Details(rs.getString(1),rs.getString(7),rs.getString(18),rs.getString(19),
                        rs.getString(24),rs.getString(25),rs.getString(26),rs.getString(27),rs.getString(28),rs.getString(82),
                        rs.getString(83),rs.getString(81)));


                startActivity( new Intent(this, MainActivity.class));
            }
            Toast.makeText(this, "" + "Login Error", Toast.LENGTH_SHORT).show();
            connection.close();
        } catch (Exception e) {

            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }




}
