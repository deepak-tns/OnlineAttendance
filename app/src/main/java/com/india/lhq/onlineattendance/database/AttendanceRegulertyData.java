package com.india.lhq.onlineattendance.database;

public class AttendanceRegulertyData {

    private int id;
    private String empid;
    private String date;
    private String intime;
    private String outtime;
    private String remark;
    private int flag;
    private String time;

    public AttendanceRegulertyData() {
    }

    public AttendanceRegulertyData(String empid, String date, String intime, String outtime, String remark, int flag,String time) {
        this.empid = empid;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.remark = remark;
        this.flag = flag;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
