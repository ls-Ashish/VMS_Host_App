package com.leegosolutions.vms_host_app.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Entity(tableName = CS_SQLiteTable.VMS_SMS_Details)
public class CS_Entity_SMSDetails {

    @PrimaryKey(autoGenerate = true)
    private int SD_Auto_Id = 0;
    private String SD_Platform = "";
    private String SD_AccountNo = "";
    private String SD_TokenNo = "";
    private String SD_ServiceNo = "";
    private String SD_Sender = "";
    private String SD_UrlCode = "";
    private String SD_ExpiryTime = "";
    private String SD_Platform_WB = "";
    private String SD_P_WB_TemplateID = "";
    private String SD_P_WB_Object_1 = "";
    private String SD_P_WB_Object_2 = "";
    private String SD_P_WB_Object_3 = "";
    private String SD_P_WB_Object_4 = "";
    private String SD_CreationDate = "";
    private String SD_UpdationDate = "";

    public CS_Entity_SMSDetails() {}

    public CS_Entity_SMSDetails(String SD_Platform, String SD_AccountNo, String SD_TokenNo, String SD_ServiceNo, String SD_Sender, String SD_UrlCode, String SD_ExpiryTime, String SD_Platform_WB, String SD_P_WB_TemplateID, String SD_P_WB_Object_1, String SD_P_WB_Object_2, String SD_P_WB_Object_3, String SD_P_WB_Object_4, String SD_CreationDate, String SD_UpdationDate) {
        this.SD_Platform = SD_Platform;
        this.SD_AccountNo = SD_AccountNo;
        this.SD_TokenNo = SD_TokenNo;
        this.SD_ServiceNo = SD_ServiceNo;
        this.SD_Sender = SD_Sender;
        this.SD_UrlCode = SD_UrlCode;
        this.SD_ExpiryTime = SD_ExpiryTime;
        this.SD_Platform_WB = SD_Platform_WB;
        this.SD_P_WB_TemplateID = SD_P_WB_TemplateID;
        this.SD_P_WB_Object_1 = SD_P_WB_Object_1;
        this.SD_P_WB_Object_2 = SD_P_WB_Object_2;
        this.SD_P_WB_Object_3 = SD_P_WB_Object_3;
        this.SD_P_WB_Object_4 = SD_P_WB_Object_4;
        this.SD_CreationDate = SD_CreationDate;
        this.SD_UpdationDate = SD_UpdationDate;
    }

    public int getSD_Auto_Id() {
        return SD_Auto_Id;
    }

    public void setSD_Auto_Id(int SD_Auto_Id) {
        this.SD_Auto_Id = SD_Auto_Id;
    }

    public String getSD_Platform() {
        return SD_Platform;
    }

    public void setSD_Platform(String SD_Platform) {
        this.SD_Platform = SD_Platform;
    }

    public String getSD_AccountNo() {
        return SD_AccountNo;
    }

    public void setSD_AccountNo(String SD_AccountNo) {
        this.SD_AccountNo = SD_AccountNo;
    }

    public String getSD_TokenNo() {
        return SD_TokenNo;
    }

    public void setSD_TokenNo(String SD_TokenNo) {
        this.SD_TokenNo = SD_TokenNo;
    }

    public String getSD_ServiceNo() {
        return SD_ServiceNo;
    }

    public void setSD_ServiceNo(String SD_ServiceNo) {
        this.SD_ServiceNo = SD_ServiceNo;
    }

    public String getSD_Sender() {
        return SD_Sender;
    }

    public void setSD_Sender(String SD_Sender) {
        this.SD_Sender = SD_Sender;
    }

    public String getSD_UrlCode() {
        return SD_UrlCode;
    }

    public void setSD_UrlCode(String SD_UrlCode) {
        this.SD_UrlCode = SD_UrlCode;
    }

    public String getSD_ExpiryTime() {
        return SD_ExpiryTime;
    }

    public void setSD_ExpiryTime(String SD_ExpiryTime) {
        this.SD_ExpiryTime = SD_ExpiryTime;
    }

    public String getSD_Platform_WB() {
        return SD_Platform_WB;
    }

    public void setSD_Platform_WB(String SD_Platform_WB) {
        this.SD_Platform_WB = SD_Platform_WB;
    }

    public String getSD_P_WB_TemplateID() {
        return SD_P_WB_TemplateID;
    }

    public void setSD_P_WB_TemplateID(String SD_P_WB_TemplateID) {
        this.SD_P_WB_TemplateID = SD_P_WB_TemplateID;
    }

    public String getSD_P_WB_Object_1() {
        return SD_P_WB_Object_1;
    }

    public void setSD_P_WB_Object_1(String SD_P_WB_Object_1) {
        this.SD_P_WB_Object_1 = SD_P_WB_Object_1;
    }

    public String getSD_P_WB_Object_2() {
        return SD_P_WB_Object_2;
    }

    public void setSD_P_WB_Object_2(String SD_P_WB_Object_2) {
        this.SD_P_WB_Object_2 = SD_P_WB_Object_2;
    }

    public String getSD_P_WB_Object_3() {
        return SD_P_WB_Object_3;
    }

    public void setSD_P_WB_Object_3(String SD_P_WB_Object_3) {
        this.SD_P_WB_Object_3 = SD_P_WB_Object_3;
    }

    public String getSD_P_WB_Object_4() {
        return SD_P_WB_Object_4;
    }

    public void setSD_P_WB_Object_4(String SD_P_WB_Object_4) {
        this.SD_P_WB_Object_4 = SD_P_WB_Object_4;
    }

    public String getSD_CreationDate() {
        return SD_CreationDate;
    }

    public void setSD_CreationDate(String SD_CreationDate) {
        this.SD_CreationDate = SD_CreationDate;
    }

    public String getSD_UpdationDate() {
        return SD_UpdationDate;
    }

    public void setSD_UpdationDate(String SD_UpdationDate) {
        this.SD_UpdationDate = SD_UpdationDate;
    }
}
