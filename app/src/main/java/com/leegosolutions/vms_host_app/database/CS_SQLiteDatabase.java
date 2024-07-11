//package com.leegosolutions.vms_host_app.database;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import com.leegosolutions.vms_host_app.utility.CS_App;
//import com.leegosolutions.vms_host_app.utility.CS_Utility;
//
//public class CS_SQLiteDatabase extends SQLiteOpenHelper {
//
//    private Context context;
//    private static CS_SQLiteDatabase mInstance = null;
//    private static final String DATABASE_NAME = "vmshost_db";
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String VMS_Server_Details = "CREATE TABLE " + CS_SQLiteTable.VMS_Server_Details + " (SD_Auto_Id INTEGER PRIMARY KEY AUTOINCREMENT, SD_BaseURL TEXT, SD_BU_ID TEXT, SD_TE_ID TEXT, SD_BCode TEXT, SD_TCode TEXT, SD_ClientSecret TEXT, SD_BuildingName TEXT, SD_TenantName TEXT, SD_AppToken TEXT, SD_ErrorPostingURL TEXT, SD_CreationDate TEXT, SD_UpdationDate TEXT, SD_Status TEXT)";
//
//    /**
//     * constructor should be private to prevent direct instantiation.
//     * make call to static factory method "getInstance()" instead.
//     */
//    private CS_SQLiteDatabase(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        try {
//            this.context = context;
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
//        }
//    }
//
//    /**
//     * use the application context as suggested by CommonsWare.
//     * this will ensure that you dont accidentally leak an Activitys
//     * context (see this article for more information:
//     * <a href="http://android-developers.blogspot.nl/2009/01/avoiding-memory-leaks.html">...</a>)
//     */
//    public static CS_SQLiteDatabase getInstance() {
//        try {
//            if (mInstance == null) {
//                try {
//                    mInstance = new CS_SQLiteDatabase(CS_App.context);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (Exception ignored) {
//            // todo - check to save
//        }
//
//        return mInstance;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        try {
//            db.execSQL(VMS_Server_Details);
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
//        }
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < newVersion) {
//        }
//    }
//
//}
