package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;

@Dao
public interface CS_Dao_AccessDetails {

    @Insert
    long insertAccessDetails(CS_Entity_AccessDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_Access_Details)
    void deleteAllAccessDetails();

    @Query("SELECT " +
            "COALESCE(AD_Auto_Id,'') AS [AD_Auto_Id], " +
            "COALESCE(AD_SourceId,'') AS [AD_SourceId], " +
            "COALESCE(AD_E_No,'') AS [AD_E_No], " +
            "COALESCE(AD_E_Name,'') AS [AD_E_Name], " +
            "COALESCE(AD_E_Unit,'') AS [AD_E_Unit], " +
            "COALESCE(AD_E_VehicleNo,'') AS [AD_E_VehicleNo], " +
            "COALESCE(AD_E_CreationDate,'') AS [AD_E_CreationDate], " +
            "COALESCE(AD_E_UpdationDate,'') AS [AD_E_UpdationDate], " +
            "COALESCE(AD_E_No_Encrypted,'') AS [AD_E_No_Encrypted], " +
            "COALESCE(AD_E_FloorUnit,'') AS [AD_E_FloorUnit] " +
            "FROM " + CS_SQLiteTable.VMS_Access_Details + " " +
            "ORDER BY AD_Auto_Id DESC LIMIT 1")
    CS_Entity_AccessDetails getAccessDetails();

    // For updating specific data only base on id
    @Query("UPDATE " + CS_SQLiteTable.VMS_Access_Details + " SET " +
            "AD_E_No = :AD_E_No, " +
            "AD_E_Name = :AD_E_Name, " +
            "AD_E_Unit = :AD_E_Unit, " +
            "AD_E_VehicleNo = :AD_E_VehicleNo, " +
            "AD_E_UpdationDate = :AD_E_UpdationDate, " +
            "AD_E_No_Encrypted = :AD_E_No_Encrypted, " +
            "AD_E_FloorUnit = :AD_E_FloorUnit " +
            "WHERE AD_SourceId = :whereColumnId")
    int updateAccessDetails(
            String whereColumnId,
            String AD_E_No,
            String AD_E_Name,
            String AD_E_Unit,
            String AD_E_VehicleNo,
            String AD_E_UpdationDate,
            String AD_E_No_Encrypted,
            String AD_E_FloorUnit
    );

}
