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
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.AppSingleton;
import com.india.lhq.onlineattendance.Utils.DrawBitmapAll;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.activity.AttendanceFixHistoryActivity;
import com.india.lhq.onlineattendance.activity.AttendanceRemoteHistoryActivity;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.constants.GoogleGPSService;
import com.india.lhq.onlineattendance.database.AttendenceFixData;
import com.india.lhq.onlineattendance.database.AttendenceRemoteData;
import com.india.lhq.onlineattendance.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFixFragment extends Fragment implements LocationListener, View.OnClickListener {

    private DatabaseHandler db;
    private ImageView edt_attandence_intime,edt_attandence_outtime;
    private TextView tv_intime_get,tv_outtime_get, txt_currdate,tvlatlong;
    private TextView tv_attendance_remote_history;
    private Button btn_in_preview, btn_out_preview;
    private Button btn_save;
    private String intimeIvEncodeString="",outtime_IvEncodeString="";
    private Geocoder geocoder;
    String empid;
    String current_date;
    private String lat = "Unknown";
    private String longi= "Unknown";
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private EditText edt_remote_intimeremark,edt_remote_outtimeremark;
    private double fixlat = 28.580315;
    private double fixlong = 77.320405;

    private int radialdis = 50;
    private Button btn_attshow;
    private LinearLayout li_show_att;
    private TextView tv_distance;

    List<AttendenceFixData> data;
    public AttendanceFixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendance_fix, container, false);
        geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        if (!GoogleGPSService.isRunning) {
            getActivity().startService(new Intent(getActivity(), GoogleGPSService.class));
        }

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstants.EMPID);
        db= new DatabaseHandler(getActivity());
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        System.out.println(dateFormat.format(cal.getTime()));
        current_date = dateFormat.format(cal.getTime());
        findIds(v);
        txt_currdate.setText(current_date);
    //  getLocation();

       data = db.getLastRowAttendanceFix();
        if(data.size() >0){
            if(current_date.equalsIgnoreCase(data.get(0).getCurrentDate())){
                edt_attandence_intime.setVisibility(View.INVISIBLE);
                tv_intime_get.setText(data.get(0).getInTime());
                tv_intime_get.setVisibility(View.VISIBLE);
                btn_in_preview.setVisibility(View.VISIBLE);
                btn_out_preview.setVisibility(View.VISIBLE);

                tv_outtime_get.setText(data.get(0).getOutTime());
                tv_outtime_get.setVisibility(View.VISIBLE);
                edt_attandence_outtime.setVisibility(View.VISIBLE);
               // btn_save.setVisibility(View.GONE);
               // alertdiaolog();
            }
        }

      /*  String out =tv_outtime_get.getText().toString();
        String in =tv_intime_get.getText().toString();

        Log.v("getouttimetext",out);
        Log.v("getintimetext",in);*/

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
        btn_attshow = (Button) v.findViewById(R.id.btn_attshow);
        li_show_att = (LinearLayout) v.findViewById(R.id.li_show_att);
        tv_distance = (TextView) v.findViewById(R.id.tv_distance);

        tvlatlong = (TextView) v.findViewById(R.id.latlong);
        txt_currdate = (TextView) v.findViewById(R.id.txt_currdate);
        edt_attandence_intime = (ImageView) v.findViewById(R.id.edt_attandence_intime);
        edt_attandence_outtime = (ImageView) v.findViewById(R.id.edt_attandence_outtime);
        tv_intime_get = (TextView) v.findViewById(R.id.tv_intime_get);
        tv_outtime_get = (TextView) v.findViewById(R.id.tv_outtime_get);
        btn_in_preview = (Button) v.findViewById(R.id.btn_in_preview);
        btn_out_preview = (Button) v.findViewById(R.id.btn_out_preview);
        btn_save= (Button) v.findViewById(R.id.btn_save);
        tv_attendance_remote_history=  (TextView) v.findViewById(R.id.tv_attendance_remote_history);
        edt_remote_intimeremark =  (EditText) v.findViewById(R.id.edt_remote_intimeremark);
        edt_remote_outtimeremark =  (EditText) v.findViewById(R.id.edt_remote_outtimeremark);

        edt_attandence_intime.setOnClickListener(this);
        edt_attandence_outtime.setOnClickListener(this);
        btn_in_preview.setOnClickListener(this);
        btn_out_preview.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_attendance_remote_history.setOnClickListener(this);
        btn_attshow.setOnClickListener(this);
        li_show_att.setOnClickListener(this);


    }
    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        longi = String.valueOf(location.getLongitude());
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:");
                // for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                strReturnedAddress =  strReturnedAddress.append(returnedAddress.getAddressLine(0)).append("\n");
                //}
                if(!strReturnedAddress.equals(null)) {
                    //getAddress.setText(strReturnedAddress);
                }
                Log.v("getLocation",strReturnedAddress.toString());
                // Toast.makeText(getActivity(), strReturnedAddress.toString() , Toast.LENGTH_LONG).show();
                // tv_first.setText("Your Current Address is : "+strReturnedAddress.toString());

            }
            else{
                //  tv_first.setText("No Address returned!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // tv_first.setText("Canont get Address!");
        }


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

    private void getLocation() {

         locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Creating an empty criteria object
        Criteria criteria = new Criteria();
        // Getting the name of the provider that meets the criteria
        String provider = locationManager.getBestProvider(criteria, false);
        if (provider != null && !provider.equals("")) {
            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions,
                // int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 5000, 0, this);
            if(location!=null)
                onLocationChanged(location);
            else
                Snackbar.make(getView(), "Location can't be retrieved", Snackbar.LENGTH_SHORT).show();

        }else{
            Snackbar.make(getView(), "No Provider Found", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId() ==R.id.edt_attandence_intime){
                //Toast.makeText(getActivity(),"All Ready Image Capture",Toast.LENGTH_LONG).show();
                selectImage("intime");
        }
        if(view.getId() ==R.id.edt_attandence_outtime){
            selectImage("outtime");

        }
        if(view.getId() ==R.id.btn_in_preview){
            imagePreviewDialogIn();
        }

        if(view.getId() ==R.id.btn_out_preview){
            imagePreviewDialogOut();
        }
        if (view.getId() == R.id.btn_attshow) {
            li_show_att.setVisibility(View.VISIBLE);
        }
        if(view.getId() ==R.id.btn_save){
            if(tv_intime_get.getText().toString().equals("Intime")){
                Snackbar.make(getView(),"Please Capture Intime Image",Snackbar.LENGTH_LONG).show();
              return;
            }
            if(tv_outtime_get.getText().toString().equals("Outtime")){
                Snackbar.make(getView(),"Please Capture Outtime Image",Snackbar.LENGTH_LONG).show();
                return;
            }
            else {

                sendAllDataServer();
            }
        }
        if(view.getId() ==R.id. tv_attendance_remote_history){
             Intent intent = new Intent(getActivity(), AttendanceFixHistoryActivity.class);
             intent.putExtra("value","Fix");
             startActivity(intent);
        }

    }

    public void imagePreviewDialogIn(){
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView =(ImageView)dialog.findViewById(R.id.iv_photo_preview);
        final TextView tv_image_not_found =(TextView)dialog.findViewById(R.id.tv_image_not_found);
        data = db.getLastRowAttendanceFix();
        if(!(data.get(0).getIntimeImage()).equals("")){
            Bitmap bt = decodeBase64(data.get(0).getIntimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        }else{
            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Snackbar.make(getView(),"Please Capture Image First",Snackbar.LENGTH_LONG).show();
        }
        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }
    public void imagePreviewDialogOut(){
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_photo_previewdialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout( LinearLayout.LayoutParams.WRAP_CONTENT, 800);
        // set the custom dialog components - text, image and button
        ImageView imageView =(ImageView)dialog.findViewById(R.id.iv_photo_preview);
        final TextView tv_image_not_found =(TextView)dialog.findViewById(R.id.tv_image_not_found);
        data = db.getLastRowAttendanceFix();
        if(!(data.get(0).getOuttimeImage()).equals("")){
            Bitmap bt = decodeBase64(data.get(0).getOuttimeImage());
            imageView.setImageBitmap(bt);
            tv_image_not_found.setVisibility(View.GONE);

        }else{

            tv_image_not_found.setVisibility(View.VISIBLE);
            // tv_image_not_found  .setVisibility(View.VISIBLE);
            Snackbar.make(getView(),"Please Capture Image First",Snackbar.LENGTH_LONG).show();
        }

        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }


    private void onCaptureImageResult(Intent data, String name) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        if (name.equals("intime"))
        {
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
                    btn_in_preview.setVisibility(View.VISIBLE);
                    edt_attandence_intime.setVisibility(View.INVISIBLE);
                    db.insertAttandanceFix(new AttendenceFixData(empid, current_date, tv_intime_get.getText().toString(), tv_outtime_get.getText().toString(), intimeIvEncodeString, outtime_IvEncodeString, lat, longi, lat, longi, edt_remote_intimeremark.getText().toString(), "outtime", "near1", "near2", 0));
                    sendAllDataServerIntime();

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
                    File file = new File(destination,  current_date + "_" + current_time_str + ".jpg");
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
                btn_out_preview.setVisibility(View.VISIBLE);
                db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"near",  0);
            }
            // setImg.setImageBitmap(thumbnail);
        }
    }

    private void selectImage(String Value) {

        if (Value.equals("intime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            startActivityForResult(intent, 3);
        }
        if (Value.equals("outtime")) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
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
        List<AttendenceFixData>  dataLast = db.getLastRowAttendanceFix();
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
            jsonObject.put("LocationType", "Fix");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", current_date);
            Log.i("Json Param",jsonObject.toString());

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
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        Log.e("res outtime", response + "");
                        btn_save.setEnabled(false);
                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("Status");
                            if(status.equalsIgnoreCase("Success")){
                                db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  1);

                                Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                            }else{
                                db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);

                                Snackbar.make(getView(), "Uploaded failed...", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);

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
                        db.updateAttandenceFixStatus(db.getLastInsertIdAttendanceFix(),tv_outtime_get.getText().toString(),outtime_IvEncodeString,lat,longi,edt_remote_intimeremark.getText().toString(),"",  0);

                        edt_remote_intimeremark.getText().clear();
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

        data = db.getLastRowAttendanceFix();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", tv_intime_get.getText().toString());
            jsonObject.put("InTimePic", data.get(0).getIntimeImage());
            jsonObject.put("OutTime", "");
            jsonObject.put("OutTimePic", "");
            jsonObject.put("InLat", data.get(0).getInlat());
            jsonObject.put("InLog",data.get(0).getInlongi());
            jsonObject.put("OutLat", "");
            jsonObject.put("OutLog", "");
            jsonObject.put("LocationType", "Fix");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", current_date);

            Log.i("Json Param",jsonObject.toString());

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
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        pDialog.dismiss();
                        Log.e("res intime", response + "");

                        try {
                            JSONArray jsonArray = new JSONArray(response.toString());
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String status = jsonObject.getString("Status");
                            if(status.equalsIgnoreCase("Success")){
                                Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                             //   edt_remote_intimeremark.getText().clear();
                            }else{
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
            }  */
        };
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsObjRequest, null);

    }

    public  void alertdiaolog(){
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
            double distenceCal = distenc2(fixlat, fixlong, Double.parseDouble(lat), Double.parseDouble(longi));

            int dis = (int) round(distenceCal * 1000, 0);
            tv_distance.setText("Distance :" + dis + " Meters");

            if (dis <= radialdis) {
                btn_attshow.setVisibility(View.VISIBLE);
            } else {
                btn_attshow.setVisibility(View.GONE);
                li_show_att.setVisibility(View.GONE);
            }

            Log.v("getDistence", String.valueOf(dis));
        }


    };

    private double distenc2(double a, double b, double c, double d) {
        double distance;
        Location locationA = new Location("point A");
        locationA.setLatitude(a);
        locationA.setLongitude(b);

        Location locationB = new Location("point B");
        locationB.setLatitude(c);
        locationB.setLongitude(d);

        // distance = locationA.distanceTo(locationB);   // in meters
        distance = round(locationA.distanceTo(locationB) / 1000, 6);
        Log.v("Distance", distance + "");
        return distance;

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
