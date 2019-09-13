package com.india.lhq.onlineattendance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TNS on 12/22/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 13;

    // Database Name
    public static final String DATABASE_NAME = "my";

    private static final String TABLE_ATTENDANCE_REMOTE = "attendanceRemote";
    private static final String TABLE_ATTENDANCE_FIX = "attendanceFix";
    private static final String TABLE_ATTENDANCE_REGULERTY = "attendanceRegulerty";
    private static final String TABLE_EMP_JOB_DETAILS = "JobDetails";

    //Table Attendance Remote columns name
    private static final String ATT_REMOTE_INCRI = "id";
    private static final String ATT_REMOTE_EMPID = "empId";
    private static final String ATT_REMOTE_CURRENTDATE= "Date";
    private static final String ATT_REMOTE_INTIME = "inTime";
    private static final String ATT_REMOTE_OUTTIME = "outTime";
    private static final String ATT_REMOTE_INTIME_PICS = "inTimePics";
    private static final String ATT_REMOTE_OUTTIME_PICS = "outTimePics";
    private static final String ATT_REMOTE_INLAT = "inlat";
    private static final String ATT_REMOTE_INLONG = "inlongi";
    private static final String ATT_REMOTE_OUTLAT = "outlat";
    private static final String ATT_REMOTE_OUTLONG = "outlongi";
    private static final String ATT_REMOTE_INTIMEREMARK = "IntimeRemark";
    private static final String ATT_REMOTE_OUTTIMEREMARK = "OuttimeRemark";
    private static final String ATT_REMOTE_NEARESTINTIME = "Intimenear";
    private static final String ATT_REMOTE_NEARESTOUTTIME = "Outtimenear";
    private static final String ATT_REMOTE_FLAG = "flag";


    //Table Attendance Fix columns name
    private static final String ATT_FIX_INCRI = "id";
    private static final String ATT_FIX_EMPID = "empId";
    private static final String ATT_FIX_CURRENTDATE= "Date";
    private static final String ATT_FIX_INTIME = "inTime";
    private static final String ATT_FIX_OUTTIME = "outTime";
    private static final String ATT_FIX_INTIME_PICS = "inTimePics";
    private static final String ATT_FIX_OUTTIME_PICS = "outTimePics";
    private static final String ATT_FIX_INLAT = "inlat";
    private static final String ATT_FIX_INLONG = "inlongi";
    private static final String ATT_FIX_OUTLAT = "outlat";
    private static final String ATT_FIX_OUTLONG = "outlongi";
    private static final String ATT_FIX_INTIMEREMARK = "IntimeRemark";
    private static final String ATT_FIX_OUTTIMEREMARK = "OuttimeRemark";
    private static final String ATT_FIX_NEARESTINTIME = "Intimenear";
    private static final String ATT_FIX_NEARESTOUTTIME = "Outtimenear";
    private static final String ATT_FIX_FLAG = "flag";


    //Table Attendance REG columns name
    private static final String ATT_REG_INCRI = "id";
    private static final String ATT_REG_EMPID = "empId";
    private static final String ATT_REG_CURRENTDATE= "date";
    private static final String ATT_REG_INTIME = "inTime";
    private static final String ATT_REG_OUTTIME = "outTime";
    private static final String ATT_REG_REMARK = "remark";
    private static final String ATT_REG_FLAG = "flag";
    private static final String ATT_REG_SYSTEMTIME = "systemtime";

    //Table EMP_JOB_Detail columns name
    private String IN_ID ="Id";
    private String EMP_ID= "EMPID";
    private String JOB_STATUS ="JOB_STATUS";
    private String PROJECT_SUB_LOC ="PROJECT_SUB_LOC";
    private String RESOURCE_TYPE ="RESOURCE_TYPE";
    private String CLIENT_ID ="CLIENT_ID";
    private String PROJECT_ID ="PROJECT_ID";
    private String LOCATION_ID ="LOCATION_ID";
    private String DESIG_ID ="DESIG_ID";
    private String DEPT_ID ="DEPT_ID";
    private String REPORTING_MANGER_ID ="REPORTING_MANGER_ID";
    private String PROJECT_HEAD_ID = "PROJECT_HEAD_ID";
    private String REPORTING_CORDINATE_ID ="REPORTING_CORDINATE_ID";



    public DatabaseHandler(Context context) {
       // super(context, "/mnt/sdcard/my.db", null, DATABASE_VERSION);
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        Log.v(TAG, "Databaser object created");
    }

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_ATTENDANCEREMOTE = "create table "
                + TABLE_ATTENDANCE_REMOTE + " (" + ATT_REMOTE_INCRI + " integer primary key autoincrement," + ATT_REMOTE_EMPID + " text,"+ ATT_REMOTE_CURRENTDATE+ " text," + ATT_REMOTE_INTIME + " text," + ATT_REMOTE_OUTTIME + " text,"
                + ATT_REMOTE_INTIME_PICS + " text," + ATT_REMOTE_OUTTIME_PICS + " text," + ATT_REMOTE_INLAT + " text," + ATT_REMOTE_INLONG + " text," + ATT_REMOTE_OUTLAT + " text," + ATT_REMOTE_OUTLONG + " text," + ATT_REMOTE_INTIMEREMARK + " text," + ATT_REMOTE_OUTTIMEREMARK + " text," +ATT_REMOTE_NEARESTINTIME + " text,"+ ATT_REMOTE_NEARESTOUTTIME + " text,"+  ATT_REMOTE_FLAG + " integer" + ");";


        String CREATE_TABLE_ATTENDANCEFIX = "create table "
                + TABLE_ATTENDANCE_FIX + " (" + ATT_FIX_INCRI + " integer primary key autoincrement," + ATT_FIX_EMPID + " text,"+ ATT_FIX_CURRENTDATE+ " text," + ATT_FIX_INTIME + " text," + ATT_FIX_OUTTIME + " text,"
                + ATT_FIX_INTIME_PICS + " text," + ATT_FIX_OUTTIME_PICS + " text," + ATT_FIX_INLAT + " text," + ATT_FIX_INLONG + " text," + ATT_FIX_OUTLAT + " text," + ATT_FIX_OUTLONG + " text,"+ ATT_FIX_INTIMEREMARK + " text," + ATT_FIX_OUTTIMEREMARK + " text," + ATT_FIX_NEARESTINTIME + " text,"+ ATT_FIX_NEARESTOUTTIME + " text,"  +  ATT_FIX_FLAG + " integer" + ");";

        String CREATE_TABLE_ATTENDANCE_REGULERTY = "create table "
                + TABLE_ATTENDANCE_REGULERTY + " (" + ATT_REG_INCRI + " integer primary key autoincrement," + ATT_REG_EMPID + " text,"+ ATT_REG_CURRENTDATE+ " text," + ATT_REG_INTIME + " text," + ATT_REG_OUTTIME + " text,"
                + ATT_REG_REMARK + " text,"  +   ATT_REG_SYSTEMTIME + " text,"  +  ATT_REG_FLAG + " integer" + ");";

        String CREATE_TABLE_EMP_JOB_DETAILS = "create table "
                + TABLE_EMP_JOB_DETAILS + " (" + IN_ID + " integer primary key autoincrement," + EMP_ID + " text,"+ JOB_STATUS+ " text," + PROJECT_SUB_LOC + " text,"
                + RESOURCE_TYPE + " text," + CLIENT_ID + " text,"  +   PROJECT_ID + " text,"  +   LOCATION_ID + " text," +   DESIG_ID + " text,"  +   DEPT_ID + " text,"
                +   REPORTING_MANGER_ID + " text,"  +   PROJECT_HEAD_ID + " text,"  +   REPORTING_CORDINATE_ID + " text"   + ");";


        db.execSQL(CREATE_TABLE_ATTENDANCEREMOTE);
        db.execSQL(CREATE_TABLE_ATTENDANCEFIX);
        db.execSQL(CREATE_TABLE_ATTENDANCE_REGULERTY);
        db.execSQL(CREATE_TABLE_EMP_JOB_DETAILS);



        Log.v(TAG, "Database table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_REMOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_FIX);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_REGULERTY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMP_JOB_DETAILS);

        // Create tables again
        onCreate(db);
    }

    //......................  Attendance Remote.............................
    public void insertAttandanceRemote(AttendenceRemoteData attendenceRemoteData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ATT_REMOTE_EMPID, attendenceRemoteData.getEmpId());
        values.put(ATT_REMOTE_CURRENTDATE, attendenceRemoteData.getCurrentDate());
        values.put(ATT_REMOTE_INTIME, attendenceRemoteData.getInTime());
        values.put(ATT_REMOTE_OUTTIME, attendenceRemoteData.getOutTime());
        values.put(ATT_REMOTE_INTIME_PICS, attendenceRemoteData.getIntimeImage());
        values.put(ATT_REMOTE_OUTTIME_PICS, attendenceRemoteData.getOuttimeImage());
        values.put(ATT_REMOTE_INLAT, attendenceRemoteData.getInlat());
        values.put(ATT_REMOTE_INLONG, attendenceRemoteData.getInlongi());
        values.put(ATT_REMOTE_OUTLAT, attendenceRemoteData.getOutlat());
        values.put(ATT_REMOTE_OUTLONG, attendenceRemoteData.getOutlongi());
        values.put(ATT_REMOTE_INTIMEREMARK, attendenceRemoteData.getIntimeRemark());
        values.put(ATT_REMOTE_OUTTIMEREMARK, attendenceRemoteData.getOuttimeRemark());
        values.put(ATT_REMOTE_NEARESTINTIME, attendenceRemoteData.getIntimetimeNear());
        values.put(ATT_REMOTE_NEARESTOUTTIME, attendenceRemoteData.getOuttimeNear());
        values.put(ATT_REMOTE_FLAG, attendenceRemoteData.getFlag());

        // Inserting Row
        db.insert(TABLE_ATTENDANCE_REMOTE, null, values);
        Log.v(TAG, "Databaser insert Attendance Remote table");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public List<AttendenceRemoteData> getAllAttendanceRemote() {

        ArrayList<AttendenceRemoteData> list = new ArrayList<AttendenceRemoteData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceRemoteData data = new AttendenceRemoteData();
                        //only one column
                        data.setId(cursor.getInt(0));  //REMOTE ATT_REMOTE_INCRI

                        data.setEmpId(cursor.getString(1));// ATT_REMOTE_EMPID
                        data.setCurrentDate(cursor.getString(2));// ATT_REMOTE_CURRENTDATE
                        data.setInTime(cursor.getString(3));//ATT_REMOTE_INTIME
                        data.setOutTime(cursor.getString(4));//ATT_REMOTE_OUTTIME
                        data.setIntimeImage(cursor.getString(5));//ATT_REMOTE_INTIME_PICS
                        data.setOuttimeImage(cursor.getString(6));//ATT_REMOTE_OUTTIME_PICS
                        data.setInlat(cursor.getString(7));//ATT_REMOTE_LAT
                        data.setInlongi(cursor.getString(8));//ATT_REMOTE_LONG
                        data.setOutlat(cursor.getString(9));//ATT_REMOTE_LAT
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));//ATT_REMOTE_FLAG
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<AttendenceRemoteData> getLastRowAttendanceRemote() {
        ArrayList<AttendenceRemoteData> list = new ArrayList<AttendenceRemoteData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        //String selectQuery = "SELECT  * FROM " + TABLE_LATLONG +" ORDER BY "+KEY_LATLONG_INCRIID+" DESC LIMIT 1;";

        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE +  " ORDER BY " + ATT_REMOTE_INCRI + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceRemoteData data = new AttendenceRemoteData();
                        //only one column
                        data.setId(cursor.getInt(0));  //REMOTE ATT_REMOTE_INCRI

                        data.setEmpId(cursor.getString(1));// ATT_REMOTE_EMPID
                        data.setCurrentDate(cursor.getString(2));// ATT_REMOTE_CURRENTDATE
                        data.setInTime(cursor.getString(3));//ATT_REMOTE_INTIME
                        data.setOutTime(cursor.getString(4));//ATT_REMOTE_OUTTIME
                        data.setIntimeImage(cursor.getString(5));//ATT_REMOTE_INTIME_PICS
                        data.setOuttimeImage(cursor.getString(6));//ATT_REMOTE_OUTTIME_PICS
                        data.setInlat(cursor.getString(7));//ATT_REMOTE_LAT
                        data.setInlongi(cursor.getString(8));//ATT_REMOTE_LONG
                        data.setOutlat(cursor.getString(9));//ATT_REMOTE_LAT
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));

                        data.setFlag(cursor.getInt(15));//ATT_REMOTE_FLAG
                        //you could add additional columns here..

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;

    }

    public int getLastInsertIdAttendanceRemote() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ATTENDANCE_REMOTE, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteAttendanceRemoteListData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCE_REMOTE + " WHERE " + ATT_REMOTE_INCRI + "='" + id + "'");
        db.close();
    }

//new AttendenceRemoteData(empid,current_date,tv_intime_get.getText().toString(),tv_outtime_get.getText().toString(),intimeIvEncodeString,outtime_IvEncodeString,getIntimeLat(),getIntimeLog(),lat,longi,edt_remote_intimeremark.getText().toString(),"",1));

    public boolean updateAttandenceRemoteStatus(int incriid,String outtime,String outtimePic,String outtimelat,String outtimelong, String intimeremark,String outimenear, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(ATT_REMOTE_OUTTIME, outtime);
        args.put(ATT_REMOTE_OUTTIME_PICS, outtimePic);
        args.put(ATT_REMOTE_OUTLAT,outtimelat);
        args.put(ATT_REMOTE_OUTLONG, outtimelong);
        args.put(ATT_REMOTE_INTIMEREMARK, intimeremark);
        args.put(ATT_REMOTE_OUTTIMEREMARK,intimeremark);
        args.put(ATT_REMOTE_NEARESTOUTTIME,outimenear);
        args.put(ATT_REMOTE_FLAG, flag);
        int i = db.update(TABLE_ATTENDANCE_REMOTE, args, ATT_REMOTE_INCRI + "=" + incriid, null);
        return i > 0;
    }

//......................  Attendance Remote.............................

//.....................AttendanceFIx................
public void insertAttandanceFix(AttendenceFixData attendenceFixData) {
    SQLiteDatabase db = this.getWritableDatabase();
try{
    ContentValues values = new ContentValues();
    values.put(ATT_FIX_EMPID, attendenceFixData.getEmpId());
    values.put(ATT_FIX_CURRENTDATE, attendenceFixData.getCurrentDate());
    values.put(ATT_FIX_INTIME, attendenceFixData.getInTime());
    values.put(ATT_FIX_OUTTIME, attendenceFixData.getOutTime());
    values.put(ATT_FIX_INTIME_PICS, attendenceFixData.getIntimeImage());
    values.put(ATT_FIX_OUTTIME_PICS, attendenceFixData.getOuttimeImage());
    values.put(ATT_FIX_INLAT, attendenceFixData.getInlat());
    values.put(ATT_FIX_INLONG, attendenceFixData.getInlongi());
    values.put(ATT_FIX_OUTLAT, attendenceFixData.getOutlat());
    values.put(ATT_FIX_OUTLONG, attendenceFixData.getOutlongi());
    values.put(ATT_FIX_INTIMEREMARK, attendenceFixData.getIntimeRemark());
    values.put(ATT_FIX_OUTTIMEREMARK, attendenceFixData.getOuttimeRemark());
    values.put(ATT_FIX_NEARESTINTIME, attendenceFixData.getIntimetimeNear());
    values.put(ATT_FIX_NEARESTOUTTIME, attendenceFixData.getOuttimeNear());
    values.put(ATT_FIX_FLAG, attendenceFixData.getFlag());

    // Inserting Row
    db.insert(TABLE_ATTENDANCE_FIX, null, values);
    Log.v(TAG, "Databaser insert Attendance Fix table");
    //2nd argument is String containing nullColumnHack
    db.close(); // Closing database connection
} finally {
        try {
            db.close();

        } catch (Exception ignore) {
            Log.v(TAG, "insertException");
        }
    }
}


    public List<AttendenceFixData> getAllAttendanceFix() {

        ArrayList<AttendenceFixData> list = new ArrayList<AttendenceFixData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_FIX ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceFixData data = new AttendenceFixData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpId(cursor.getString(1));
                        data.setCurrentDate(cursor.getString(2));
                        data.setInTime(cursor.getString(3));
                        data.setOutTime(cursor.getString(4));
                        data.setIntimeImage(cursor.getString(5));
                        data.setOuttimeImage(cursor.getString(6));
                        data.setInlat(cursor.getString(7));
                        data.setInlongi(cursor.getString(8));
                        data.setOutlat(cursor.getString(9));
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<AttendenceFixData> getLastRowAttendanceFix() {

        ArrayList<AttendenceFixData> list = new ArrayList<AttendenceFixData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_FIX +  " ORDER BY " + ATT_FIX_INCRI + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendenceFixData data = new AttendenceFixData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpId(cursor.getString(1));
                        data.setCurrentDate(cursor.getString(2));
                        data.setInTime(cursor.getString(3));
                        data.setOutTime(cursor.getString(4));
                        data.setIntimeImage(cursor.getString(5));
                        data.setOuttimeImage(cursor.getString(6));
                        data.setInlat(cursor.getString(7));
                        data.setInlongi(cursor.getString(8));
                        data.setOutlat(cursor.getString(9));
                        data.setOutlongi(cursor.getString(10));
                        data.setIntimeRemark(cursor.getString(11));
                        data.setOuttimeRemark(cursor.getString(12));
                        data.setIntimetimeNear(cursor.getString(13));
                        data.setOuttimeNear(cursor.getString(14));
                        data.setFlag(cursor.getInt(15));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public int getLastInsertIdAttendanceFix() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ATTENDANCE_FIX, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteAttendanceFixListData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCE_FIX + " WHERE " + ATT_FIX_INCRI + "='" + id + "'");
        db.close();
    }

    public boolean updateAttandenceFixStatus(int incriid,String outtime,String outtimePic,String outtimelat,String outtimelong, String intimeremark,String outimenear, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
        args.put(ATT_FIX_OUTTIME, outtime);
        args.put(ATT_FIX_OUTTIME_PICS, outtimePic);
        args.put(ATT_FIX_OUTLAT,outtimelat);
        args.put(ATT_FIX_OUTLONG, outtimelong);
        args.put(ATT_FIX_INTIMEREMARK, intimeremark);
        args.put(ATT_FIX_OUTTIMEREMARK,intimeremark);
        args.put(ATT_FIX_NEARESTOUTTIME,outimenear);
        args.put(ATT_FIX_FLAG, flag);
        int i = db.update(TABLE_ATTENDANCE_FIX, args, ATT_FIX_INCRI + "=" + incriid, null);
        return i > 0;
    }


    //.....................AttendanceRegulerty................
    public void insertAttandanceRegulerty(AttendanceRegulertyData attendencRegulertyData) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(ATT_REG_EMPID, attendencRegulertyData.getEmpid());
            values.put(ATT_REG_CURRENTDATE, attendencRegulertyData.getDate());
            values.put(ATT_REG_INTIME, attendencRegulertyData.getIntime());
            values.put(ATT_REG_OUTTIME, attendencRegulertyData.getOuttime());
            values.put(ATT_REG_REMARK, attendencRegulertyData.getRemark());
            values.put(ATT_REG_SYSTEMTIME, attendencRegulertyData.getTime());
            values.put(ATT_FIX_FLAG, attendencRegulertyData.getFlag());

            // Inserting Row
            db.insert(TABLE_ATTENDANCE_REGULERTY, null, values);
            Log.v(TAG, "Databaser insert Attendance Regulerty table");
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } finally {
            try {
                db.close();

            } catch (Exception ignore) {
                Log.v(TAG, "insertException");
            }
        }
    }


    public List<AttendanceRegulertyData> getAllAttendanceRegulertyData() {

        ArrayList<AttendanceRegulertyData> list = new ArrayList<AttendanceRegulertyData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REGULERTY ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendanceRegulertyData data = new AttendanceRegulertyData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpid(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setIntime(cursor.getString(3));
                        data.setOuttime(cursor.getString(4));
                        data.setRemark(cursor.getString(5));
                        data.setTime(cursor.getString(6));
                        data.setFlag(cursor.getInt(7));
                        //you could add additional columns here..
                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<AttendanceRegulertyData> getLastRowAttendanceRegulerty() {

        ArrayList<AttendanceRegulertyData> list = new ArrayList<AttendanceRegulertyData>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_REGULERTY +  " ORDER BY " + ATT_REG_INCRI + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        AttendanceRegulertyData data = new AttendanceRegulertyData();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEmpid(cursor.getString(1));
                        data.setDate(cursor.getString(2));
                        data.setIntime(cursor.getString(3));
                        data.setOuttime(cursor.getString(4));
                        data.setRemark(cursor.getString(5));
                        data.setTime(cursor.getString(6));
                        data.setFlag(cursor.getInt(7));
                        //you could add additional columns here..
                        list.add(data);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public int getLastInsertIdAttendanceRegulerty() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_ATTENDANCE_REGULERTY, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteAttendanceRegulerty(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ATTENDANCE_REGULERTY + " WHERE " + ATT_REG_INCRI + "='" + id + "'");
        db.close();
    }

    public boolean updateAttandenceRegulertyStatus(int incriid, int flag) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();

        args.put(ATT_REG_FLAG, flag);
        int i = db.update(TABLE_ATTENDANCE_REGULERTY, args, ATT_REG_INCRI + "=" + incriid, null);
        return i > 0;
    }


    //.....................EMP_JOB_DETAIL ................
    public void insertEMPJOBDETAIL(Emp_Job_Details empJobDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put(EMP_ID, empJobDetails.getEMP_ID());
            values.put(JOB_STATUS, empJobDetails.getJOB_STATUS());
            values.put(PROJECT_SUB_LOC, empJobDetails.getPROJECT_SUB_LOC());
            values.put(RESOURCE_TYPE, empJobDetails.getRESOURCE_TYPE());
            values.put(CLIENT_ID, empJobDetails.getCLIENT_ID());
            values.put(PROJECT_ID, empJobDetails.getPROJECT_ID());
            values.put(LOCATION_ID, empJobDetails.getLOCATION_ID());
            values.put(DESIG_ID, empJobDetails.getDESIG_ID());
            values.put(DEPT_ID, empJobDetails.getDEPT_ID());
            values.put(REPORTING_MANGER_ID, empJobDetails.getREPORTING_MANGER_ID());
            values.put(PROJECT_HEAD_ID, empJobDetails.getPROJECT_HEAD_ID());
            values.put(REPORTING_CORDINATE_ID, empJobDetails.getREPORTING_CORDINATE_ID());

            // Inserting Row
            db.insert(TABLE_EMP_JOB_DETAILS, null, values);
            Log.v(TAG, "Databaser insert Emp JobDetails table");
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
        } finally {
            try {
                db.close();

            } catch (Exception ignore) {
                Log.v(TAG, "insertException");
            }
        }
    }

    public List<Emp_Job_Details> getAllEMPJOBDRETAILData() {

        ArrayList<Emp_Job_Details> list = new ArrayList<Emp_Job_Details>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_EMP_JOB_DETAILS ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {


            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Emp_Job_Details data = new Emp_Job_Details();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEMP_ID(cursor.getString(1));
                        data.setJOB_STATUS(cursor.getString(2));
                        data.setPROJECT_SUB_LOC(cursor.getString(3));
                        data.setRESOURCE_TYPE(cursor.getString(4));
                        data.setCLIENT_ID(cursor.getString(5));
                        data.setPROJECT_ID(cursor.getString(6));
                        data.setLOCATION_ID(cursor.getString(7));
                        data.setDESIG_ID(cursor.getString(8));
                        data.setDEPT_ID(cursor.getString(9));
                        data.setREPORTING_MANGER_ID(cursor.getString(10));
                        data.setPROJECT_HEAD_ID(cursor.getString(11));
                        data.setREPORTING_CORDINATE_ID(cursor.getString(12));

                        list.add(data);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public List<Emp_Job_Details> getLastRowEMPJOBDETAILS() {

        ArrayList<Emp_Job_Details> list = new ArrayList<Emp_Job_Details>();
        // Select All Query
        // SELECT * FROM members ORDER BY date_of_birth DESC;
        String selectQuery = "SELECT  * FROM " + TABLE_EMP_JOB_DETAILS +  " ORDER BY " + IN_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Emp_Job_Details data = new Emp_Job_Details();
                        //only one column
                        data.setId(cursor.getInt(0));
                        data.setEMP_ID(cursor.getString(1));
                        data.setJOB_STATUS(cursor.getString(2));
                        data.setPROJECT_SUB_LOC(cursor.getString(3));
                        data.setRESOURCE_TYPE(cursor.getString(4));
                        data.setCLIENT_ID(cursor.getString(5));
                        data.setPROJECT_ID(cursor.getString(6));
                        data.setLOCATION_ID(cursor.getString(7));
                        data.setDESIG_ID(cursor.getString(8));
                        data.setDEPT_ID(cursor.getString(9));
                        data.setREPORTING_MANGER_ID(cursor.getString(10));
                        data.setPROJECT_HEAD_ID(cursor.getString(11));
                        data.setREPORTING_CORDINATE_ID(cursor.getString(12));
                        //you could add additional columns here..
                        list.add(data);

                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();

                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {

            }
        }

        return list;
    }

    public int getLastInsertIdEMPJOBDETAILS() {
        int index = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_EMP_JOB_DETAILS, null);

        if(cursor.moveToLast()) {
            index = cursor.getInt(0);//to get id, 0 is the column index
        }
        cursor.close();
        return index;
    }

    public void deleteEMPJOBDETAILS() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EMP_JOB_DETAILS + ";" );
        db.close();
    }


}