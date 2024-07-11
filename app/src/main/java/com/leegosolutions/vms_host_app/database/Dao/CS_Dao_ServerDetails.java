package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;

@Dao
public interface CS_Dao_ServerDetails {

    @Insert
    long insertServerDetails(CS_Entity_ServerDetails model);

    @Insert
    long updateServerDetails(CS_Entity_ServerDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_Server_Details)
    void deleteAllServerDetails();

    @Query("SELECT * FROM "+ CS_SQLiteTable.VMS_Server_Details + " ORDER BY SD_Auto_Id DESC LIMIT 1")
    CS_Entity_ServerDetails getServerDetails();
}
