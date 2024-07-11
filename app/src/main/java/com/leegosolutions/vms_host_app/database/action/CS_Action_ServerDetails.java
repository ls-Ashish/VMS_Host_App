package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_ServerDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_ServerDetails dao;

    public CS_Action_ServerDetails(Context context) {
        try {
            this.context=context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.serverDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    // Insert
    public boolean insertServerDetails(CS_Entity_ServerDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertServerDetails(model);
            result = rowId != -1;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    // Delete
    public void deleteServerDetails() {
        dao.deleteAllServerDetails();
    }

    // Fetch
    public CS_Entity_ServerDetails getServerDetails() {
        CS_Entity_ServerDetails model = null;
        try {
            model = dao.getServerDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return model;
    }

}
