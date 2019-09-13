package com.india.lhq.onlineattendance.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.database.AttendanceApprovalData;
import com.india.lhq.onlineattendance.database.AttendenceRemoteData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.india.lhq.onlineattendance.constants.JDBCConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.

 */
public class AttendanceApprovalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "AttendanceApprovalFrgament";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listview_attandence_approval;
    AttendanceAdapter attendanceAdapter;
    ArrayList<AttendanceApprovalData> attendancelist ;

    private Spinner spi_select_year, spiMonth;
    private ArrayAdapter adapter, adaptermonth;
    ArrayList<String> selectyear_list = new ArrayList<>();
    ArrayList<String> selectMonthList = new ArrayList<>();

    private String selectYear,selectMonth;
    private RadioGroup radioGroup;
    private RadioButton rb_cordinator;
    private RadioButton rb_projectmanager;
    private RadioButton rb_projecthead;

    private Connection connection;
    private String empid;




    public AttendanceApprovalFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AttendanceApprovalFragment newInstance(String param1, String param2) {
        AttendanceApprovalFragment fragment = new AttendanceApprovalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance_approval, container, false);
        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        empid = sharedPreferenceUtils.getString(AppConstants.EMPID);
         findIds(v);
        return v;
    }

private void findIds(View v){
    addApaterMonthYear(v);

    listview_attandence_approval =(ListView)v.findViewById(R.id.listview_attandence_approval);
    radioGroup = (RadioGroup)v. findViewById(R.id.radiogroup);
    rb_cordinator = (RadioButton) v. findViewById(R.id.rb_cordinator);
    rb_projectmanager = (RadioButton) v. findViewById(R.id.rb_project_manager);
    rb_projecthead = (RadioButton) v. findViewById(R.id.rb_project_head);


    int selectedId1 = radioGroup.getCheckedRadioButtonId();
   // yes = (RadioButton) getView().findViewById(selectedId1);

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @SuppressLint("LongLogTag")
        public void onCheckedChanged(RadioGroup arg0, int id) {
            switch (id) {
                case R.id.rb_cordinator:
                    String query ="Select * from AttendanceRemote where Reporting_Cordinator_id ="+"'"+empid+"' and monthnmame ="+"'"+selectMonth+"'"+ " and yearname ="+"'"+selectYear+"'" ;
                    Log.v("query", query);
                    selectDataonServer(query);
                    Log.v(TAG, "Cordinator");

                    attendanceAdapter = new AttendanceAdapter(getActivity(),R.layout.attendance_approval_adapter,attendancelist);
                    listview_attandence_approval.setAdapter(attendanceAdapter);
                    attendanceAdapter.notifyDataSetChanged();

                    break;
                case R.id.rb_project_manager:
                    String query2 ="Select * from AttendanceRemote where Reporting_manager_id ="+"'"+empid+"' and monthnmame ="+"'"+selectMonth+"'"+ " and yearname ="+"'"+selectYear+"'" ;
                    Log.v("query", query2);
                    Log.v(TAG, "Project Manager");
                    selectDataonServer(query2);
                    attendanceAdapter = new AttendanceAdapter(getActivity(),R.layout.attendance_approval_adapter,attendancelist);
                    listview_attandence_approval.setAdapter(attendanceAdapter);
                    attendanceAdapter.notifyDataSetChanged();
                    break;
                case R.id.rb_project_head:
                    String query3 ="Select * from AttendanceRemote where project_head_id ="+"'"+empid+"' and monthnmame ="+"'"+selectMonth+"'"+ " and yearname ="+"'"+selectYear+"'" ;
                    Log.v("query", query3);
                    Log.v(TAG, "Project Head");
                    selectDataonServer(query3);
                    attendanceAdapter = new AttendanceAdapter(getActivity(),R.layout.attendance_approval_adapter,attendancelist);
                    listview_attandence_approval.setAdapter(attendanceAdapter);
                    attendanceAdapter.notifyDataSetChanged();
                    break;
            /*    default:
                    Log.v(TAG, "Huh?");
                    break;*/
            }
        }
    });
}

@SuppressLint("LongLogTag")
private void addApaterMonthYear(View v){
    selectMonthList.add("1");
    selectMonthList.add("2");
    selectMonthList.add("3");
    selectMonthList.add("4");
    selectMonthList.add("5");
    selectMonthList.add("6");
    selectMonthList.add("7");
    selectMonthList.add("8");
    selectMonthList.add("9");
    selectMonthList.add("10");
    selectMonthList.add("11");
    selectMonthList.add("12");

    selectyear_list.add("2018");
    selectyear_list.add("2019");
    selectyear_list.add("2020");

    Calendar instance = Calendar.getInstance();
    int thisMonth = instance.get(Calendar.MONTH);
    Log.d(TAG, "@ thisMonth : " + (thisMonth+1));
    thisMonth =thisMonth+1;
    int year = instance.get(Calendar.YEAR);
    Log.d(TAG, "@ thisyear : " + year);

    spi_select_year = (Spinner) v.findViewById(R.id.spi_leavrtransactrion_year);
    spiMonth = (Spinner) v.findViewById(R.id.spi_leavrtransactrion_month);

    adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectyear_list);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spi_select_year.setAdapter(adapter);
    spi_select_year.setSelection(adapter.getPosition(year+""));
  //  spi_select_year.setSelection(year);

    spi_select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectYear = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
    adaptermonth = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, selectMonthList);
    adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spiMonth.setAdapter(adaptermonth);
    spiMonth.setSelection(adaptermonth.getPosition(thisMonth+""));
  //  spiMonth.setSelection(thisMonth);
    spiMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectMonth = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
}

//................................................Attendance Appoval..............................................
private class AttendanceAdapter extends ArrayAdapter {
    AttendanceApprovalData attendenceRemoteData;
    int deepColor = Color.parseColor("#FFFFFF");
    int deepColor2 = Color.parseColor("#DCDCDC");
    //  int deepColor3 = Color.parseColor("#B58EBF");
    private int[] colors = new int[]{deepColor, deepColor2};
    private List<AttendanceApprovalData> searchlist ;


    public AttendanceAdapter(Context context, int resource, ArrayList<AttendanceApprovalData> latLongDatas) {
        super(context, resource, latLongDatas);
        this.searchlist = latLongDatas;

    }

    private class ViewHolder {

        TextView id;
        TextView empid;
        TextView date;
        TextView cordinatorapprove;
        TextView managerapprove;
        TextView headapprove;
        TextView startkm;
        TextView endkm;
        TextView delete;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        attendenceRemoteData = searchlist.get(position);
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.attendance_approval_adapter, parent, false);
            //int colorPos = position % colors.length;
            // convertView.setBackgroundColor(colors[colorPos]);

            viewHolder.id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.empid = (TextView) convertView.findViewById(R.id.emp_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.cordinatorapprove = (TextView) convertView.findViewById(R.id.coordinateapprove);
            viewHolder.managerapprove = (TextView) convertView.findViewById(R.id.managerapprove);
            viewHolder.headapprove = (TextView) convertView.findViewById(R.id.headeapprove);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        //  String fr = "<u>" + attendenceRemoteData.getFormno() + "</u>";
        //  viewHolder.formno.setText(Html.fromHtml(fr));
        viewHolder.id.setText(position+1 +"");
        viewHolder.empid.setText(attendenceRemoteData.getEMPID());
        // viewHolder.id.setText(position+"");
        viewHolder.date.setText(attendenceRemoteData.getAR_DATE());
        viewHolder.cordinatorapprove.setText(attendenceRemoteData.getAPPROVED_CORDINATOR());
        viewHolder.managerapprove.setText(attendenceRemoteData.getAPPROVED_MANAGER());
        viewHolder.headapprove.setText(attendenceRemoteData.getAPPROVED_HEAD());

        if(attendenceRemoteData.getREPORTING_CORDINATOR_ID().equalsIgnoreCase(empid)){
            viewHolder.cordinatorapprove.setTextColor(getResources().getColor(R.color.red));
            viewHolder.cordinatorapprove.setTypeface(viewHolder.cordinatorapprove.getTypeface(), Typeface.BOLD);
            viewHolder.cordinatorapprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attendenceRemoteData = searchlist.get(position);
                    attendanceApprovalDialog( "Approved_cordinator",attendenceRemoteData.getId()+"",searchlist,attendenceRemoteData,position);
                    Toast.makeText(getActivity(),attendenceRemoteData.getREPORTING_CORDINATOR_ID(),Toast.LENGTH_LONG).show();

                 //   attendenceRemoteData.setAPPROVED_CORDINATOR("Done");
                 //   searchlist.set(position,attendenceRemoteData );//Change your dataset
                 //   notifyDataSetChanged();

                }
            });

        }
        if(attendenceRemoteData.getREPORTING_MANAGER_ID().equalsIgnoreCase(empid)){
            viewHolder.managerapprove.setTextColor(getResources().getColor(R.color.red));
            viewHolder.managerapprove.setTypeface(viewHolder.managerapprove.getTypeface(), Typeface.BOLD);
            viewHolder.managerapprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attendenceRemoteData = searchlist.get(position);
                    attendanceApprovalDialog("Approved_Manager", attendenceRemoteData.getId()+"",searchlist,attendenceRemoteData,position);
                    Toast.makeText(getActivity(),attendenceRemoteData.getREPORTING_MANAGER_ID(),Toast.LENGTH_LONG).show();


                }
            });
        }
        if(attendenceRemoteData.getREPORTING_HEAD_ID().equalsIgnoreCase(empid)){
            viewHolder.headapprove.setTextColor(getResources().getColor(R.color.red));
            viewHolder.headapprove.setTypeface(viewHolder.headapprove.getTypeface(), Typeface.BOLD);
            viewHolder.headapprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attendenceRemoteData = searchlist.get(position);
                    attendanceApprovalDialog("Approved_Head ",attendenceRemoteData.getId()+"",searchlist,attendenceRemoteData,position);
                    Toast.makeText(getActivity(),attendenceRemoteData.getREPORTING_HEAD_ID(),Toast.LENGTH_LONG).show();

                }
            });
        }


    /*
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    deleteitemDialog(position);
                // getActivity(). getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_home_frag, new TaxiFormRecordFragment()).commit();
                    *//*
                       FragmentTransaction ft = getFragmentManager().beginTransaction();
                       ft.detach(TaxiFormRecordFragment.this).attach(TaxiFormRecordFragment.this).commit();
                   *//*
            }
        });*/

        return convertView;
    }

    public void attendanceApprovalDialog(final String approvedby, final String updatevalue, final List<AttendanceApprovalData>list, final AttendanceApprovalData data, final int pos) {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.attendance_approval_dialog);
        dialog.setCancelable(true);
        //  dialog.setTitle("Title...");
        dialog.getWindow().setLayout(800, 800);

        radioGroup = (RadioGroup)dialog. findViewById(R.id.radiogroup);
        final RadioButton rb_pending = (RadioButton) dialog. findViewById(R.id.rb_pending);
        final RadioButton rb_approved =  (RadioButton) dialog. findViewById(R.id.rb_approved);
        final Button btn_ok =  (Button) dialog. findViewById(R.id.ok);

        int selectedId1 = radioGroup.getCheckedRadioButtonId();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("LongLogTag")
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.rb_pending:

                        String  query = "UPDATE AttendanceRemote SET "+approvedby +" = '"+ rb_pending.getText().toString() + "'"+ "WHERE id ="+ "'"+updatevalue+"'";
                        Log.v("updatequery", query);
                        updatesendDataonServer(query);

                        if(approvedby.equalsIgnoreCase("Approved_cordinator")){
                            data.setAPPROVED_CORDINATOR( rb_pending.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }
                        if(approvedby.equalsIgnoreCase("Approved_Manager")){
                            data.setAPPROVED_MANAGER( rb_pending.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }
                        if(approvedby.equalsIgnoreCase("Approved_head")){
                            data.setAPPROVED_HEAD( rb_pending.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }
                        break;

                    case R.id.rb_approved:
                        String  query2 = "UPDATE AttendanceRemote SET "+approvedby+" = '"+ rb_approved.getText().toString() + "'"+ "WHERE id ="+ "'"+updatevalue+"'";
                        Log.v("updatequery2", query2);
                        updatesendDataonServer(query2);

                        if(approvedby.equalsIgnoreCase("Approved_cordinator")){
                            data.setAPPROVED_CORDINATOR( rb_approved.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }

                        if(approvedby.equalsIgnoreCase("Approved_Manager")){
                            data.setAPPROVED_MANAGER( rb_approved.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }
                        if(approvedby.equalsIgnoreCase("Approved_Head")){
                            data.setAPPROVED_HEAD( rb_approved.getText().toString());
                            list.set(pos,data);
                            AttendanceAdapter.this.notifyDataSetChanged();
                        }

                        break;

                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*
                attendanceAdapter.clear();
                attendanceAdapter.addAll(searchlist);
                AttendanceAdapter.this.notifyDataSetChanged();
             */

               // attendanceAdapter.notifyDataSetChanged();
               // notifyDataSetChanged();
               // AttendanceAdapter.this.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
        // Window window = dialog.getWindow();
        //  window.setLayout(300, 300);
    }
    private void updatesendDataonServer(String query) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = createConnection();

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            //  query = "UPDATE AttendanceRemote SET OUTTIME = '"+ tv_outtime_get.getText().toString() +"'," + "OUTLAT = '"+ data.get(0).getOutlat() +"'," +"OUTLOG = '"+ data.get(0).getOutlongi()+"'"+ "WHERE AR_DATE ="+ "'"+current_date+"'";

            Log.v("updatequery",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            while (rs.next()) {
                stringBuffer.append( rs.getString(1)+"\n");
                Log.v("oneupdate",rs.getString(1)+"\n");
                Toast.makeText(getActivity(),"Update Successfully",
                        Toast.LENGTH_SHORT).show();
            }

            connection.commit();
            connection.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

/*
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
                attendenceRemoteData = searchlist.get(p);
                //  db.deleteSingleRowTaxiformData_ByID(attendenceRemoteData.getId());
                db.deleteAttendanceFixListData(attendenceRemoteData.getId());

                searchlist.remove(p);
                AttendanceFixHistoryFragment.TaxiFormRecordHistoryAdapter.this.notifyDataSetChanged();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }*/
}

    //.........................................oracle...........................................
    private void selectDataonServer(String query) {
      ProgressDialog  pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            connection = createConnection();
            //   Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            attendancelist = new ArrayList<>();
            while (rs.next()) {

                AttendanceApprovalData approvalData = new AttendanceApprovalData();
                stringBuffer.append( rs.getString(1)+"\n");
                approvalData.setId(Integer.parseInt(rs.getString(1)+""));
                approvalData.setEMPID(rs.getString(2));
                approvalData.setAR_DATE(rs.getString(13));
             /*   private String INTIME;
                private String INTIMEPIC;
                private String OUTTIME;
                private String OUTTIMEPIC;
                private String INLAT;
                private String INLOG;
                private String OUTLAT;
                private String OUTLOG;
                private String LOCATIONTYPE;
                private String RADIALDISTANCE;
                private String AR_DATE;
                private String JOB_STATUS;
                private String CLIENT_ID;
                private String PROJECT_ID;
                private String LOCATION_ID;
                private String DESIG_ID;
                private String DEPT_ID;
                private String REPORTING_CORDINATOR_ID;
                private String REPORTING_MANAGER_ID;
                private String REPORTING_HEAD_ID;*/
                approvalData.setREPORTING_CORDINATOR_ID(rs.getString(20));
                approvalData.setREPORTING_MANAGER_ID(rs.getString(21));
                approvalData.setREPORTING_HEAD_ID(rs.getString(22));
                approvalData.setAPPROVED_CORDINATOR(rs.getString(23));
                approvalData.setAPPROVED_MANAGER(rs.getString(24));
                approvalData.setAPPROVED_HEAD(rs.getString(25));
              /*  private String APPROVED_CORDINATOR;
                private String APPROVED_MANAGER;
                private String APPROVED_HEAD;
                private String MONTH_NAME;
                private String YEAR_NAME;
                private String STATUS;;*/
                Log.v("one",rs.getString(1)+"");
                Toast.makeText(getActivity(),"All Records Show Successfully", Toast.LENGTH_SHORT).show();

                attendancelist.add(approvalData);
           //   attendanceAdapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
            pDialog.dismiss();

            connection.close();
        } catch (Exception e) {

            pDialog.dismiss();
            Log.v("one_error",e+"");
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    //........................................................................................


}
