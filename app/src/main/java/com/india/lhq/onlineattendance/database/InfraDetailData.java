package com.india.lhq.onlineattendance.database;

public class InfraDetailData {
    private String infraID;
    private String infraType;

    public InfraDetailData() {
    }

    public InfraDetailData(String infraID, String infraType) {
        this.infraID = infraID;
        this.infraType = infraType;
    }

    public String getInfraID() {
        return infraID;
    }

    public void setInfraID(String infraID) {
        this.infraID = infraID;
    }

    public String getInfraType() {
        return infraType;
    }

    public void setInfraType(String infraType) {
        this.infraType = infraType;
    }
}
