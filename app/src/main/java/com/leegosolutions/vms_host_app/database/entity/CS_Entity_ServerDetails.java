package com.leegosolutions.vms_host_app.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Entity(tableName = CS_SQLiteTable.VMS_Server_Details)
public class CS_Entity_ServerDetails {

    @PrimaryKey(autoGenerate = true)
    private int SD_Auto_Id = 0;
    private String SD_BaseURL = "";
    private String SD_BU_ID = "";
    private String SD_TE_ID = "";
    private String SD_BCode = "";
    private String SD_TCode = "";
    private String SD_ClientSecret = "";
    private String SD_BuildingName = "";
    private String SD_TenantName = "";
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] SD_Logo = null;
    private String SD_AppToken = "";
    private String SD_ErrorPostingURL = "";
    private String SD_CreationDate = "";
    private String SD_UpdationDate = "";
    private String SD_Status = "";
    private String SD_Country = "";
    private String SD_Address_Line_1 = "";
    private String SD_Address_Line_2 = "";

    public CS_Entity_ServerDetails() {}

    public CS_Entity_ServerDetails(String SD_BaseURL, String SD_BU_ID, String SD_TE_ID, String SD_BCode, String SD_TCode, String SD_ClientSecret, String SD_BuildingName, String SD_TenantName, byte[] SD_Logo, String SD_AppToken, String SD_ErrorPostingURL, String SD_CreationDate, String SD_UpdationDate, String SD_Status, String SD_Country, String SD_Address_Line_1, String SD_Address_Line_2) {
        this.SD_BaseURL = SD_BaseURL;
        this.SD_BU_ID = SD_BU_ID;
        this.SD_TE_ID = SD_TE_ID;
        this.SD_BCode = SD_BCode;
        this.SD_TCode = SD_TCode;
        this.SD_ClientSecret = SD_ClientSecret;
        this.SD_BuildingName = SD_BuildingName;
        this.SD_TenantName = SD_TenantName;
        this.SD_Logo = SD_Logo;
        this.SD_AppToken = SD_AppToken;
        this.SD_ErrorPostingURL = SD_ErrorPostingURL;
        this.SD_CreationDate = SD_CreationDate;
        this.SD_UpdationDate = SD_UpdationDate;
        this.SD_Status = SD_Status;
        this.SD_Country = SD_Country;
        this.SD_Address_Line_1 = SD_Address_Line_1;
        this.SD_Address_Line_2 = SD_Address_Line_2;
    }

    public int getSD_Auto_Id() {
        return SD_Auto_Id;
    }

    public void setSD_Auto_Id(int SD_Auto_Id) {
        this.SD_Auto_Id = SD_Auto_Id;
    }

    public String getSD_BaseURL() {
        return SD_BaseURL;
    }

    public void setSD_BaseURL(String SD_BaseURL) {
        this.SD_BaseURL = SD_BaseURL;
    }

    public String getSD_BU_ID() {
        return SD_BU_ID;
    }

    public void setSD_BU_ID(String SD_BU_ID) {
        this.SD_BU_ID = SD_BU_ID;
    }

    public String getSD_TE_ID() {
        return SD_TE_ID;
    }

    public void setSD_TE_ID(String SD_TE_ID) {
        this.SD_TE_ID = SD_TE_ID;
    }

    public String getSD_BCode() {
        return SD_BCode;
    }

    public void setSD_BCode(String SD_BCode) {
        this.SD_BCode = SD_BCode;
    }

    public String getSD_TCode() {
        return SD_TCode;
    }

    public void setSD_TCode(String SD_TCode) {
        this.SD_TCode = SD_TCode;
    }

    public String getSD_ClientSecret() {
        return SD_ClientSecret;
    }

    public void setSD_ClientSecret(String SD_ClientSecret) {
        this.SD_ClientSecret = SD_ClientSecret;
    }

    public String getSD_BuildingName() {
        return SD_BuildingName;
    }

    public void setSD_BuildingName(String SD_BuildingName) {
        this.SD_BuildingName = SD_BuildingName;
    }

    public String getSD_TenantName() {
        return SD_TenantName;
    }

    public void setSD_TenantName(String SD_TenantName) {
        this.SD_TenantName = SD_TenantName;
    }

    public byte[] getSD_Logo() {
        return SD_Logo;
    }

    public void setSD_Logo(byte[] SD_Logo) {
        this.SD_Logo = SD_Logo;
    }

    public String getSD_AppToken() {
        return SD_AppToken;
    }

    public void setSD_AppToken(String SD_AppToken) {
        this.SD_AppToken = SD_AppToken;
    }

    public String getSD_ErrorPostingURL() {
        return SD_ErrorPostingURL;
    }

    public void setSD_ErrorPostingURL(String SD_ErrorPostingURL) {
        this.SD_ErrorPostingURL = SD_ErrorPostingURL;
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

    public String getSD_Status() {
        return SD_Status;
    }

    public void setSD_Status(String SD_Status) {
        this.SD_Status = SD_Status;
    }

    public String getSD_Country() {
        return SD_Country;
    }

    public void setSD_Country(String SD_Country) {
        this.SD_Country = SD_Country;
    }

    public String getSD_Address_Line_1() {
        return SD_Address_Line_1;
    }

    public void setSD_Address_Line_1(String SD_Address_Line_1) {
        this.SD_Address_Line_1 = SD_Address_Line_1;
    }

    public String getSD_Address_Line_2() {
        return SD_Address_Line_2;
    }

    public void setSD_Address_Line_2(String SD_Address_Line_2) {
        this.SD_Address_Line_2 = SD_Address_Line_2;
    }
}
