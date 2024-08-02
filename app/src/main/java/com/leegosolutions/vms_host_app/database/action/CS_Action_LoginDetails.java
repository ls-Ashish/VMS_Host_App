package com.leegosolutions.vms_host_app.database.action;

import android.content.Context;

import com.leegosolutions.vms_host_app.database.join.CS_LoginDetailsWithAccessDetails;
import com.leegosolutions.vms_host_app.database.room.CS_HostDatabase;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.List;

public class CS_Action_LoginDetails {

    private Context context;
    private CS_HostDatabase database;
    private CS_Dao_LoginDetails dao;

    public CS_Action_LoginDetails(Context context) {
        this.context = context;
        database = CS_HostDatabase.getInstance(context);
        dao = database.loginDetails_Dao();
    }

    // Insert
    public boolean insertLoginDetails(CS_Entity_LoginDetails model) {
        boolean result = false;
        try {
            long rowId = dao.insertLoginDetails(model);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Delete
    public void deleteLoginDetails() {
        try {
            dao.deleteAllLoginDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    // Fetch
    public CS_Entity_LoginDetails getLoginDetails() {
        CS_Entity_LoginDetails model = new CS_Entity_LoginDetails();
        try {
            CS_Entity_LoginDetails fetchedModel = dao.getLoginDetails();
            if (fetchedModel != null) {
                model = fetchedModel;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return model;
    }

    // Logout
    public boolean logout() {
        boolean result = false;
        try {
            long rowId = dao.logout();
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Logout
    public boolean setIsLogin(int isLogin) {
        boolean result = false;
        try {
            long rowId = dao.setIsLogin(isLogin);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Update password
    public boolean updatePassword(String whereColumnId, String newPassword) {
        boolean result = false;
        try {
            long rowId = dao.updatePassword(whereColumnId, newPassword);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Update password
    public boolean updateMobileNo(String whereColumnId, String countryCode, String mobileNo) {
        boolean result = false;
        try {
            long rowId = dao.updateMobileNo(whereColumnId, countryCode, mobileNo);
            result = rowId > 0;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    // Update password
    public CS_LoginDetailsWithAccessDetails getLoginDetailsWithAccessDetails() {
        CS_LoginDetailsWithAccessDetails list = null;
        try {
            list = dao.getLoginDetailsWithAccessDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return list;
    }

}
