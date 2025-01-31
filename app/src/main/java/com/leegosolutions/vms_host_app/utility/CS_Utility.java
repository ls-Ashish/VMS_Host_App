package com.leegosolutions.vms_host_app.utility;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.provider.Settings;
import android.widget.Toast;

import com.leegosolutions.vms_host_app.BuildConfig;
import com.leegosolutions.vms_host_app.database.action.CS_Action;
import com.leegosolutions.vms_host_app.database.action.CS_Action_EmailDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_SMSDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.model.CS_VisitorDetailsModel;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
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
            saveError(e);
        }
        return result;
    }

    public void showToast(String message, int length) {
        try {
            Toast.makeText(context, message, length).show();

        } catch (Exception ignored) {
        }
    }

    public void saveError(Exception e) {
        try {
            if (BuildConfig.DEBUG) {
                showToast(e.toString(), 0);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String stackTrace = sw.toString();

            String e_ClassName = context.getClass().getSimpleName();
            String e_MethodName = new Object() {
            }.getClass().getEnclosingMethod().getName();
            String e_LineNo = String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber());

            e_LineNo = e_LineNo;

        } catch (Exception ignored) {}
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
            saveError(e);
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
            saveError(e);
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
            saveError(e);
        } finally {
            try {
                if (client == null) {
                    client = new OkHttpClient().newBuilder()
                            .build();
                }
            } catch (Exception e) {
                saveError(e);
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
            saveError(e);
        }
        return result;
    }

    public boolean containsUpperCase(String str) {
        try {
            for (char c : str.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    return true;
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return false;
    }

    public boolean containsLowerCase(String str) {
        try {
            for (char c : str.toCharArray()) {
                if (Character.isLowerCase(c)) {
                    return true;
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return false;
    }

    public boolean containsNumber(String str) {
        try {
            for (char c : str.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true;
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return false;
    }

    public boolean containsSpecificSymbols(String str, String symbols) {
        try {
            for (char symbol : symbols.toCharArray()) {
                if (str.contains(String.valueOf(symbol))) {
                    return true;
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return false;
    }

    public boolean checkEmailSetUpBuildingWise() {
        boolean result = false;
        try {
            // Building Wise
            CS_Entity_EmailDetails model = new CS_Action_EmailDetails(context).getEmailDetails("Building_Wise");
            if (model != null) {

                String emailId = CS_ED.Decrypt(model.getED_EmailId());
                String password = CS_ED.Decrypt(model.getED_Password());
                String server = CS_ED.Decrypt(model.getED_Server());
                String port = CS_ED.Decrypt(model.getED_Port());
                String enableSSL = model.getED_EnableSSL();

                if (!server.isEmpty() && !port.isEmpty() && !emailId.isEmpty() && !password.isEmpty()) {
                    result = true;
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    public boolean checkDefaultEmail() {
        boolean result = false;
        try {
            // Default
            CS_Entity_EmailDetails model = new CS_Action_EmailDetails(context).getEmailDetails("Default");
            if (model != null) {

                String emailId = CS_ED.Decrypt(model.getED_EmailId());
                String password = CS_ED.Decrypt(model.getED_Password());
                String server = CS_ED.Decrypt(model.getED_Server());
                String port = CS_ED.Decrypt(model.getED_Port());
                String enableSSL = model.getED_EnableSSL();

                if (!server.isEmpty() && !port.isEmpty() && !emailId.isEmpty() && !password.isEmpty()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    public boolean checkSMSSettings() {
        boolean result = false;
        try {
            CS_Entity_SMSDetails model = new CS_Action_SMSDetails(context).getSMSDetails();
            if (model != null) {
                String platform = CS_ED.Decrypt(model.getSD_Platform());
                String accountNo = CS_ED.Decrypt(model.getSD_AccountNo());
                String tokenNo = CS_ED.Decrypt(model.getSD_TokenNo());
                String sender = CS_ED.Decrypt(model.getSD_Sender());
                String urlCode = CS_ED.Decrypt(model.getSD_UrlCode());
                String serviceNo = CS_ED.Decrypt(model.getSD_ServiceNo());
                String platform_WB = CS_ED.Decrypt(model.getSD_Platform_WB());
                String templateID = CS_ED.Decrypt(model.getSD_P_WB_TemplateID());
                String object_1 = CS_ED.Decrypt(model.getSD_P_WB_Object_1());
                String object_2 = CS_ED.Decrypt(model.getSD_P_WB_Object_2());
                String object_3 = CS_ED.Decrypt(model.getSD_P_WB_Object_3());
                String object_4 = CS_ED.Decrypt(model.getSD_P_WB_Object_4());

                if (!platform.isEmpty() && !accountNo.isEmpty() && !tokenNo.isEmpty() && !sender.isEmpty() && !urlCode.isEmpty()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    public boolean isValidEmail(String email) {
        boolean result = false;
        try {
            result =  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    public String getAndroidId() {
        String androidID = "";
        try {
            // Get
            SharedPreferences prefs = context.getSharedPreferences(CS_SharedPreferences.SHP_NAME, MODE_PRIVATE);
            androidID = prefs.getString(CS_SharedPreferences.SHP_ANDROID_ID, "");
            if (androidID.equals("")) {
                androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

                // Set
                SharedPreferences.Editor editor = context.getSharedPreferences(CS_SharedPreferences.SHP_NAME, MODE_PRIVATE).edit();
                editor.putString(CS_SharedPreferences.SHP_ANDROID_ID, androidID);
                editor.apply();
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return androidID;
    }

    public String capitalizeFirstLetter(String text) {
        String androidID = "";
        try {
            if (text == null || text.isEmpty()) {
                return text;
            }
            return text.substring(0, 1).toUpperCase() + text.substring(1);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return androidID;
    }

}
