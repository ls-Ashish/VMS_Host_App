package com.leegosolutions.vms_host_app.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.leegosolutions.vms_host_app.BuildConfig;
import com.leegosolutions.vms_host_app.database.action.CS_Action;
import com.leegosolutions.vms_host_app.model.CS_VisitorDetailsModel;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import com.leegosolutions.vms_host_app.BuildConfig;

public class CS_Utility {

    private Context context;

    public CS_Utility(Context context) {
        this.context = context;
    }

    // GEt Date and Time in SimpleDateFormat()
    public String getDateTime() {
        String result = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.getDefault());
            Date date = new Date();
            result = dateFormat.format(date);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    public void showToast(String message, int length) {
        try {
            Toast.makeText(context, message, length).show();

        } catch (Exception ignored) {
        }
    }

    public void saveError(Exception e, String sourceClassName, String sourceMethodName, String sourceLineNo) {
        try {
            showToast(e.toString(), 0);
        } catch (Exception ignored) {
        }
    }

    public ArrayList<CS_VisitorsModel> getTestingVisitorsData() {
        ArrayList<CS_VisitorsModel> al_Visitors = new ArrayList<>();
        try {
            // ----------------------Visitor 1---------------------------------------
            CS_VisitorsModel model = new CS_VisitorsModel();

            model.setAppointmentNo("87468");
            model.setName("Steven Lee");
            model.setType("Visitor");
            model.setMobileNo("+65-97731768");
            model.setStartDate("Thu 25/04/2024 12:00 PM");
            model.setEndDate("Thu 25/04/2024 3:00 PM");
            model.setOvernights("-");
            model.setStatus("Active");

            al_Visitors.add(model);

            // ----------------------Visitor 2---------------------------------------
            model = new CS_VisitorsModel();

            model.setAppointmentNo("29836");
            model.setName("Kenneth Ong");
            model.setType("Visitor");
            model.setMobileNo("+65-56731755");
            model.setStartDate("Thu 25/04/2024 12:00 PM");
            model.setEndDate("Fri 26/04/2024 1:59 AM");
            model.setOvernights("-");
            model.setStatus("Active");

            al_Visitors.add(model);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return al_Visitors;
    }

    public CS_VisitorDetailsModel getTestingVisitorsDetails() {
        CS_VisitorDetailsModel model = new CS_VisitorDetailsModel();
        try {
            // ----------------------Visitor 1---------------------------------------

            model.setPageCaption("A15-403");
            model.setUnitName("HiCOMM PTE LTD");
            model.setUserName("John Smith");
            model.setAppointmentNo("87468");
            model.setInvitationTitle("Meeting");
            model.setVisitorName("Steven  Lee");
            model.setCompanyName("HiCOMM PTE LTD");
            model.setVisitDates("Thurs, 25 Apr 2024");
            model.setValidUntil("Fri, 26 Apr 2024 (3:00 PM)");
            model.setOvernight("1 night(s)");
            model.setEntryType("Multiple Entry");
            model.setVehicleNo("SJH6723");
            model.setRemarks("Scan QR Code to access the turnstile.");
            model.setCreatedBy("John Smith");
            model.setCreatedAt("Thurs, 25 Apr 2024, 12:00 PM");
            model.setLastUpdatedBy("Thurs, 25 Apr 2024, 12:14 PM");


        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return model;
    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient client = null;
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));

        } finally {
            try {
                if (client == null) {
                    client = new OkHttpClient().newBuilder()
                            .build();
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
        return client;
    }

    public String encode(String data) {
        String result = "";
        try {
            result = URLEncoder.encode(data, "utf-8");
        } catch (Exception e) {
            result = data;
        }
        return result;
    }

    public String appVersion() {
        String result = "";
        try {
            // Version name
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String releaseDate = CS_VersionDetails.appVersion;
            String version = pInfo.versionName;
            String databaseVersion = new CS_Action(context).getDatabaseVersion() + ".";
            String versionToDisplay = releaseDate + databaseVersion + version;
            // release or debug
            if (BuildConfig.DEBUG) {
                // do something for a debug build
                versionToDisplay += " (D)";
            }
            result = versionToDisplay;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

}
