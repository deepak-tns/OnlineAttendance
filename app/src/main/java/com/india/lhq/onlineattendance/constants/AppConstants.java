package com.india.lhq.onlineattendance.constants;

import java.sql.Connection;

/**
 * Created by Mayank on 27/04/2016.
 */
public interface AppConstants {
    public static final int ALERT_TYPE_NO_NETWORK = 0x01;
    public static final int ALERT_TYPE_LOGOUT = 0x02;
    public static final int ALERT_TYPE_DELETE_USER = 0x03;
    public static final String VERIFYLOGINURL = "http://13.126.69.214/lq/api/Login/Getlogin";
    public static final String ATTENDANCEMASTER = "http://13.126.69.214/lq/api/Login/AttendenceMaster";



     String APPNAME = "LQFarMiddleEast" ;
     String EMPID = "EMPID" ;

}
