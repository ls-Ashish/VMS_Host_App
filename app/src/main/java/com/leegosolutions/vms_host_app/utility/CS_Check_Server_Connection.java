package com.leegosolutions.vms_host_app.utility;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;

import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CS_Check_Server_Connection extends AsyncTask<Void, Void, Void> {

    private Context context;
    private String result = "", msg = "";
    private String appToken = "", baseURL = "", bCode = "", tCode = "", clientSecret = "0";
    private byte[] logo = null;
    private ResultListener listener = null;
    private boolean isServerDetailsAvailable = false, dataInserted = false;

    public CS_Check_Server_Connection(Context context, ResultListener listener) {
        try {
            this.context = context;
            this.listener = listener;

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
                bCode = model.getSD_BCode();
                tCode = model.getSD_TCode();
                clientSecret = model.getSD_ClientSecret();
                appToken = model.getSD_AppToken();
                logo = model.getSD_Logo();

                if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {
                    isServerDetailsAvailable = true;
                }
            }

            if (isServerDetailsAvailable) {

                JSONObject jObject = new JSONObject();
                jObject.put("bcode", bCode);
                jObject.put("tcode", tCode);
                jObject.put("client_secret", clientSecret);
                jObject.put("Flag", "Validate_Building");

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

                            if (jsonObject.has("Result") && jsonObject.has("Msg")) {

                                result = jsonObject.getString("Result");
                                msg = jsonObject.getString("Msg");

                                if (result.equals("1")) {
                                    String bu_Id = jsonObject.getString("BU_Id");
                                    String te_Id = jsonObject.getString("TE_Id");
                                    String buildingName = jsonObject.getString("BU_BuildingName");
                                    String tenantName = jsonObject.getString("TenantName");
                                    String errorPostingURL = jsonObject.getString("ErrorPostingURL");
                                    String buildingCountry = jsonObject.getString("Country");
                                    String buildingAddressLine_1 = jsonObject.getString("AddressLine_1");
                                    String buildingAddressLine_2 = jsonObject.getString("AddressLine_2");

                                    byte[] attachedPhoto = null;
                                    String base64_Image = jsonObject.getString("AttachedPhoto");
                                    if (!base64_Image.equals("null") && !base64_Image.equals("")) {
                                        attachedPhoto = Base64.decode(base64_Image, Base64.NO_WRAP);
                                    }

                                    // Update
                                    dataInserted = new CS_Action_ServerDetails(context).updateServerDetails(bu_Id, te_Id, buildingName, tenantName, attachedPhoto, errorPostingURL, new CS_Utility(context).getDateTime(), buildingCountry, buildingAddressLine_1, buildingAddressLine_2);
                                }

                            } else if (jsonObject.has("code") && jsonObject.has("Msg")) {

                                result = jsonObject.getString("code");
                                msg = jsonObject.getString("Msg");

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            msg = e.toString();
            new CS_Utility(context).saveError(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        try {
            if (listener != null) {
                listener.connectionStatus(result, msg, dataInserted);

            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public interface ResultListener {
        void connectionStatus(String result, String msg, boolean dataInserted);
    }

}
