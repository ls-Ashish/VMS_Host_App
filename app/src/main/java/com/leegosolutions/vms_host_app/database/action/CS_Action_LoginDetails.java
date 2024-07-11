package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_LoginDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_LoginDetails dao;

    public CS_Action_LoginDetails(Context context) {
        this.context=context;
        database = CS_HostDatabase.getInstance(context);
        dao = database.loginDetails_Dao();
    }

    // Insert
    public boolean insertLoginDetails(CS_Entity_LoginDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertLoginDetails(model);
            result = rowId != -1;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    // Fetch
    public CS_Entity_LoginDetails getLoginDetails() {
        CS_Entity_LoginDetails model = null;
        try {
            model = dao.getLoginDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return model;
    }

    // Delete
    public void deleteLoginDetails() {
        try {
            dao.deleteAllLoginDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}
