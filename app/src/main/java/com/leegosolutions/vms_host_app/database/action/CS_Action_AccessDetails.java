package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_AccessDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_AccessDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_AccessDetails dao;

    public CS_Action_AccessDetails(Context context) {
        try {
            this.context=context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.accessDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    // Insert
    public boolean insertAccessDetails(CS_Entity_AccessDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertAccessDetails(model);
            result = rowId != -1;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    // Delete
    public void deleteAccessDetails() {
        dao.deleteAllAccessDetails();
    }
}
