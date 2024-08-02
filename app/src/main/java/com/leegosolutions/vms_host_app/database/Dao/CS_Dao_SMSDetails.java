package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Dao
public interface CS_Dao_SMSDetails {

    @Insert
    long insertSMSDetails(CS_Entity_SMSDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_SMS_Details)
    void deleteAllSMSDetails();

    @Query("SELECT " +
            "COALESCE(SD_Auto_Id,'') AS [SD_Auto_Id], " +
            "COALESCE(SD_Platform,'') AS [SD_Platform], " +
            "COALESCE(SD_AccountNo,'') AS [SD_AccountNo], " +
            "COALESCE(SD_TokenNo,'') AS [SD_TokenNo], " +
            "COALESCE(SD_ServiceNo,'') AS [SD_ServiceNo], " +
            "COALESCE(SD_Sender,'') AS [SD_Sender], " +
            "COALESCE(SD_UrlCode,'') AS [SD_UrlCode], " +
            "COALESCE(SD_ExpiryTime,'') AS [SD_ExpiryTime], " +
            "COALESCE(SD_Platform_WB,'') AS [SD_Platform_WB], " +
            "COALESCE(SD_P_WB_TemplateID,'') AS [SD_P_WB_TemplateID], " +
            "COALESCE(SD_P_WB_Object_1,'') AS [SD_P_WB_Object_1], " +
            "COALESCE(SD_P_WB_Object_2,'') AS [SD_P_WB_Object_2], " +
            "COALESCE(SD_P_WB_Object_3,'') AS [SD_P_WB_Object_3], " +
            "COALESCE(SD_P_WB_Object_4,'') AS [SD_P_WB_Object_4], " +
            "COALESCE(SD_CreationDate,'') AS [SD_CreationDate], " +
            "COALESCE(SD_UpdationDate,'') AS [SD_UpdationDate] " +
            "FROM " + CS_SQLiteTable.VMS_SMS_Details + " " +
            "ORDER BY SD_Auto_Id DESC LIMIT 1")
    CS_Entity_SMSDetails getSMSDetails();

}
