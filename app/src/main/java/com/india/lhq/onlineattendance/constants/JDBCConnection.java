package com.india.lhq.onlineattendance.constants;

import android.util.Log;

import com.india.lhq.onlineattendance.database.DatabaseHandler;
import com.india.lhq.onlineattendance.database.Emp_Job_Details;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class JDBCConnection {

    public static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    public static final String DEFAULT_URL = "jdbc:oracle:thin:@203.122.13.203:1521:orcl";
    public static final String DEFAULT_USERNAME = "lqapp";
    public static final String DEFAULT_PASSWORD = "lqapp";

    public static Connection createConnection (String driver, String url, String
            username, String password) throws ClassNotFoundException, SQLException {

        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection createConnection () throws ClassNotFoundException, SQLException {
        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }
    public static void printDatesInMonth(int year, int month) {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MMMM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            Log.v("month days",fmt.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }





    public void getEmp_job_details(){

       /*
         DatabaseHandler db = new DatabaseHandler();
         List<Emp_Job_Details> emp_job_details = new ArrayList<>();
         emp_job_details = db.getLastRowEMPJOBDETAILS();

         for(Emp_Job_Details data : emp_job_details){

            Log.v("oneprojectheadid>>list",data.getPROJECT_HEAD_ID());

        }  */
    }

}
