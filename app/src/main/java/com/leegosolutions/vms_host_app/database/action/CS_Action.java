package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action {

    private Context context;
    private CS_HostDatabase database;

    public CS_Action(Context context) {
        try {
            this.context=context;
            database = CS_HostDatabase.getInstance(context);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);        }
    }

    public String getDatabaseVersion() {
        String version = "";
        try {
            version = String.valueOf(database.getOpenHelper().getReadableDatabase().getVersion());

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);        }
        return version;
    }


}
