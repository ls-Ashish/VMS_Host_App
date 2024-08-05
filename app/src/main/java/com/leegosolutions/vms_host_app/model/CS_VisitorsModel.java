package com.leegosolutions.vms_host_app.model;

public class CS_VisitorsModel {

    String id="", appointmentNo="", name = "", type = "", mobileNo = "", startDate = "", endDate = "", overnights = "", status = "";
    boolean isConnected = true, visitorDataFound = true; // default is true

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(String appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOvernights() {
        return overnights;
    }

    public void setOvernights(String overnights) {
        this.overnights = overnights;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isVisitorDataFound() {
        return visitorDataFound;
    }

    public void setVisitorDataFound(boolean visitorDataFound) {
        this.visitorDataFound = visitorDataFound;
    }
}
