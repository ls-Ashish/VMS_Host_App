package com.leegosolutions.vms_host_app.utility.email;

import android.content.Context;
import android.os.AsyncTask;

import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_EmailDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_EmailDetails;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CS_Fetch_Email_Setup extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", appToken = "", logged_BU_Id = "", logged_TE_Id = "", resultMessage = "";
    private OnUpdateCompleted listener;
    private boolean defaultEmailStatus = false, emailSetupStatus = false;

    public CS_Fetch_Email_Setup(Context context, String baseURL, String bCode, String tCode, String clientSecret, String appToken, String logged_BU_Id, String logged_TE_Id, OnUpdateCompleted listener) {
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
        new CS_Action_EmailDetails(context).deleteEmailDetails();

        // Default_Email
        try {
            if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {

                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                JSONObject jObject = new JSONObject();
                jObject.put("bcode", bCode);
                jObject.put("tcode", tCode);
                jObject.put("client_secret", clientSecret);
                jObject.put("Flag", "Default_Email");
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
                                String emailId = jsonObject.getString("EmailId");
                                String password = jsonObject.getString("Password");
                                String server = jsonObject.getString("Server");
                                String port = jsonObject.getString("Port");
                                String enableSSL = jsonObject.getString("EnableSSL");

                                // Save
                                CS_Entity_EmailDetails model = new CS_Entity_EmailDetails("Default", CS_ED.Encrypt(emailId), CS_ED.Encrypt(password), CS_ED.Encrypt(server), CS_ED.Encrypt(port), enableSSL, new CS_Utility(context).getDateTime(), "");
                                defaultEmailStatus = new CS_Action_EmailDetails(context).insertEmailDetails(model);

                            }
                        }
                    }
                }

            }

        } catch (Exception e) {

            defaultEmailStatus = false;
            resultMessage = e.toString();

            new CS_Utility(context).saveError(e);
        }
        // Email_Setup
        try {
            if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {

                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                JSONObject jObject = new JSONObject();
                jObject.put("bcode", bCode);
                jObject.put("tcode", tCode);
                jObject.put("client_secret", clientSecret);
                jObject.put("Flag", "Email_Setup");
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
                                String emailId = jsonObject.getString("EmailId");
                                String password = jsonObject.getString("Password");
                                String server = jsonObject.getString("Server");
                                String port = jsonObject.getString("Port");
                                String enableSSL = jsonObject.getString("EnableSSL");

                                // Save
                                CS_Entity_EmailDetails model = new CS_Entity_EmailDetails("Building_Wise", CS_ED.Encrypt(emailId), CS_ED.Encrypt(password), CS_ED.Encrypt(server), CS_ED.Encrypt(port), enableSSL, new CS_Utility(context).getDateTime(), "");
                                emailSetupStatus = new CS_Action_EmailDetails(context).insertEmailDetails(model);

                            }
                        }
                    }
                }

            }

        } catch (Exception e) {

            emailSetupStatus = false;
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
                listener.OnUpdateCompleted(defaultEmailStatus, emailSetupStatus, resultMessage);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public interface OnUpdateCompleted {
        void OnUpdateCompleted(boolean defaultEmailStatus, boolean emailStatus, String message);
    }

}
