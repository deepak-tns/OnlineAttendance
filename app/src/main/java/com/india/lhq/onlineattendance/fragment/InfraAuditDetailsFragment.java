package com.india.lhq.onlineattendance.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.india.lhq.onlineattendance.R;
import com.india.lhq.onlineattendance.Utils.SharedPreferenceUtils;
import com.india.lhq.onlineattendance.constants.AppConstants;
import com.india.lhq.onlineattendance.database.Customer;
import com.india.lhq.onlineattendance.database.DatabaseHandler;
import com.india.lhq.onlineattendance.database.Emp_Job_Details;
import com.india.lhq.onlineattendance.database.InfraDetailData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.india.lhq.onlineattendance.constants.JDBCConnection.createConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfraAuditDetailsFragment extends Fragment {

    private TextView tv_infratype1, tv_serialno1;
    private EditText  tv_modelno1, tv_servicetag1, tv_imeno1,edt_recivedate1, edt_receivefromname1, edt_remark1;
    private Spinner spi_infrastatus1,spi_receivedby1;
    private TextView tv_infratype2, tv_serialno2;
    private EditText tv_modelno2, tv_servicetag2, tv_imeno2,edt_recivedate2, edt_receivefromname2, edt_remark2;
    private Spinner spi_infrastatus2, spi_receivedby2;
    private TextView tv_infratype3, tv_serialno3;
    private EditText  tv_modelno3, tv_servicetag3, tv_imeno3,edt_recivedate3, edt_receivefromname3, edt_remark3;
    private Spinner spi_infrastatus3,spi_receivedby3;
    private TextView tv_infratype4, tv_serialno4;
    private EditText tv_modelno4, tv_servicetag4, tv_imeno4, edt_recivedate4, edt_receivefromname4, edt_remark4;
    private Spinner spi_infrastatus4,spi_receivedby4;
    private TextView tv_infratype5, tv_serialno5;
    private EditText tv_modelno5, tv_servicetag5, tv_imeno5,edt_recivedate5, edt_receivefromname5, edt_remark5;
    private Spinner spi_infrastatus5,spi_receivedby5;
    private Button btn_save;

    ArrayList<String> listinfrastatus;
    ArrayList<String> listreceivedby;
    ArrayList<String> listType1 = new ArrayList<>();
    ArrayList<String> listSerailNo = new ArrayList<>();
    private Connection connection;
    private static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DEFAULT_URL = "jdbc:oracle:thin:@203.122.13.203:1521:orcl";
    private static final String DEFAULT_USERNAME = "lqapp";
    private static final String DEFAULT_PASSWORD = "lqapp";
    private DatabaseHandler db;
    SharedPreferenceUtils sharedPreferenceUtils;
    ArrayList<String> datalistinfraType = new ArrayList<>();

    private String emp_id;
    private String current_date;
    public InfraAuditDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_infra_audit_details, container, false);
        db = new DatabaseHandler(getActivity());
        sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
        sharedPreferenceUtils.setContext(getActivity());
        current_date = android.text.format.DateFormat.format("dd-MM-yyyy h:mm:ss:aa", System.currentTimeMillis()).toString();

        setDatainView(v);
        init_view(v);
        getDataonInfraType();

        return v;
    }

    private void setDatainView(View v){


        List<Emp_Job_Details> jobDetailsList = db.getLastRowEMPJOBDETAILS();
        TextView tvempid =(TextView)v.findViewById(R.id.tv_empid);
        TextView location =(TextView)v.findViewById(R.id.tv_location);
         emp_id = sharedPreferenceUtils.getString(AppConstants.EMPID);
        tvempid.setText(emp_id);
        location.setText(jobDetailsList.get(0).getPROJECT_SUB_LOC());
    }

    private void init_view(View v) {
        listinfrastatus = new ArrayList<>();
        listinfrastatus.add("OK");
        listinfrastatus.add("Faulty");
        listinfrastatus.add("Damage");

        listreceivedby = new ArrayList<>();
        listreceivedby.add("By Hand");
        listreceivedby.add("By Courier");

        // first row...............................................................
        tv_infratype1 = v.findViewById(R.id.tv_infratype1);
        tv_serialno1 = v.findViewById(R.id.tv_serialno1);
        tv_modelno1 = v.findViewById(R.id.tv_modelno1);
        tv_servicetag1 = v.findViewById(R.id.tv_servicetag1);
        tv_imeno1 = v.findViewById(R.id.tv_imeno1);
        spi_infrastatus1 = v.findViewById(R.id.spi_infrastatus1);
        spi_receivedby1 = v.findViewById(R.id.spi_receivedby1);
        edt_recivedate1 = v.findViewById(R.id.edt_recivedate1);
        edt_receivefromname1 = v.findViewById(R.id.edt_receivefromname1);
        edt_remark1 = v.findViewById(R.id.edt_remark1);
        btn_save = v.findViewById(R.id.btn_save);

        final ArrayAdapter<String> dataAdapterinfrastatus = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listinfrastatus);
        dataAdapterinfrastatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_infrastatus1.setAdapter(dataAdapterinfrastatus);

        final ArrayAdapter<String> dataAdapterreceivedby = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listreceivedby);
        dataAdapterreceivedby.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spi_receivedby1.setAdapter(dataAdapterreceivedby);

        // Second row...............................................................
        tv_infratype2 = v.findViewById(R.id.tv_infratype2);
        tv_serialno2 = v.findViewById(R.id.tv_serialno2);
        tv_modelno2 = v.findViewById(R.id.tv_modelno2);
        tv_servicetag2 = v.findViewById(R.id.tv_servicetag2);
        tv_imeno2 = v.findViewById(R.id.tv_imeno2);
        spi_infrastatus2 = v.findViewById(R.id.spi_infrastatus2);
        spi_receivedby2 = v.findViewById(R.id.spi_receivedby2);
        edt_recivedate2 = v.findViewById(R.id.edt_recivedate2);
        edt_receivefromname2 = v.findViewById(R.id.edt_receivefromname2);
        edt_remark2 = v.findViewById(R.id.edt_remark2);

        spi_infrastatus2.setAdapter(dataAdapterinfrastatus);
        spi_receivedby2.setAdapter(dataAdapterreceivedby);

        // Third row........................................................................................
        tv_infratype3 = v.findViewById(R.id.tv_infratype3);
        tv_serialno3 = v.findViewById(R.id.tv_serialno3);
        tv_modelno3 = v.findViewById(R.id.tv_modelno3);
        tv_servicetag3 = v.findViewById(R.id.tv_servicetag3);
        tv_imeno3 = v.findViewById(R.id.tv_imeno3);
        spi_infrastatus3 = v.findViewById(R.id.spi_infrastatus3);
        spi_receivedby3 = v.findViewById(R.id.spi_receivedby3);
        edt_recivedate3 = v.findViewById(R.id.edt_recivedate3);
        edt_receivefromname3 = v.findViewById(R.id.edt_receivefromname3);
        edt_remark3 = v.findViewById(R.id.edt_remark3);

        spi_infrastatus3.setAdapter(dataAdapterinfrastatus);
        spi_receivedby3.setAdapter(dataAdapterreceivedby);

        // Fourth row........................................................................................
        tv_infratype4 = v.findViewById(R.id.tv_infratype4);
        tv_serialno4 = v.findViewById(R.id.tv_serialno4);
        tv_modelno4 = v.findViewById(R.id.tv_modelno4);
        tv_servicetag4 = v.findViewById(R.id.tv_servicetag4);
        tv_imeno4 = v.findViewById(R.id.tv_imeno4);
        spi_infrastatus4 = v.findViewById(R.id.spi_infrastatus4);
        spi_receivedby4 = v.findViewById(R.id.spi_receivedby4);
        edt_recivedate4 = v.findViewById(R.id.edt_recivedate4);
        edt_receivefromname4 = v.findViewById(R.id.edt_receivefromname4);
        edt_remark4 = v.findViewById(R.id.edt_remark4);

        spi_infrastatus4.setAdapter(dataAdapterinfrastatus);
        spi_receivedby4.setAdapter(dataAdapterreceivedby);

        // Five row........................................................................................
        tv_infratype5 = v.findViewById(R.id.tv_infratype5);
        tv_serialno5 = v.findViewById(R.id.tv_serialno5);
        tv_modelno5 = v.findViewById(R.id.tv_modelno5);
        tv_servicetag5 = v.findViewById(R.id.tv_servicetag5);
        tv_imeno5 = v.findViewById(R.id.tv_imeno5);
        spi_infrastatus5 = v.findViewById(R.id.spi_infrastatus5);
        spi_receivedby5 = v.findViewById(R.id.spi_receivedby5);
        edt_recivedate5 = v.findViewById(R.id.edt_recivedate5);
        edt_receivefromname5 = v.findViewById(R.id.edt_receivefromname5);
        edt_remark5 = v.findViewById(R.id.edt_remark5);

        spi_infrastatus5.setAdapter(dataAdapterinfrastatus);
        spi_receivedby5.setAdapter(dataAdapterreceivedby);

        tv_infratype1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // sendDataonServer();
                adddialogInfraType(tv_infratype1);
            }
        });
        tv_infratype2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogInfraType(tv_infratype2);
            }
        });
        tv_infratype3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogInfraType(tv_infratype3);
            }
        });
        tv_infratype4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogInfraType(tv_infratype4);
            }
        });
        tv_infratype5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogInfraType(tv_infratype5);
            }
        });

    //    listSerailNo.add("Other");

        tv_serialno1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogSerialNo(tv_serialno1,listSerailNo);
            }
        });
        tv_serialno2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogSerialNo(tv_serialno2,listSerailNo);
            }
        });
        tv_serialno3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogSerialNo(tv_serialno3,listSerailNo);
            }
        });
        tv_serialno4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogSerialNo(tv_serialno4,listSerailNo);
            }
        });
        tv_serialno5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sendDataonServer();
                adddialogSerialNo(tv_serialno5,listSerailNo);
            }
        });

       btn_save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               insertsendDataonServer();
           }
       });



    }

/*
  private void infraTypeTextview(){
    ArrayAdapter<String> adapter = new ArrayAdapter<String> (getActivity(),android.R.layout.select_dialog_item,listType1);
    //Getting the instance of AutoCompleteTextView
    infraTypeTextview1.setThreshold(1);//will start working from first character
    infraTypeTextview1.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
    infraTypeTextview1.setTextColor(Color.RED);
    adapter.notifyDataSetChanged();
}
*/

    private void sendDataonServer() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            connection = createConnection();
            Snackbar.make(getView(), "Connected", Snackbar.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
            String query ="select * from customers";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            ArrayList<Customer> list = new ArrayList<>();
            while (rs.next()) {

                Customer data = new Customer();
                //only one column
                data.setId(rs.getInt(1));
                data.setName(rs.getString(2));
                data.setCity(rs.getString(3));
                //you could add additional columns here..
                listType1.add(rs.getString(2));
            }
          //  infraTypeTextview();
            Snackbar.make(getView(),"All Records Save Successfully",
                    Snackbar.LENGTH_SHORT).show();
            connection.close();
        } catch (Exception e) {

            Snackbar.make(getView(), "" + e, Snackbar.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void getDataonInfraType() {

        datalistinfraType.clear();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            connection = createConnection();
            Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
            String query ="select * from LQAPP_INFRA_TYPE_MASTER";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            ArrayList<InfraDetailData> list = new ArrayList<>();
            while (rs.next()) {

                InfraDetailData data = new InfraDetailData();
                //only one column
                data.setInfraID(rs.getString(1));
                data.setInfraType(rs.getString(2));
                //you could add additional columns here..
                datalistinfraType.add(rs.getString(2));
            }
            //  infraTypeTextview();
            Toast.makeText(getActivity(),"All Records Save Successfully",
                    Toast.LENGTH_SHORT).show();
            connection.close();
        } catch (Exception e) {

            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

   // select * from LQAPP.LQAPP_INFRA_STOCK_MASTER where INFRA_TYPE = 'DT_MOBILE_HANDSET' and SERIAL_NUMBER ='354023030812287';

    private void getSerialNo( String surveyType) {

        listSerailNo.clear();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            connection = createConnection();
            Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
            String query ="select * from LQAPP.LQAPP_INFRA_STOCK_MASTER where INFRA_TYPE = '"+surveyType+"'";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            // ResultSet rs = stmt.executeQuery("insert into AttendanceRegualerty values('101','01jun19','10am','6pm','done','1')");
            ArrayList<InfraDetailData> list = new ArrayList<>();
            while (rs.next()) {

                InfraDetailData data = new InfraDetailData();
                //only one column
                data.setInfraID(rs.getString(1));
                data.setInfraType(rs.getString(2));
                //you could add additional columns here..
                listSerailNo.add(rs.getString(5));
            }
            //  infraTypeTextview();
            Toast.makeText(getActivity(),"All Records Save Successfully",
                    Toast.LENGTH_SHORT).show();
            connection.close();
        } catch (Exception e) {

            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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

            String query = "insert into Infra_Detail values( seq.nextval, '"+emp_id+"','"+tv_infratype1.getText().toString()+"','"+
                    tv_serialno1.getText().toString()+"','"+tv_modelno1.getText().toString()+"','"+tv_servicetag1.getText().toString()+"',"+"'"+
                    tv_imeno1.getText().toString() +"'"+","+ "'"+ spi_infrastatus1.getSelectedItem().toString() +"','"+ edt_recivedate1.getText().toString() +"',"+"'"+
                    spi_receivedby1.getSelectedItem().toString() + "'"+","+ "'"+ edt_receivefromname1.getText().toString() +"','"+ edt_remark1.getText().toString() +"','"+
                    current_date +"'" +")";

            String query2 = "insert into Infra_Detail values( seq.nextval, '"+emp_id+"','"+tv_infratype2.getText().toString()+"','"+
                    tv_serialno2.getText().toString()+"','"+tv_modelno2.getText().toString()+"','"+tv_servicetag2.getText().toString()+"',"+"'"+
                    tv_imeno2.getText().toString() +"'"+","+ "'"+ spi_infrastatus2.getSelectedItem().toString() +"','"+ edt_recivedate2.getText().toString() +"',"+"'"+
                    spi_receivedby2.getSelectedItem().toString() + "'"+","+ "'"+ edt_receivefromname2.getText().toString() +"','"+ edt_remark2.getText().toString() +"','"+
                    current_date +"'" +")";

            String query3 = "insert into Infra_Detail values( seq.nextval, '"+emp_id+"','"+tv_infratype3.getText().toString()+"','"+
                    tv_serialno3.getText().toString()+"','"+tv_modelno3.getText().toString()+"','"+tv_servicetag3.getText().toString()+"',"+"'"+
                    tv_imeno3.getText().toString() +"'"+","+ "'"+ spi_infrastatus3.getSelectedItem().toString() +"','"+ edt_recivedate3.getText().toString() +"',"+"'"+
                    spi_receivedby3.getSelectedItem().toString() + "'"+","+ "'"+ edt_receivefromname3.getText().toString() +"','"+ edt_remark3.getText().toString() +"','"+
                    current_date +"'" +")";

            String query4 = "insert into Infra_Detail values( seq.nextval, '"+emp_id+"','"+tv_infratype4.getText().toString()+"','"+
                    tv_serialno4.getText().toString()+"','"+tv_modelno4.getText().toString()+"','"+tv_servicetag4.getText().toString()+"',"+"'"+
                    tv_imeno4.getText().toString() +"'"+","+ "'"+ spi_infrastatus4.getSelectedItem().toString() +"','"+ edt_recivedate4.getText().toString() +"',"+"'"+
                    spi_receivedby4.getSelectedItem().toString() + "'"+","+ "'"+ edt_receivefromname4.getText().toString() +"','"+ edt_remark4.getText().toString() +"','"+
                    current_date +"'" +")";

            String query5 = "insert into Infra_Detail values( seq.nextval, '"+emp_id+"','"+tv_infratype5.getText().toString()+"','"+
                    tv_serialno5.getText().toString()+"','"+tv_modelno5.getText().toString()+"','"+tv_servicetag5.getText().toString()+"',"+"'"+
                    tv_imeno5.getText().toString() +"'"+","+ "'"+ spi_infrastatus5.getSelectedItem().toString() +"','"+ edt_recivedate5.getText().toString() +"',"+"'"+
                    spi_receivedby5.getSelectedItem().toString() + "'"+","+ "'"+ edt_receivefromname5.getText().toString() +"','"+ edt_remark5.getText().toString() +"','"+
                    current_date +"'" +")";





            //"'"+time+"'"+   ")";

            Log.v("query",query);
            ResultSet rs = stmt.executeQuery(query);
            ResultSet rs1 = stmt.executeQuery(query2);
            ResultSet rs2 = stmt.executeQuery(query3);
            ResultSet rs3 = stmt.executeQuery(query4);
            ResultSet r4 = stmt.executeQuery(query5);

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


    private void adddialogInfraType(final TextView textView) {
        final Dialog openDialog = new Dialog(getActivity());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialoglayout);


        Button save = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_save1);
        Button close = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_close1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        ListView lst_infraType =(ListView)openDialog.findViewById(R.id.lst_infraType);

       /* datalistinfraType.add("Mobile");
        datalistinfraType.add("Laptop");
        datalistinfraType.add("Laptop Adapter");
        datalistinfraType.add("Bag");
        datalistinfraType.add("Other");*/
        ArrayAdapter<String> lst_infraTypeAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,datalistinfraType);
        lst_infraType.setAdapter(lst_infraTypeAdapter);
        lst_infraTypeAdapter.notifyDataSetChanged();
        lst_infraType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(parent.getItemAtPosition(position).toString());
                getSerialNo(parent.getItemAtPosition(position).toString());

                openDialog.dismiss();
            }
        });

        /*
        AlertDialog alertDialog = openDialog.create();
        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        openDialog.getWindow().getAttributes().verticalMargin = 0.50F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        */

        WindowManager.LayoutParams wmlp = openDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        wmlp.x = 70;   //x position
        wmlp.y = 100;   //y position
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        openDialog.show();
    }

    private void adddialogSerialNo(final TextView textView, ArrayList<String> list) {
        final Dialog openDialog = new Dialog(getActivity());
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialoglayout);

        Button save = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_save1);
        Button close = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_close1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        ListView lst_infraType =(ListView)openDialog.findViewById(R.id.lst_infraType);

        ArrayAdapter<String> lst_Adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,list);
        lst_infraType.setAdapter(lst_Adapter);

        lst_infraType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(parent.getItemAtPosition(position).toString());
                openDialog.dismiss();
            }
        });

        /*
        AlertDialog alertDialog = openDialog.create();
        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        openDialog.getWindow().getAttributes().verticalMargin = 0.50F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        */

        WindowManager.LayoutParams wmlp = openDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        wmlp.x = 70;     //x position
        wmlp.y = 100;    //y position
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        openDialog.show();
    }


}
