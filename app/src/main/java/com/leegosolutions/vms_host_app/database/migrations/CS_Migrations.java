package com.leegosolutions.vms_host_app.database.migrations;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.leegosolutions.vms_host_app.database.table.CS_SQLiteTable;

public class CS_Migrations {

    public static final Migration VERSION_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_Access_Details +" ADD COLUMN AD_E_No_Encrypted TEXT");
        }
    };

    public static final Migration VERSION_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_Access_Details +" ADD COLUMN AD_E_FloorUnit TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_Server_Details +" ADD COLUMN SD_Country TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_Server_Details +" ADD COLUMN SD_Address_Line_1 TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_Server_Details +" ADD COLUMN SD_Address_Line_2 TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_LogIn_Details +" ADD COLUMN LD_CountryCode TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_LogIn_Details +" ADD COLUMN LD_MobileNo TEXT");

            database.execSQL("CREATE TABLE IF NOT EXISTS " + CS_SQLiteTable.VMS_Email_Details + "(ED_Auto_Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ED_Type TEXT, ED_Password TEXT, ED_EmailId TEXT, ED_Server TEXT, ED_Port TEXT, ED_EnableSSL TEXT, ED_CreationDate TEXT, ED_UpdationDate TEXT)");

            database.execSQL("CREATE TABLE IF NOT EXISTS " + CS_SQLiteTable.VMS_SMS_Details + "(SD_Auto_Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, SD_Platform TEXT, SD_AccountNo TEXT, SD_TokenNo TEXT, SD_ServiceNo TEXT, SD_Sender TEXT, SD_UrlCode TEXT, SD_ExpiryTime TEXT, SD_Platform_WB TEXT, SD_P_WB_TemplateID TEXT, SD_P_WB_Object_1 TEXT, SD_P_WB_Object_2 TEXT, SD_P_WB_Object_3 TEXT, SD_P_WB_Object_4 TEXT, SD_CreationDate TEXT, SD_UpdationDate TEXT)");
        }
    };

    public static final Migration VERSION_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_LogIn_Details +" ADD COLUMN LD_S_PIN_Status TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_LogIn_Details +" ADD COLUMN LD_S_PIN TEXT");
            database.execSQL("ALTER TABLE "+ CS_SQLiteTable.VMS_LogIn_Details +" ADD COLUMN LD_S_Fingerprint_Status TEXT");

        }
    };

//    public static final Migration VERSION_4 = new Migration(4, 5) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//        }
//    };

}
