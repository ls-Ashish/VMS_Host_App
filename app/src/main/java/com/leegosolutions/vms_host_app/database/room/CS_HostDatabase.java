package com.leegosolutions.vms_host_app.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_AccessDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_EmailDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_SMSDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.database.migrations.CS_Migrations;

@Database(entities = {
        CS_Entity_ServerDetails.class,
        CS_Entity_LoginDetails.class,
        CS_Entity_AccessDetails.class,
        CS_Entity_EmailDetails.class,
        CS_Entity_SMSDetails.class

}, version = 4)
public abstract class CS_HostDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "vmshost_db";
    private static CS_HostDatabase instance;

    public abstract CS_Dao_ServerDetails serverDetails_Dao();
    public abstract CS_Dao_LoginDetails loginDetails_Dao();
    public abstract CS_Dao_AccessDetails accessDetails_Dao();
    public abstract CS_Dao_EmailDetails emailDetails_Dao();
    public abstract CS_Dao_SMSDetails smsDetails_Dao();

    public static synchronized CS_HostDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CS_HostDatabase.class, DATABASE_NAME)
                            .addMigrations(CS_Migrations.VERSION_2, CS_Migrations.VERSION_3, CS_Migrations.VERSION_4)
//                            .addCallback(roomCallback)
                            .build();
        }
        return instance;
    }

    // below line is to create a callback for our room database.
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            // this method is called when database is created and below line is to populate our data.
//        }
//    };
}
