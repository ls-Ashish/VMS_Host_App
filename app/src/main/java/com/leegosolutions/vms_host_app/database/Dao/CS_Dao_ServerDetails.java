package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;

@Dao
public interface CS_Dao_ServerDetails {

    @Insert
    long insertServerDetails(CS_Entity_ServerDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_Server_Details)
    void deleteAllServerDetails();

    @Query("SELECT " +
            "COALESCE(SD_Auto_Id,'') AS [SD_Auto_Id], " +
            "COALESCE(SD_BaseURL,'') AS [SD_BaseURL], " +
            "COALESCE(SD_BU_ID,'') AS [SD_BU_ID], " +
            "COALESCE(SD_TE_ID,'') AS [SD_TE_ID], " +
            "COALESCE(SD_BCode,'') AS [SD_BCode], " +
            "COALESCE(SD_TCode,'') AS [SD_TCode], " +
            "COALESCE(SD_ClientSecret,'') AS [SD_ClientSecret], " +
            "COALESCE(SD_BuildingName,'') AS [SD_BuildingName], " +
            "COALESCE(SD_TenantName,'') AS [SD_TenantName], " +
            "[SD_Logo] AS [SD_Logo], " +
            "COALESCE(SD_AppToken,'') AS [SD_AppToken], " +
            "COALESCE(SD_ErrorPostingURL,'') AS [SD_ErrorPostingURL], " +
            "COALESCE(SD_CreationDate,'') AS [SD_CreationDate], " +
            "COALESCE(SD_UpdationDate,'') AS [SD_UpdationDate], " +
            "COALESCE(SD_Status,'') AS [SD_Status], " +
            "COALESCE(SD_Country,'') AS [SD_Country], " +
            "COALESCE(SD_Address_Line_1,'') AS [SD_Address_Line_1], " +
            "COALESCE(SD_Address_Line_2,'') AS [SD_Address_Line_2] " +
            "FROM " + CS_SQLiteTable.VMS_Server_Details + " " +
            "ORDER BY SD_Auto_Id DESC LIMIT 1")
    CS_Entity_ServerDetails getServerDetails();



    // Update server details
    @Query("UPDATE " + CS_SQLiteTable.VMS_Server_Details  + " SET " +
            "SD_BuildingName = :buildingName, " +
            "SD_BuildingName = :buildingName, " +
            "SD_TenantName = :tenantName, " +
            "SD_Logo = :logo, " +
            "SD_ErrorPostingURL = :errorPostingURL, " +
            "SD_UpdationDate = :updationDate, " +
            "SD_Country = :country, " +
            "SD_Address_Line_1 = :address_Line_1, " +
            "SD_Address_Line_2 = :address_Line_2 " +
            "WHERE SD_BU_ID = :where_Building_Id AND SD_TE_ID = :where_Tenant_Id")
    int updateServerDetails(String where_Building_Id, String where_Tenant_Id, String buildingName, String tenantName, byte[] logo, String errorPostingURL, String updationDate, String country, String address_Line_1, String address_Line_2);


}
