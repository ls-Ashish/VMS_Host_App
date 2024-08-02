package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_Action_AccessDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_AccessDetails dao;

    public CS_Action_AccessDetails(Context context) {
        try {
            this.context = context;
            database = CS_HostDatabase.getInstance(context);
            dao = database.accessDetails_Dao();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Insert
    public boolean insertAccessDetails(CS_Entity_AccessDetails model) {
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
    public void deleteAccessDetails() {
        try {
            dao.deleteAllAccessDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Fetch
    public CS_Entity_AccessDetails getAccessDetails() {
        CS_Entity_AccessDetails model = new CS_Entity_AccessDetails();
        try {
            CS_Entity_AccessDetails fetchedModel = dao.getAccessDetails();
            if (fetchedModel != null) {
                model = fetchedModel;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return model;
    }

    // Update
    public boolean updateAccessDetails(String whereColumnId, String AD_E_No, String AD_E_Name, String AD_E_Unit, String AD_E_VehicleNo, String AD_E_UpdationDate, String AD_E_No_Encrypted, String AD_E_FloorUnit) {
        boolean result = false;
        try {
            long rowId = dao.updateAccessDetails(whereColumnId, AD_E_No, AD_E_Name, AD_E_Unit, AD_E_VehicleNo, AD_E_UpdationDate, AD_E_No_Encrypted, AD_E_FloorUnit);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

}
