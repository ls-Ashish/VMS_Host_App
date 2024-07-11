package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;

@Dao
public interface CS_Dao_LoginDetails {

    @Insert
    long insertLoginDetails(CS_Entity_LoginDetails model);

    @Insert
    void updateLoginDetails(CS_Entity_LoginDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_LogIn_Details)
    void deleteAllLoginDetails();

    @Query("SELECT * FROM "+ CS_SQLiteTable.VMS_LogIn_Details + " ORDER BY LD_Auto_Id DESC LIMIT 1")
    CS_Entity_LoginDetails getLoginDetails();
}
