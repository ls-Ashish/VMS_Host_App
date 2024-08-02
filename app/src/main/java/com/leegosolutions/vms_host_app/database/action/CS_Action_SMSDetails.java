package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_EmailDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_SMSDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_SMSDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_SMSDetails dao;

    public CS_Action_SMSDetails(Context context) {
        try {
            this.context = context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.smsDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Insert
    public boolean insertSMSDetails(CS_Entity_SMSDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertSMSDetails(model);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Delete
    public void deleteSMSDetails() {
        try {
            dao.deleteAllSMSDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Fetch
    public CS_Entity_SMSDetails getSMSDetails() {
        CS_Entity_SMSDetails model = new CS_Entity_SMSDetails();
        try {
            CS_Entity_SMSDetails fetchedModel = dao.getSMSDetails();
            if (fetchedModel != null) {
                model = fetchedModel;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return model;
    }
}
