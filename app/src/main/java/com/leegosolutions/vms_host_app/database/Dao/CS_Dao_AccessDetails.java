package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;

@Dao
public interface CS_Dao_AccessDetails {

    @Insert
    long insertAccessDetails(CS_Entity_AccessDetails model);

    @Insert
    long updateAccessDetails(CS_Entity_AccessDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_Access_Details)
    void deleteAllAccessDetails();

    @Query("SELECT * FROM "+ CS_SQLiteTable.VMS_Access_Details + " ORDER BY AD_Auto_Id DESC LIMIT 1")
    CS_Entity_AccessDetails getAccessDetails();
}
