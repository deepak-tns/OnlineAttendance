package com.india.lhq.onlineattendance.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.AppSingleton;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.database.AttendenceFixData;
import com.india.lhq.onlineattendance.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFixHistoryFragment extends Fragment {
    private DatabaseHandler db;
    private String empid;
    private ArrayList<AttendenceFixData> dataArrayList;
    private ListView listView;
    TaxiFormRecordHistoryAdapter adapter;

    public AttendanceFixHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_attendance_remote_history, container, false);
        db = new DatabaseHandler(getActivity());

        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstants.EMPID);

        dataArrayList = new ArrayList<>();
        listView = (ListView) v.findViewById(R.id.listview_attandence_remote_history);
        List<AttendenceFixData> recordDataList = db.getAllAttendanceFix();
        int size = recordDataList.size();
        if(size >0) {
            dataArrayList.addAll(recordDataList);
             adapter = new TaxiFormRecordHistoryAdapter(getActivity(), R.layout.attendance_approval_adapter, dataArrayList);
            listView.setAdapter(adapter);
        }

        return v;
    }




    private class TaxiFormRecordHistoryAdapter extends ArrayAdapter {
        AttendenceFixData taxiFormData;
        int deepColor = Color.parseColor("#FFFFFF");
        int deepColor2 = Color.parseColor("#DCDCDC");
        //  int deepColor3 = Color.parseColor("#B58EBF");
        private int[] colors = new int[]{deepColor, deepColor2};
        private List<AttendenceFixData> searchlist = null;
        List<AttendenceFixData> taxiForm_DataArrayList;
        private SparseBooleanArray mSelectedItemsIds;
        String  getstatus;

        public TaxiFormRecordHistoryAdapter(Context context, int resource, ArrayList<AttendenceFixData> latLongDatas) {
            super(context, resource, latLongDatas);
            this.searchlist = latLongDatas;
            this.taxiForm_DataArrayList = new ArrayList<>();
            taxiForm_DataArrayList.addAll(searchlist);
        }

        private class ViewHolder {

            TextView formno;
            TextView date;
            TextView id;
            TextView projecttype;
            TextView vihecleno;
            ImageView status;
            TextView startkm;
            TextView endkm;
            TextView delete;


        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            taxiFormData = searchlist.get(position);
           ViewHolder viewHolder = new ViewHolder();

            if (convertView == null) {

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.taxiform_record_history_adapter, parent, false);
                //int colorPos = position % colors.length;
                // convertView.setBackgroundColor(colors[colorPos]);

                viewHolder.formno = (TextView) convertView.findViewById(R.id.tv_formno_taxiadapter);
                viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date_taxiadapter);
                viewHolder.id = (TextView) convertView.findViewById(R.id.tv_id_taxiadapter);
                viewHolder.projecttype = (TextView) convertView.findViewById(R.id.tv_project_taxiadapter);
                viewHolder.vihecleno = (TextView) convertView.findViewById(R.id.tv_vechicle_taxiadapter);
                viewHolder.status = (ImageView) convertView.findViewById(R.id.iv_status_taxiadapter);
                viewHolder.startkm = (TextView) convertView.findViewById(R.id.tv_startkm_taxiadapter);
                viewHolder.endkm = (TextView) convertView.findViewById(R.id.tv_endkm_taxiadapter);
                viewHolder.delete = (TextView) convertView.findViewById(R.id.tv_delete_history);



                convertView.setTag(viewHolder);

            } else {


                viewHolder = (ViewHolder) convertView.getTag();

            }



            getstatus = taxiFormData.getFlag() + "";
            //  String fr = "<u>" + attendenceRemoteData.getFormno() + "</u>";
            //  viewHolder.formno.setText(Html.fromHtml(fr));
            viewHolder.  formno.setText(position+1 +"");
            viewHolder.date.setText(taxiFormData.getCurrentDate());
           // viewHolder.id.setText(position+"");
            viewHolder.projecttype.setText(taxiFormData.getInTime());
            viewHolder.vihecleno.setText(taxiFormData.getOutTime());
            viewHolder.startkm.setText(taxiFormData.getInlat());
            viewHolder.endkm.setText(taxiFormData.getInlongi());


            if (getstatus.equals("1")) {
                viewHolder.status.setBackgroundResource(R.drawable.success);
                //status.setText("Success");
            } else if (getstatus.equals("0")) {
                viewHolder.status.setBackgroundResource(R.drawable.upload);
                //status.setText("Pending");
            }


            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    taxiFormData = searchlist.get(position);
                    getstatus = taxiFormData.getFlag() + "";
                    boolean b = getstatus.equals("0");

                    if (b) {

                        sendAllDataServer(taxiFormData);

                     //   new TaxiFormRecordFragment.getDataAsnycTask(attendenceRemoteData).execute(AppConstraint.TAXIFORMURL);
                      //  getActivity().startService(new Intent(getActivity(), SendLatiLongiServerIntentService.class));
                    }


                }
            });

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteitemDialog(position);

                    // getActivity(). getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).commit();

                    /*
                       FragmentTransaction ft = getFragmentManager().beginTransaction();
                       ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();
                   */
                }
            });

            return convertView;
        }

        private void deleteitemDialog(final int p){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(true);
            builder.setTitle("Are you sure delete this item?");
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    dialog.dismiss();
                }


            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // How to remove the selected item?
                    // adapter.remove(adapter.getItem(p));
                    taxiFormData = searchlist.get(p);
                    //  db.deleteSingleRowTaxiformData_ByID(attendenceRemoteData.getId());
                    db.deleteAttendanceFixListData(taxiFormData.getId());

                    searchlist.remove(p);
                    AttendanceFixHistoryFragment.TaxiFormRecordHistoryAdapter.this.notifyDataSetChanged();
                }


            });

            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    private void sendAllDataServer(final AttendenceFixData ad) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, AppConstants.ATTENDANCEMASTER, JsonParameterSendServer(ad),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        Log.e("res ", response + "");
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response.toString());

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("Status");
                        if(status.equalsIgnoreCase("Success")){
                            db.updateAttandenceFixStatus(ad.getId(),ad.getOutTime(),ad.getOuttimeImage(),ad.getOutlat(),ad.getOutlongi(),ad.getIntimeRemark(),ad.getOuttimeNear(),1);

                            Snackbar.make(getView(), "Uploaded Successfully...", Snackbar.LENGTH_LONG).show();
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            db.updateAttandenceFixStatus(ad.getId(),ad.getOutTime(),ad.getOuttimeImage(),ad.getOutlat(),ad.getOutlongi(),ad.getIntimeRemark(),ad.getOuttimeNear(),0);

                        }

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(AttendanceFixHistoryFragment.this).attach(AttendanceFixHistoryFragment.this).commit();

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Errors--", error + "");
                        pDialog.dismiss();
                        Snackbar.make(getView(), "Uploaded Failed...", Snackbar.LENGTH_LONG).show();
                        db.updateAttandenceFixStatus(ad.getId(),ad.getOutTime(),ad.getOuttimeImage(),ad.getOutlat(),ad.getOutlongi(),ad.getIntimeRemark(),ad.getOuttimeNear(),0);
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

    private JSONObject JsonParameterSendServer(AttendenceFixData ad) {

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("EmpId", empid);
            jsonObject.put("InTime", ad.getInTime());
            jsonObject.put("InTimePic", ad.getIntimeImage());
            jsonObject.put("OutTime", ad.getOutTime());
            jsonObject.put("OutTimePic", ad.getOuttimeImage());
            jsonObject.put("InLat", ad.getInlat());
            jsonObject.put("InLog", ad.getInlongi());
            jsonObject.put("OutLat", ad.getOutlat());
            jsonObject.put("OutLog", ad.getOutlongi());
            jsonObject.put("LocationType", "Fix");
            jsonObject.put("RadialDistance", "0");
            jsonObject.put("Date", ad.getCurrentDate());
            //  jsonObject.put("ParameterList", jsonArrayParameter);

            Log.i("Json Param",jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
