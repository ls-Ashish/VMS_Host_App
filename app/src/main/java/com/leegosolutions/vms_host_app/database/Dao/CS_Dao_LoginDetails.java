package com.leegosolutions.vms_host_app.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.leegosolutions.vms_host_app.database.join.CS_LoginDetailsWithAccessDetails;
import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;

@Dao
public interface CS_Dao_LoginDetails {

    @Insert
    long insertLoginDetails(CS_Entity_LoginDetails model);

    @Query("DELETE FROM " + CS_SQLiteTable.VMS_LogIn_Details)
    void deleteAllLoginDetails();

    @Query("SELECT " +
            "COALESCE(LD_Auto_Id,'') AS [LD_Auto_Id], " +
            "COALESCE(LD_SourceId,'') AS [LD_SourceId], " +
            "COALESCE(LD_Email,'') AS [LD_Email], " +
            "COALESCE(LD_Password,'') AS [LD_Password], " +
            "COALESCE(LD_UserType,'') AS [LD_UserType], " +
            "COALESCE(LD_UserName,'') AS [LD_UserName], " +
            "[LD_UserPhoto] AS [LD_UserPhoto], " +
            "COALESCE(LD_IsLogin,'0') AS [LD_IsLogin], " +
            "COALESCE(LD_CreationDate,'') AS [LD_CreationDate], " +
            "COALESCE(LD_UpdationDate,'') AS [LD_UpdationDate], " +
            "COALESCE(LD_CountryCode,'') AS [LD_CountryCode], " +
            "COALESCE(LD_MobileNo,'') AS [LD_MobileNo] " +
            "FROM " + CS_SQLiteTable.VMS_LogIn_Details + " " +
            "ORDER BY LD_Auto_Id DESC LIMIT 1")
    CS_Entity_LoginDetails getLoginDetails();

    // For updating specific data only base on id
    @Query("UPDATE " + CS_SQLiteTable.VMS_LogIn_Details + " SET LD_IsLogin = 0")
    int logout();

    // For updating specific data only base on id
    @Query("UPDATE " + CS_SQLiteTable.VMS_LogIn_Details + " SET LD_IsLogin = :isLogin")
    int setIsLogin(int isLogin);

    // Update password
    @Query("UPDATE " + CS_SQLiteTable.VMS_LogIn_Details + " SET LD_Password = :newPassword WHERE LD_SourceId = :whereColumnId")
    int updatePassword(String whereColumnId, String newPassword);

    // Update mobile no.
    @Query("UPDATE " + CS_SQLiteTable.VMS_LogIn_Details  + " SET " +
            "LD_CountryCode = :countryCode, " +
            "LD_MobileNo = :mobileNo " +
            "WHERE LD_SourceId = :whereColumnId")
    int updateMobileNo(String whereColumnId, String countryCode, String mobileNo);

    // JOIN
    @Query("SELECT * FROM "+CS_SQLiteTable.VMS_LogIn_Details+" A LEFT OUTER JOIN "+CS_SQLiteTable.VMS_Access_Details+" B ON (A.LD_SourceId=B.AD_SourceId)")
    CS_LoginDetailsWithAccessDetails getLoginDetailsWithAccessDetails();

}
