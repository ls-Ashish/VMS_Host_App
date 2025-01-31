package com.leegosolutions.vms_host_app.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Entity(tableName = CS_SQLiteTable.VMS_LogIn_Details)
public class CS_Entity_LoginDetails {

    @PrimaryKey(autoGenerate = true)
    private int LD_Auto_Id = 0;
    private String LD_SourceId = "";
    private String LD_Email = "";
    private String LD_Password = "";
    private String LD_UserType = "";
    private String LD_UserName = "";
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] LD_UserPhoto = null;
    private int LD_IsLogin = 0;
    private String LD_CreationDate = "";
    private String LD_UpdationDate = "";
    private String LD_CountryCode = "";
    private String LD_MobileNo = "";
    private String LD_S_PIN_Status = "";
    private String LD_S_PIN = "";
    private String LD_S_Fingerprint_Status = "";

    public CS_Entity_LoginDetails() {}

    public CS_Entity_LoginDetails(String LD_SourceId, String LD_Email, String LD_Password, String LD_UserType, String LD_UserName, byte[] LD_UserPhoto, int LD_IsLogin, String LD_CreationDate, String LD_UpdationDate, String LD_CountryCode, String LD_MobileNo, String LD_S_PIN_Status, String LD_S_PIN, String LD_S_Fingerprint_Status) {
        this.LD_SourceId = LD_SourceId;
        this.LD_Email = LD_Email;
        this.LD_Password = LD_Password;
        this.LD_UserType = LD_UserType;
        this.LD_UserName = LD_UserName;
        this.LD_UserPhoto = LD_UserPhoto;
        this.LD_IsLogin = LD_IsLogin;
        this.LD_CreationDate = LD_CreationDate;
        this.LD_UpdationDate = LD_UpdationDate;
        this.LD_CountryCode = LD_CountryCode;
        this.LD_MobileNo = LD_MobileNo;
        this.LD_S_PIN_Status = LD_S_PIN_Status;
        this.LD_S_PIN = LD_S_PIN;
        this.LD_S_Fingerprint_Status = LD_S_Fingerprint_Status;
    }

    public int getLD_Auto_Id() {
        return LD_Auto_Id;
    }

    public void setLD_Auto_Id(int LD_Auto_Id) {
        this.LD_Auto_Id = LD_Auto_Id;
    }

    public String getLD_SourceId() {
        return LD_SourceId;
    }

    public void setLD_SourceId(String LD_SourceId) {
        this.LD_SourceId = LD_SourceId;
    }

    public String getLD_Email() {
        return LD_Email;
    }

    public void setLD_Email(String LD_Email) {
        this.LD_Email = LD_Email;
    }

    public String getLD_Password() {
        return LD_Password;
    }

    public void setLD_Password(String LD_Password) {
        this.LD_Password = LD_Password;
    }

    public String getLD_UserType() {
        return LD_UserType;
    }

    public void setLD_UserType(String LD_UserType) {
        this.LD_UserType = LD_UserType;
    }

    public String getLD_UserName() {
        return LD_UserName;
    }

    public void setLD_UserName(String LD_UserName) {
        this.LD_UserName = LD_UserName;
    }

    public byte[] getLD_UserPhoto() {
        return LD_UserPhoto;
    }

    public void setLD_UserPhoto(byte[] LD_UserPhoto) {
        this.LD_UserPhoto = LD_UserPhoto;
    }

    public int getLD_IsLogin() {
        return LD_IsLogin;
    }

    public void setLD_IsLogin(int LD_IsLogin) {
        this.LD_IsLogin = LD_IsLogin;
    }

    public String getLD_CreationDate() {
        return LD_CreationDate;
    }

    public void setLD_CreationDate(String LD_CreationDate) {
        this.LD_CreationDate = LD_CreationDate;
    }

    public String getLD_UpdationDate() {
        return LD_UpdationDate;
    }

    public void setLD_UpdationDate(String LD_UpdationDate) {
        this.LD_UpdationDate = LD_UpdationDate;
    }

    public String getLD_CountryCode() {
        return LD_CountryCode;
    }

    public void setLD_CountryCode(String LD_CountryCode) {
        this.LD_CountryCode = LD_CountryCode;
    }

    public String getLD_MobileNo() {
        return LD_MobileNo;
    }

    public void setLD_MobileNo(String LD_MobileNo) {
        this.LD_MobileNo = LD_MobileNo;
    }

    public String getLD_S_PIN_Status() {
        return LD_S_PIN_Status;
    }

    public void setLD_S_PIN_Status(String LD_S_PIN_Status) {
        this.LD_S_PIN_Status = LD_S_PIN_Status;
    }

    public String getLD_S_PIN() {
        return LD_S_PIN;
    }

    public void setLD_S_PIN(String LD_S_PIN) {
        this.LD_S_PIN = LD_S_PIN;
    }

    public String getLD_S_Fingerprint_Status() {
        return LD_S_Fingerprint_Status;
    }

    public void setLD_S_Fingerprint_Status(String LD_S_Fingerprint_Status) {
        this.LD_S_Fingerprint_Status = LD_S_Fingerprint_Status;
    }
}
