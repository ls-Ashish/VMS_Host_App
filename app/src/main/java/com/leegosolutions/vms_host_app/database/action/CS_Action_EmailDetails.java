package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_AccessDetails;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_EmailDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_EmailDetails dao;

    public CS_Action_EmailDetails(Context context) {
        try {
            this.context = context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.emailDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Insert
    public boolean insertEmailDetails(CS_Entity_EmailDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertAccessDetails(model);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Delete
    public void deleteEmailDetails() {
        try {
            dao.deleteAllEmailDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Fetch
    public CS_Entity_EmailDetails getEmailDetails(String emailType) {
        CS_Entity_EmailDetails model = new CS_Entity_EmailDetails();
        try {
            CS_Entity_EmailDetails fetchedModel = dao.getEmailDetails(emailType);
            if (fetchedModel != null) {
                model = fetchedModel;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return model;
    }

}
