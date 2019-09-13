package com.india.lhq.onlineattendance.fragment;


import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.AppSingleton;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.database.AttendanceRegulertyData;
import com.india.lhq.onlineattendance.database.AttendenceFixData;
import com.india.lhq.onlineattendance.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.india.lhq.onlineattendance.activity.MainActivity.createConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRegularityFragment extends Fragment {

    DatePicker picker;
    Button displayDate;
    TextView textview1;
    Button btnInTimePicker, btnOutTimePicker,btnSave;
    TextView txtInTime, txtOutTime;
    LinearLayout li_outime;
    private int mHour, mMinute;
    private String format = "";
    private String empid = "";
    EditText edt_remark;
    DatabaseHandler db;
    String time;
    private Connection connection;

    public AttendanceRegularityFragment() {
        // Required empty public constructor
    }

    public static AttendanceRegularityFragment newInstance(int index) {
        AttendanceRegularityFragment f = new AttendanceRegularityFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance_regularization, container, false);
        db = new DatabaseHandler(getActivity());
        time = DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstants.EMPID);
        init_view(v);
        picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
        return v;
    }

    private void init_view(View v) {

        textview1 = (TextView) v.findViewById(R.id.textView1);
        picker = (DatePicker) v.findViewById(R.id.datePicker);
        displayDate = (Button) v.findViewById(R.id.button1);
        edt_remark = (EditText) v.findViewById(R.id.write_post);
        li_outime = (LinearLayout) v.findViewById(R.id.li_outime);

        textview1.setText("" + getCurrentDate());
        displayDate.setVisibility(View.GONE);
     // picker.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    int month = monthOfYear+1;
                    textview1.setText("" + dayOfMonth  + "-" +month + "-" + year);
                }
            });
        } else {
            displayDate.setVisibility(View.VISIBLE);
            displayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    textview1.setText("Select Date: " + getCurrentDate());
                }

            });
        }

        btnInTimePicker = (Button) v.findViewById(R.id.btn_intime);
        btnOutTimePicker = (Button) v.findViewById(R.id.btn_outtime);
        txtInTime = (TextView) v.findViewById(R.id.in_time);
        txtOutTime = (TextView) v.findViewById(R.id.out_time);
        btnSave = (Button) v.findViewById(R.id.btn_save);

        btnInTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                showTime(hourOfDay, minute, "Intime");
                            //  txtInTime.setText(hourOfDay + ":" + minute+" "+am_pm);
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }

        });

        btnOutTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                showTime(hourOfDay, minute, "Outtime");
                          //    txtOutTime.setText(hourOfDay + ":" + minute +" "+am_pm);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(textview1.getText().toString().equalsIgnoreCase("")){
                    Snackbar.make(getView(),"Please Select Date",
                            Snackbar.LENGTH_SHORT).show();
                  return;
                }
                if(txtInTime.getText().toString().equalsIgnoreCase("InTime")){
                    Snackbar.make(getView(),"Please Select InTime",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(txtOutTime.getText().toString().equalsIgnoreCase("OutTime")){
                    Snackbar.make(getView(),"Please Select OutTime",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(edt_remark.getText().toString().equalsIgnoreCase("")){
                    Snackbar.make(getView(),"Please fill reason",
                            Snackbar.LENGTH_SHORT).show();
                    edt_remark.setError("Please fill reason");
                    return;
                }
                db.insertAttandanceRegulerty(new AttendanceRegulertyData(empid,textview1.getText().toString(),txtInTime.getText().toString(),
                        txtOutTime.getText().toString(),edt_remark.getText().toString(),0,time));
                sendDataonServer();
               // sendAllDataServerVolly();
            }
        });
    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(picker.getDayOfMonth() + "-");
        builder.append((picker.getMonth() + 1) + "-");//month is 0 based
        builder.append(picker.getYear());
        return builder.toString();

    }

    public void showTime(int hour, int min, String inout) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        if (inout.equalsIgnoreCase("Intime")) {
            txtInTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
                    .append(" ").append(format));
            li_outime.setVisibility(View.VISIBLE);
        } else {
            txtOutTime.setText(new StringBuilder().append(hour).append(" : ").append(min)
                    .append(" ").append(format));
        }
    }

    private void getTimeFromAndroid() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        String am_pm = (hours < 12) ? "AM" : "PM";

//        if(hourOfDay < 12) {
//            AM_PM = "AM";
//        } else {
//            AM_PM = "PM";
//        }

        if (hours >= 1 && hours <= 12) {
            Toast.makeText(getActivity(), "Good Morning", Toast.LENGTH_SHORT).show();
        } else if (hours >= 12 && hours <= 16) {
            Toast.makeText(getActivity(), "Good Afternoon", Toast.LENGTH_SHORT).show();
        } else if (hours >= 16 && hours <= 21) {
            Toast.makeText(getActivity(), "Good Evening", Toast.LENGTH_SHORT).show();
        } else if (hours >= 21 && hours <= 24) {
            Toast.makeText(getActivity(), "Good Night", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDataonServer() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            this.connection = createConnection();
         //   Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();

            Snackbar.make(getView(), "Connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            String query ="insert into AttendanceRegualerty values('"+empid+"','"+textview1.getText().toString()+"','"+
                   txtInTime.getText().toString()+"','"+txtOutTime.getText().toString()+"','"+edt_remark.getText().toString()+"',"+ "'1'," +"'"+time+"'"+   ")";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
         // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");

            while (rs.next()) {
                  stringBuffer.append( rs.getString(1)+"\n");
            }

            Snackbar.make(getView(),"All Records Save Successfully",
                    Snackbar.LENGTH_SHORT).show();
            connection.close();
        } catch (Exception e) {

            Snackbar.make(getView(), "" + e, Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



//............................USING VOLLY>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private JSONObject JsonParameterSendServer() {
        List<AttendanceRegulertyData> dataLast = db.getLastRowAttendanceRegulerty();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmpId", empid);
            jsonObject.put("Date", dataLast.get(0).getDate());
            jsonObject.put("InTime", dataLast.get(0).getIntime());
            jsonObject.put("OutTime", dataLast.get(0).getOuttime());
            jsonObject.put("edt_remark", dataLast.get(0).getRemark());
            jsonObject.put("currenttime", dataLast.get(0).getTime());
            jsonObject.put("status", dataLast.get(0).getFlag()+"");

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    private void sendAllDataServerVolly() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, AppConstants.ATTENDANCEMASTER, JsonParameterSendServer(),
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        Log.e("res ", response + "");

                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("Status");
                            if(status.equalsIgnoreCase("Success")){
                   //             db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  1);

                                Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                            }else{
                  //              db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);

                                Snackbar.make(getView(), "Uploaded failed...", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                  //          db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);

                            Snackbar.make(getView(), "Json Parse Exception...", Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        //  db.insertAttandanceRemote(new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",1));


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Errors--", error + "");
                        pDialog.dismiss();
                        Snackbar.make(getView(), "Uploaded Failed Exception...", Snackbar.LENGTH_LONG).show();
                    //       db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);


                    }
                }) {

        /*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }  */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

}




