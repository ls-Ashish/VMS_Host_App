package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_ServerDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_ServerDetails dao;

    public CS_Action_ServerDetails(Context context) {
        try {
            this.context = context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.serverDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Insert
    public boolean insertServerDetails(CS_Entity_ServerDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertServerDetails(model);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Delete
    public void deleteServerDetails() {
        try {
            dao.deleteAllServerDetails();
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Fetch
    public CS_Entity_ServerDetails getServerDetails() {
        CS_Entity_ServerDetails model = new CS_Entity_ServerDetails();
        try {
            CS_Entity_ServerDetails fetchedModel = dao.getServerDetails();
            if (fetchedModel != null) {
                model = fetchedModel;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return model;
    }

    // Update server details
    public boolean updateServerDetails(String where_Building_Id, String where_Tenant_Id, String buildingName, String tenantName, byte[] logo, String errorPostingURL, String updationDate, String country, String address_Line_1, String address_Line_2) {
        boolean result = false;
        try {
            long rowId = dao.updateServerDetails(where_Building_Id, where_Tenant_Id, buildingName, tenantName, logo, errorPostingURL, updationDate, country, address_Line_1, address_Line_2);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

}
