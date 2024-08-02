package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

@Dao
public interface CS_Dao_EmailDetails {

    @Insert
    long insertAccessDetails(CS_Entity_EmailDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_Email_Details)
    void deleteAllEmailDetails();

    @Query(value = "SELECT " +
            "COALESCE(ED_Auto_Id,'') AS [ED_Auto_Id], " +
            "COALESCE(ED_EmailId,'') AS [ED_EmailId], " +
            "COALESCE(ED_Password,'') AS [ED_Password], " +
            "COALESCE(ED_Server,'') AS [ED_Server], " +
            "COALESCE(ED_Port,'') AS [ED_Port], " +
            "COALESCE(ED_EnableSSL,'') AS [ED_EnableSSL], " +
            "COALESCE(ED_CreationDate,'') AS [ED_CreationDate], " +
            "COALESCE(ED_UpdationDate,'') AS [ED_UpdationDate] " +
            "FROM " + CS_SQLiteTable.VMS_Email_Details + " " +
            "WHERE ED_Type =:emailType")
    CS_Entity_EmailDetails getEmailDetails(String emailType);
}
