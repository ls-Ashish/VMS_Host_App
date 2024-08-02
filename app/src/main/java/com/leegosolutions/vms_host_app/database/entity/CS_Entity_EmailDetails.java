package com.leegosolutions.vms_host_app.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Entity(tableName = CS_SQLiteTable.VMS_Email_Details)
public class CS_Entity_EmailDetails {

    @PrimaryKey(autoGenerate = true)
    private int ED_Auto_Id = 0;
    private String ED_Type = "";
    private String ED_EmailId = "";
    private String ED_Password = "";
    private String ED_Server = "";
    private String ED_Port = "";
    private String ED_EnableSSL = "";
    private String ED_CreationDate = "";
    private String ED_UpdationDate = "";

    public CS_Entity_EmailDetails() {}

    public CS_Entity_EmailDetails(String ED_Type, String ED_EmailId, String ED_Password, String ED_Server, String ED_Port, String ED_EnableSSL, String ED_CreationDate, String ED_UpdationDate) {
        this.ED_Type = ED_Type;
        this.ED_EmailId = ED_EmailId;
        this.ED_Password = ED_Password;
        this.ED_Server = ED_Server;
        this.ED_Port = ED_Port;
        this.ED_EnableSSL = ED_EnableSSL;
        this.ED_CreationDate = ED_CreationDate;
        this.ED_UpdationDate = ED_UpdationDate;
    }

    public int getED_Auto_Id() {
        return ED_Auto_Id;
    }

    public void setED_Auto_Id(int ED_Auto_Id) {
        this.ED_Auto_Id = ED_Auto_Id;
    }

    public String getED_Type() {
        return ED_Type;
    }

    public void setED_Type(String ED_Type) {
        this.ED_Type = ED_Type;
    }

    public String getED_EmailId() {
        return ED_EmailId;
    }

    public void setED_EmailId(String ED_EmailId) {
        this.ED_EmailId = ED_EmailId;
    }

    public String getED_Password() {
        return ED_Password;
    }

    public void setED_Password(String ED_Password) {
        this.ED_Password = ED_Password;
    }

    public String getED_Server() {
        return ED_Server;
    }

    public void setED_Server(String ED_Server) {
        this.ED_Server = ED_Server;
    }

    public String getED_Port() {
        return ED_Port;
    }

    public void setED_Port(String ED_Port) {
        this.ED_Port = ED_Port;
    }

    public String getED_EnableSSL() {
        return ED_EnableSSL;
    }

    public void setED_EnableSSL(String ED_EnableSSL) {
        this.ED_EnableSSL = ED_EnableSSL;
    }

    public String getED_CreationDate() {
        return ED_CreationDate;
    }

    public void setED_CreationDate(String ED_CreationDate) {
        this.ED_CreationDate = ED_CreationDate;
    }

    public String getED_UpdationDate() {
        return ED_UpdationDate;
    }

    public void setED_UpdationDate(String ED_UpdationDate) {
        this.ED_UpdationDate = ED_UpdationDate;
    }
}
