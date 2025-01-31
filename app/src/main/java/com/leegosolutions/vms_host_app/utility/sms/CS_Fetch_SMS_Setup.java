package com.leegosolutions.vms_host_app.utility.sms;

import android.content.Context;
import android.os.AsyncTask;

import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_EmailDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_SMSDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.email.CS_Fetch_Email_Setup;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CS_Fetch_SMS_Setup extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", appToken = "", logged_BU_Id = "", logged_TE_Id = "", resultMessage = "";
    private OnUpdateCompleted listener;
    private boolean status = false;

    public CS_Fetch_SMS_Setup(Context context, String baseURL, String bCode, String tCode, String clientSecret, String appToken, String logged_BU_Id, String logged_TE_Id, OnUpdateCompleted listener) {
        try {
            this.context = context;
            this.baseURL = baseURL;
            this.bCode = bCode;
            this.tCode = tCode;
            this.clientSecret = clientSecret;
            this.appToken = appToken;
            this.logged_BU_Id = logged_BU_Id;
            this.logged_TE_Id = logged_TE_Id;
            this.listener = listener;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    protected Void doInBackground(Void... strings) {
        // CLean
        new CS_Action_SMSDetails(context).deleteSMSDetails();
        try {
            if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {

                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                JSONObject jObject = new JSONObject();
                jObject.put("bcode", bCode);
                jObject.put("tcode", tCode);
                jObject.put("client_secret", clientSecret);
                jObject.put("Flag", "SMS_Setup");
                jObject.put("Logged_BU_Id", logged_BU_Id);
                jObject.put("Logged_TE_Id", logged_TE_Id);

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("Id", "0")
                        .addFormDataPart("Json_Data", String.valueOf(jObject))
                        .addFormDataPart("App_Token", appToken)
                        .build();

                Request request = new Request.Builder()
                        .url(baseURL + CS_API_URL.Connection)
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

                            String result = jsonObject.getString("Result");
                            String msg = jsonObject.getString("Msg");

                            if (result.equals("1")) {
                                String platform = jsonObject.getString("Platform");
                                String accountNo = jsonObject.getString("AccountNo");
                                String tokenNo = jsonObject.getString("TokenNo");
                                String serviceNo = jsonObject.getString("ServiceNo");
                                String sender = jsonObject.getString("Sender");
                                String urlCode = jsonObject.getString("UrlCode");
                                String expiryTime = jsonObject.getString("ExpiryTime");

                                // Save
                                CS_Entity_SMSDetails model = new CS_Entity_SMSDetails(CS_ED.Encrypt(platform), CS_ED.Encrypt(accountNo), CS_ED.Encrypt(tokenNo), CS_ED.Encrypt(serviceNo), CS_ED.Encrypt(sender), CS_ED.Encrypt(urlCode), CS_ED.Encrypt(expiryTime), "", "", "", "", "", "", new CS_Utility(context).getDateTime(), "");
                                status = new CS_Action_SMSDetails(context).insertSMSDetails(model);

                            }
                        }
                    }
                }

            }

        } catch (Exception e) {

            status = false;
            resultMessage = e.toString();

            new CS_Utility(context).saveError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        try {
            if (listener != null) {
                listener.OnUpdateCompleted(status, resultMessage);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public interface OnUpdateCompleted {
        void OnUpdateCompleted(boolean status, String message);
    }

}
