package com.leegosolutions.vms_host_app.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.utility.sms.CS_SendSMS;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CS_Update_Login_Details extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String result = "", msg = "", isLogin = "", deviceInfo = "";
    private String appToken = "", baseURL = "", buildingId = "", tenantId = "", hostId = "0", hostType = "";
    private ResultListener listener = null;

    public CS_Update_Login_Details(Context context, ResultListener listener, String isLogin, String deviceInfo, String hostId, String hostType) {
        try {
            this.context = context;
            this.listener = listener;
            this.isLogin = isLogin;
            this.deviceInfo = deviceInfo;
            this.hostId = hostId;
            this.hostType = hostType;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            OkHttpClient client = new CS_Utility(context).getOkHttpClient();

            CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();
            if (model != null) {
                baseURL = CS_ED.Decrypt(model.getSD_BaseURL());
                appToken = model.getSD_AppToken();
                buildingId = model.getSD_BU_ID();
                tenantId = model.getSD_TE_ID();
            }

            if (!baseURL.equals("")) {

                JSONObject jObject = new JSONObject();
                jObject.put("CountryCode", "");
                jObject.put("MobileNo", "");
                jObject.put("Password", "");
                jObject.put("Logged_BU_Id", buildingId);
                jObject.put("Logged_TE_Id", tenantId);
                jObject.put("Flag", "Save_HostApp_Login_Details");
                jObject.put("Logged_User_Id", hostId);
                jObject.put("Logged_User_Type", hostType);
                jObject.put("IsLogin", isLogin);
                jObject.put("DeviceId", new CS_Utility(context).getAndroidId());
                jObject.put("DeviceInfo", Build.MANUFACTURER + "§" + Build.MODEL);

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Id", "0")
                        .addFormDataPart("Json_Data", String.valueOf(jObject))
                        .addFormDataPart("App_Token", appToken)
                        .build();

                Request request = new Request.Builder()
                        .url(baseURL + CS_API_URL.Settings)
                        .method("POST", body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", "Bearer " + appToken)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    if (response != null) {
                        String responseBody = response.body().string();
                        if (!responseBody.equals("")) {

                            String jsonData = responseBody;
                            JSONArray jsonArray = new JSONArray(jsonData);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            result = jsonObject.getString("Result");
                            msg = jsonObject.getString("Msg");

                        }
                    }
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        try {
            if (listener != null) {
                listener.status(result, msg);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public interface ResultListener {
        void status(String result, String msg);
    }

}
