package com.leegosolutions.vms_host_app.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.leegosolutions.vms_host_app.database.CS_SQLiteTable;

@Entity(tableName = CS_SQLiteTable.VMS_Access_Details)
public class CS_Entity_AccessDetails {

    @PrimaryKey(autoGenerate = true)
    private int AD_Auto_Id;

    private String AD_SourceId;

    private String AD_E_No;

    private String AD_E_Name;

    private String AD_E_Unit;

    private String AD_E_VehicleNo;

    private String AD_E_CreationDate;

    private String AD_E_UpdationDate;

    public CS_Entity_AccessDetails(String AD_SourceId, String AD_E_No, String AD_E_Name, String AD_E_Unit, String AD_E_VehicleNo, String AD_E_CreationDate, String AD_E_UpdationDate) {
        this.AD_SourceId = AD_SourceId;
        this.AD_E_No = AD_E_No;
        this.AD_E_Name = AD_E_Name;
        this.AD_E_Unit = AD_E_Unit;
        this.AD_E_VehicleNo = AD_E_VehicleNo;
        this.AD_E_CreationDate = AD_E_CreationDate;
        this.AD_E_UpdationDate = AD_E_UpdationDate;
    }

    public int getAD_Auto_Id() {
        return AD_Auto_Id;
    }

    public void setAD_Auto_Id(int AD_Auto_Id) {
        this.AD_Auto_Id = AD_Auto_Id;
    }

    public String getAD_SourceId() {
        return AD_SourceId;
    }

    public void setAD_SourceId(String AD_SourceId) {
        this.AD_SourceId = AD_SourceId;
    }

    public String getAD_E_No() {
        return AD_E_No;
    }

    public void setAD_E_No(String AD_E_No) {
        this.AD_E_No = AD_E_No;
    }

    public String getAD_E_Name() {
        return AD_E_Name;
    }

    public void setAD_E_Name(String AD_E_Name) {
        this.AD_E_Name = AD_E_Name;
    }

    public String getAD_E_Unit() {
        return AD_E_Unit;
    }

    public void setAD_E_Unit(String AD_E_Unit) {
        this.AD_E_Unit = AD_E_Unit;
    }

    public String getAD_E_VehicleNo() {
        return AD_E_VehicleNo;
    }

    public void setAD_E_VehicleNo(String AD_E_VehicleNo) {
        this.AD_E_VehicleNo = AD_E_VehicleNo;
    }

    public String getAD_E_CreationDate() {
        return AD_E_CreationDate;
    }

    public void setAD_E_CreationDate(String AD_E_CreationDate) {
        this.AD_E_CreationDate = AD_E_CreationDate;
    }

    public String getAD_E_UpdationDate() {
        return AD_E_UpdationDate;
    }

    public void setAD_E_UpdationDate(String AD_E_UpdationDate) {
        this.AD_E_UpdationDate = AD_E_UpdationDate;
    }
}
