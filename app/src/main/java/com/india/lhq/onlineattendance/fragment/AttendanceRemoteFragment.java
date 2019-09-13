package com.india.lhq.onlineattendance.fragment;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.AppSingleton;
import com.india.lhq.onlineattendance.Utils.DrawBitmapAll;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.activity.AttendanceRemoteHistoryActivity;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.constants.GoogleGPSService;
import com.india.lhq.onlineattendance.database.AttendenceRemoteData;
import com.india.lhq.onlineattendance.database.DatabaseHandler;
import com.india.lhq.onlineattendance.database.Emp_Job_Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.india.lhq.onlineattendance.constants.JDBCConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceRemoteFragment extends Fragment implements LocationListener, View.OnClickListener {

    DatabaseHandler db;
    private ImageView edt_attandence_intime, edt_attandence_outtime;
    private TextView tv_intime_get, tv_outtime_get, txt_currdate, tvlatlong;
    private TextView tv_attendance_remote_history;
    private Button btn_in_preview, btn_out_preview;
    private Button btn_save;
    private String intimeIvEncodeString = "", outtime_IvEncodeString = "";
    private Geocoder geocoder;
    private String empid;
    String current_date;
    private String lat = "Unknown";
    private String longi = "Unknown";
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private EditText edt_remote_intimeremark, edt_remote_outtimeremark;
    private Connection connection;
    private String time;
    List<AttendenceRemoteData> data;
    private String Job_status,client_id,project_id,location_id,desig_id,dept_id,reportingcordinator_id,reportingmanger_id,projecthead_id;
    private int thisMonth,thisyear;

    public AttendanceRemoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance_remote, container, false);
        geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        if (!GoogleGPSService.isRunning) {
            getActivity().startService(new Intent(getActivity(), GoogleGPSService.class));
        }
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstants.EMPID);
        time = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();
        db = new DatabaseHandler(getActivity());
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());
        findIds(v);
        txt_currdate.setText(current_date);
        //    getLocation();

        thisMonth = cal.get(Calendar.MONTH);
        Log.d("month", "@ thisMonth : " + (thisMonth+1));
         thisMonth =thisMonth+1;
         thisyear = cal.get(Calendar.YEAR);
        Log.d("year", "@ thisyear : " + thisyear);

         List<Emp_Job_Details> emp_job_details = new ArrayList<>();
         emp_job_details = db.getLastRowEMPJOBDETAILS();

         for(Emp_Job_Details data : emp_job_details){

             Job_status = data.getJOB_STATUS();
             client_id = data.getCLIENT_ID();
             project_id =data.getPROJECT_ID();
             location_id =data.getLOCATION_ID();
             desig_id =data.getDESIG_ID();
             dept_id =data.getDEPT_ID();
             reportingcordinator_id =data.getREPORTING_CORDINATE_ID();
             reportingmanger_id =data.getREPORTING_MANGER_ID();
             projecthead_id =data.getPROJECT_HEAD_ID();

            Log.v("oneprojectheadid>>list",data.getPROJECT_HEAD_ID());

        }
        data = db.getLastRowAttendanceRemote();
        if (data.size() > 0) {
            if (current_date.equalsIgnoreCase(data.get(0).getCurrentDate())) {
                edt_attandence_intime.setVisibility(View.VISIBLE);
                tv_intime_get.setText(data.get(0).getInTime());
                tv_intime_get.setVisibility(View.VISIBLE);
              //  btn_in_preview.setVisibility(View.VISIBLE);
              //  btn_out_preview.setVisibility(View.VISIBLE);
                tv_outtime_get.setText(data.get(0).getOutTime());
                tv_outtime_get.setVisibility(View.VISIBLE);
                edt_attandence_outtime.setVisibility(View.VISIBLE);

                //  btn_save.setVisibility(View.GONE);
                //  alertdiaolog();
            }
        }


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        //   locationManager.removeUpdates(this);
    }

    private void findIds(View v) {
        tvlatlong = (TextView) v.findViewById(R.id.latlong);
        txt_currdate = (TextView) v.findViewById(R.id.txt_currdate);
        edt_attandence_intime = (ImageView) v.findViewById(R.id.edt_attandence_intime);
        edt_attandence_outtime = (ImageView) v.findViewById(R.id.edt_attandence_outtime);
        tv_intime_get = (TextView) v.findViewById(R.id.tv_intime_get);
        tv_outtime_get = (TextView) v.findViewById(R.id.tv_outtime_get);
        btn_in_preview = (Button) v.findViewById(R.id.btn_in_preview);
        btn_out_preview = (Button) v.findViewById(R.id.btn_out_preview);
        btn_save = (Button) v.findViewById(R.id.btn_save);
        tv_attendance_remote_history = (TextView) v.findViewById(R.id.tv_attendance_remote_history);
        edt_remote_intimeremark = (EditText) v.findViewById(R.id.edt_remote_intimeremark);
        edt_remote_outtimeremark = (EditText) v.findViewById(R.id.edt_remote_outtimeremark);
        edt_attandence_intime.setOnClickListener(this);
        edt_attandence_outtime.setOnClickListener(this);
        btn_in_preview.setOnClickListener(this);
        btn_out_preview.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_attendance_remote_history.setOnClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        longi = String.valueOf(location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        //  Toast.makeText(getActivity(), lats + "," + longi, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.edt_attandence_intime) {
          //  selectImage("intime");
            tv_intime_get.setText(time);
            tv_intime_get.setVisibility(View.VISIBLE);
            edt_attandence_outtime.setVisibility(View.VISIBLE);
            db.insertAttandanceRemote(new AttendenceRemoteData(empid, current_date, tv_intime_get.getText().toString(), tv_outtime_get.getText().toString(), intimeIvEncodeString, outtime_IvEncodeString, lat, longi, lat, longi, edt_remote_intimeremark.getText().toString(), "outtime", "near1", tvlatlong.getText().toString(), 0));
            insertsendDataonServer();
            edt_attandence_intime.setVisibility(View.GONE);
        }
        if (view.getId() == R.id.edt_attandence_outtime) {
          //  selectImage("outtime");
            tv_outtime_get.setVisibility(View.VISIBLE);
            tv_outtime_get.setText(time);
            db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), tvlatlong.getText().toString(), 0);

        }
        if (view.getId() == R.id.btn_in_preview) {
            imagePreviewDialogIn();
        }
        if (view.getId() == R.id.btn_out_preview) {
            imagePreviewDialogOut();
        }
        if (view.getId() == R.id.btn_save) {
            if (tv_intime_get.getText().toString().equals("Intime")) {
                Snackbar.make(getView(), "Please Capture Intime Image", Snackbar.LENGTH_LONG).show();
                return;
            }
            if (tv_outtime_get.getText().toString().equals("Outtime")) {
                Snackbar.make(getView(), "Please Capture Outtime Image", Snackbar.LENGTH_LONG).show();
                return;
            } else {
              //  sendAllDataServer();
                updatesendDataonServer();
            }
        }
        if (view.getId() == R.id.tv_attendance_remote_history) {
            Intent intent = new Intent(getActivity(), AttendanceRemoteHistoryActivity.class);
            intent.putExtra("value", "Remote");
            startActivity(intent);
        }
    }

    public void imagePreviewDialogIn() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_photo_preview);
        final   TextView tv_image_not_found = (TextView) dialog.findViewById(R.id.tv_image_not_found);
        data = db.getLastRowAttendanceRemote();
        if (!(data.get(0).getIntimeImage()).equals("")) {
            Bitmap bt = decodeBase64(data.get(0).getIntimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        } else {

            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Snackbar.make(getView(), "Please Capture Image First", Snackbar.LENGTH_LONG).show();
        }
        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }

    public void imagePreviewDialogOut() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_photo_preview);
        final TextView tv_image_not_found = (TextView) dialog.findViewById(R.id.tv_image_not_found);
        data = db.getLastRowAttendanceRemote();

        if (!(data.get(0).getOuttimeImage()).equals("")) {

            Bitmap bt = decodeBase64(data.get(0).getOuttimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        } else {
            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Snackbar.make(getView(), "Please Capture Image First", Snackbar.LENGTH_LONG).show();
        }

        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }


    private void onCaptureImageResult(Intent data, String name) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (name.equals("intime")) {

            SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
            String current_time_str = time_formatter.format(System.currentTimeMillis());
            if (lat == null) {
                Snackbar.make(getView(), "please wait gps location not found", Snackbar.LENGTH_LONG).show();

            } else {
                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(longi);
                String totalString = current_date + current_time_str + "\nLat :" + String.format("%.4f", latitude) + ",  Long :" + String.format("%.4f", longitude) + "\nInTime Image ";
                // Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                String destinationpath = Environment.getExternalStorageDirectory().toString();
                File destination = new File(destinationpath + "/LQAttendance/");

                if (!destination.exists()) {
                    destination.mkdirs();
                }
                File file = null;
                FileOutputStream fo;
                try {
                    // destination.createNewFile();
                    file = new File(destination, current_date + "_" + current_time_str + ".jpg");
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
                String path = (file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
                // startkmImageEncodeString = encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG, 50);
                intimeIvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 80);

                tv_intime_get.setText(current_time_str);
                tv_intime_get.setVisibility(View.VISIBLE);
                edt_attandence_outtime.setClickable(true);
                edt_attandence_outtime.setFocusable(true);
                edt_attandence_outtime.setVisibility(View.VISIBLE);
                btn_in_preview.setVisibility(View.GONE);
                edt_attandence_intime.setVisibility(View.INVISIBLE);
                db.insertAttandanceRemote(new AttendenceRemoteData(empid, current_date, tv_intime_get.getText().toString(), tv_outtime_get.getText().toString(), intimeIvEncodeString, outtime_IvEncodeString, lat, longi, lat, longi, edt_remote_intimeremark.getText().toString(), "outtime", "near1", tvlatlong.getText().toString(), 0));
                insertsendDataonServer();
            }
        }

        if (name.equals("outtime")) {
            SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
            String current_time_str = time_formatter.format(System.currentTimeMillis());

            if (lat == null) {
                Snackbar.make(getView(), "please wait gps location not found", Snackbar.LENGTH_LONG).show();

            } else {

                latitude = Double.parseDouble(lat);
                longitude = Double.parseDouble(longi);

                String totalString = current_date + current_time_str + "\nLat:" + String.format("%.4f", latitude) + ",Long:" + String.format("%.4f", longitude) + "\nOutTime Image ";
                //  Bitmap setTextwithImage =    ProcessingBitmap(thumbnail,totalString);
                Bitmap setTextwithImage = DrawBitmapAll.drawTextToBitmap(getContext(), thumbnail, totalString);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                setTextwithImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String destinationpath = Environment.getExternalStorageDirectory().toString();

                File destination = new File(destinationpath + "/LQAttendance/");
                if (!destination.exists()) {
                    destination.mkdirs();
                }
                FileOutputStream fo;
                try {
                    //  file.createNewFile();
                    File file = new File(destination, current_date + "_" + current_time_str + ".jpg");
                    fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                String path = (destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
                // endkmImageEncodeString= encodeToBase64(thumbnail, Bitmap.CompressFormat.JPEG,50);
                outtime_IvEncodeString = encodeToBase64(setTextwithImage, Bitmap.CompressFormat.JPEG, 80);
                tv_outtime_get.setText(current_time_str);
                tv_outtime_get.setVisibility(View.VISIBLE);
                btn_out_preview.setVisibility(View.GONE);
                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), tvlatlong.getText().toString(), 0);

            }
            // setImg.setImageBitmap(thumbnail);
        }
    }

    private void selectImage(String Value) {

        if (Value.equals("intime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);*/
            startActivityForResult(intent, 3);
        }
        if (Value.equals("outtime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        /*    intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);*/
            startActivityForResult(intent, 4);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if (requestCode == 3) {
                onCaptureImageResult(data, "intime");
            }

            if (requestCode == 4) {
                onCaptureImageResult(data, "outtime");
            }
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private JSONObject JsonParameterSendServer() {
        List<AttendenceRemoteData> dataLast = db.getLastRowAttendanceRemote();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", tv_intime_get.getText().toString());
            jsonObject.put("InTimePic", dataLast.get(0).getIntimeImage());
            jsonObject.put("OutTime", tv_outtime_get.getText().toString());
            jsonObject.put("OutTimePic", dataLast.get(0).getOuttimeImage());
            jsonObject.put("InLat", dataLast.get(0).getInlat());
            jsonObject.put("InLog", dataLast.get(0).getInlongi());
            jsonObject.put("OutLat", dataLast.get(0).getOutlat());
            jsonObject.put("OutLog", dataLast.get(0).getOutlongi());
            jsonObject.put("LocationType", "Anywhere");
            jsonObject.put("RadialDistance", dataLast.get(0).getOuttimeNear());
            jsonObject.put("Date", current_date);
            Log.i("Json Param", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void sendAllDataServer() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, AppConstants.ATTENDANCEMASTER, JsonParameterSendServer(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        Log.e("Res outtime", response + "");
                        btn_save.setEnabled(false);
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("Status");
                            if (status.equalsIgnoreCase("Success")) {
                                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), "", 1);

                                Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                            } else {
                                db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), tvlatlong.getText().toString(), 0);

                                Snackbar.make(getView(), "Uploaded failed...", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), tvlatlong.getText().toString(), 0);

                            Snackbar.make(getView(), "Json Parse Exception...", Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        //  db.insertAttandanceRemote(new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",1));

                        edt_remote_intimeremark.getText().clear();
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Errors--", error + "");
                        pDialog.dismiss();
                        Snackbar.make(getView(), "Uploaded Failed Exception...", Snackbar.LENGTH_LONG).show();
                        //   db.insertAttandanceRemote(new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",0));
                        db.updateAttandenceRemoteStatus(db.getLastInsertIdAttendanceRemote(), tv_outtime_get.getText().toString(), outtime_IvEncodeString, lat, longi, edt_remote_intimeremark.getText().toString(), tvlatlong.getText().toString(), 0);

                        //  edt_remote_intimeremark.getText().clear();
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

    private JSONObject JsonParameterSendServerInTime() {

        data = db.getLastRowAttendanceRemote();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", tv_intime_get.getText().toString());
            jsonObject.put("InTimePic", data.get(0).getIntimeImage());
            jsonObject.put("OutTime", "");
            jsonObject.put("OutTimePic", "");
            jsonObject.put("InLat", data.get(0).getInlat());
            jsonObject.put("InLog", data.get(0).getInlongi());
            jsonObject.put("OutLat", "");
            jsonObject.put("OutLog", "");
            jsonObject.put("LocationType", "Anywhere");
            jsonObject.put("RadialDistance", data.get(0).getOuttimeNear());
            jsonObject.put("Date", current_date);
            Log.i("Json Param", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void sendAllDataServerIntime() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, AppConstants.ATTENDANCEMASTER, JsonParameterSendServerInTime(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        Log.e("res intime", response + "");

                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("Status");
                            if (status.equalsIgnoreCase("Success")) {
                                Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                                //  edt_remote_intimeremark.getText().clear();
                            } else {
                                Snackbar.make(getView(), "Uploaded failed...", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Snackbar.make(getView(), "Json Parse Exception...", Snackbar.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Errors--", error + "");
                        pDialog.dismiss();
                        Snackbar.make(getView(), "Uploaded Failed Exception...", Snackbar.LENGTH_LONG).show();

                    }
                }) {

        /*
          @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
               // params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    //.........................................oracle...........................................
    private void insertsendDataonServer() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = createConnection();
            //   Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            String query = "insert into AttendanceRemote values( seq.nextval, '"+empid+"','"+tv_intime_get.getText().toString()+"','"+
                    "NA"+"','"+"NA"+"','"+"NA"+"',"+"'"+ data.get(0).getInlat() +"'"+","+
                    "'"+ data.get(0).getInlongi() +"','"+ data.get(0).getOutlat() +"',"+"'"+ data.get(0).getOutlongi() +
                    "'"+","+ "'"+ "NA" +"','"+ data.get(0).getOuttimeNear() +"','"+ current_date +"','"+ Job_status +"','"+ client_id
                    +"','"+ project_id +"','"+ location_id +"','"+ desig_id +"','"+ dept_id +"','"+ reportingcordinator_id
                    +"','"+ reportingmanger_id +"','"+ projecthead_id +"','" + "Pending"+"','"+"Pending"+"','"+"Pending'"+","+"'"+thisMonth+""+"','"
                    +thisyear+""+"','"+"Present'"+")";

                    //"'"+time+"'"+   ")";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            while (rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                Log.v("one",rs.getString(1)+"");
                Toast.makeText(getActivity(),"All Records Save Successfully", Toast.LENGTH_SHORT).show();
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {

            Log.v("one_error",e+"");
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updatesendDataonServer() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = createConnection();
            //   Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            String query = "UPDATE AttendanceRemote SET OUTTIME = '"+ tv_outtime_get.getText().toString() +"'," + "OUTLAT = '"+ data.get(0).getOutlat() +"'," +"OUTLOG = '"+ data.get(0).getOutlongi()+"'"+ "WHERE AR_DATE ="+ "'"+current_date+"'";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            while (rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                Log.v("oneupdate",rs.getString(1)+"\n");
                Toast.makeText(getActivity(),"All Records Save Successfully",
                        Toast.LENGTH_SHORT).show();
                ;
            }
            connection.commit();
            connection.close();
        } catch (Exception e) {

            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //........................................................................................


    public void alertdiaolog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set title
        alertDialogBuilder.setTitle("Exit Page ?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Attendance has been allready taken")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, close
                                // current activity
                                // startActivity(new Intent(CardLogin.this, MainActivity.class));
                                getActivity().finish();

                            }
                        })
                /*.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })*/;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        //  GPSTracker.BUS.register(this);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(GoogleGPSService.BROADCAST_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // mContact = (Contact)getIntent().getExtras().getSerializable(EXTRA_CONTACT);
            lat = intent.getStringExtra("LAT");
            longi = intent.getStringExtra("LOG");
            tvlatlong.setText(lat + "," + longi);
            //  Toast.makeText(getActivity(), "Lat : "+lat+","+ "Long : "+ log, Toast.LENGTH_LONG).show();
            try {
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(longi), 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder(" Your Address:");
                    //  for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("\n");
                    //  }
                    Log.v("getLocation", strReturnedAddress.toString());
                    //   String add = returnedAddress.getAddressLine(0);
                    //   String add = returnedAddress.getFeatureName();
                    String add = returnedAddress.getPremises();
                    add = add + "," + returnedAddress.getSubLocality();
                    add = add + "," + returnedAddress.getLocality();
                  //add = add + "," + returnedAddress.getSubAdminArea();
                    add = add + "," + returnedAddress.getAdminArea();
                    add = add + "," + returnedAddress.getPostalCode();
                    tvlatlong.setText(add);

                } else {
                    tvlatlong.setText("No Address returned!");
                    //  zoomEnabled = false;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // tv_first.setText("Canont get Address!");
            }

        }


    };
}
