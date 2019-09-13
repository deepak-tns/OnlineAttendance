package com.india.lhq.onlineattendance.database;

public class Emp_Job_Details {

    private int id;
    private String EMP_ID;
    private String JOB_STATUS;
    private String PROJECT_SUB_LOC;
    private String RESOURCE_TYPE;
    private String CLIENT_ID;
    private String PROJECT_ID;
    private String LOCATION_ID;
    private String DESIG_ID;
    private String DEPT_ID;
    private String REPORTING_MANGER_ID;
    private String PROJECT_HEAD_ID;
    private String REPORTING_CORDINATE_ID;


    public Emp_Job_Details() {
    }

    public Emp_Job_Details(String EMP_ID, String JOB_STATUS, String PROJECT_SUB_LOC, String RESOURCE_TYPE, String CLIENT_ID, String PROJECT_ID, String LOCATION_ID, String DESIG_ID, String DEPT_ID, String REPORTING_MANGER_ID, String PROJECT_HEAD_ID, String REPORTING_CORDINATE_ID) {
        this.EMP_ID = EMP_ID;
        this.JOB_STATUS = JOB_STATUS;
        this.PROJECT_SUB_LOC = PROJECT_SUB_LOC;
        this.RESOURCE_TYPE = RESOURCE_TYPE;
        this.CLIENT_ID = CLIENT_ID;
        this.PROJECT_ID = PROJECT_ID;
        this.LOCATION_ID = LOCATION_ID;
        this.DESIG_ID = DESIG_ID;
        this.DEPT_ID = DEPT_ID;
        this.REPORTING_MANGER_ID = REPORTING_MANGER_ID;
        this.PROJECT_HEAD_ID = PROJECT_HEAD_ID;
        this.REPORTING_CORDINATE_ID = REPORTING_CORDINATE_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(String EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getJOB_STATUS() {
        return JOB_STATUS;
    }

    public void setJOB_STATUS(String JOB_STATUS) {
        this.JOB_STATUS = JOB_STATUS;
    }

    public String getPROJECT_SUB_LOC() {
        return PROJECT_SUB_LOC;
    }

    public void setPROJECT_SUB_LOC(String PROJECT_SUB_LOC) {
        this.PROJECT_SUB_LOC = PROJECT_SUB_LOC;
    }

    public String getRESOURCE_TYPE() {
        return RESOURCE_TYPE;
    }

    public void setRESOURCE_TYPE(String RESOURCE_TYPE) {
        this.RESOURCE_TYPE = RESOURCE_TYPE;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setCLIENT_ID(String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public String getLOCATION_ID() {
        return LOCATION_ID;
    }

    public void setLOCATION_ID(String LOCATION_ID) {
        this.LOCATION_ID = LOCATION_ID;
    }

    public String getDESIG_ID() {
        return DESIG_ID;
    }

    public void setDESIG_ID(String DESIG_ID) {
        this.DESIG_ID = DESIG_ID;
    }

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public void setDEPT_ID(String DEPT_ID) {
        this.DEPT_ID = DEPT_ID;
    }

    public String getREPORTING_MANGER_ID() {
        return REPORTING_MANGER_ID;
    }

    public void setREPORTING_MANGER_ID(String REPORTING_MANGER_ID) {
        this.REPORTING_MANGER_ID = REPORTING_MANGER_ID;
    }

    public String getPROJECT_HEAD_ID() {
        return PROJECT_HEAD_ID;
    }

    public void setPROJECT_HEAD_ID(String PROJECT_HEAD_ID) {
        this.PROJECT_HEAD_ID = PROJECT_HEAD_ID;
    }

    public String getREPORTING_CORDINATE_ID() {
        return REPORTING_CORDINATE_ID;
    }

    public void setREPORTING_CORDINATE_ID(String REPORTING_CORDINATE_ID) {
        this.REPORTING_CORDINATE_ID = REPORTING_CORDINATE_ID;
    }
}
