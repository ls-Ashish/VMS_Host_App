package com.leegosolutions.vms_host_app.utility.sms;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.database.action.CS_Action_SMSDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_SMSDetails;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CS_SendSMS {

    private Context context;
    private String mobileNo = "", message = "", ph_Value_1 = "", ph_Value_2 = "", ph_Value_3 = "", ph_Value_4 = "";
    String accountNo = "", tokenNo = "", sender = "", urlCode = "", platform = "", serviceNo = "", platform_WB = "0", templateID = "", object_1 = "", object_2 = "", object_3 = "", object_4 = "", smsUrl = "";
    private ResultListener listener = null;

    public CS_SendSMS(Context context) {
        try {
            this.context = context;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public CS_SendSMS(Context context, ResultListener listener) {
        try {
            this.context = context;
            this.listener = listener;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void sendSMS(String mobileNo, String message) {
        try {
            this.mobileNo = mobileNo;
            this.message = message;

            send();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void sendSMS_WB(String mobileNo, String message, String ph_Value_1, String ph_Value_2, String ph_Value_3, String ph_Value_4) {
        try {
            this.mobileNo = mobileNo;
            this.message = message;
            this.ph_Value_1 = ph_Value_1;
            this.ph_Value_2 = ph_Value_2;
            this.ph_Value_3 = ph_Value_3;
            this.ph_Value_4 = ph_Value_4;

            send();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void send() {
        try {
            new FetchSMSDetailsFromSQLite().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private class FetchSMSDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                CS_Entity_SMSDetails model = new CS_Action_SMSDetails(context).getSMSDetails();
                if (model != null) {
                    platform = CS_ED.Decrypt(model.getSD_Platform());
                    accountNo = CS_ED.Decrypt(model.getSD_AccountNo());
                    tokenNo = CS_ED.Decrypt(model.getSD_TokenNo());
                    sender = CS_ED.Decrypt(model.getSD_Sender());
                    urlCode = CS_ED.Decrypt(model.getSD_UrlCode());
                    serviceNo = CS_ED.Decrypt(model.getSD_ServiceNo());
                    platform_WB = CS_ED.Decrypt(model.getSD_Platform_WB());
                    templateID = CS_ED.Decrypt(model.getSD_P_WB_TemplateID());
                    object_1 = CS_ED.Decrypt(model.getSD_P_WB_Object_1());
                    object_2 = CS_ED.Decrypt(model.getSD_P_WB_Object_2());
                    object_3 = CS_ED.Decrypt(model.getSD_P_WB_Object_3());
                    object_4 = CS_ED.Decrypt(model.getSD_P_WB_Object_4());

                    // Platform 2
                    if (platform.equals("Platform 2")) {
                        if (urlCode.equals("SG")) {
                            // 10002 (Old)
//                    smsUrl = "http://gateway.onewaysms.sg:"+urlCode+"/api.aspx?apiusername="+accountNo+"&apipassword="+tokenNo+"&mobileno="+mobileNo+"&senderid="+sender+"&languagetype=1&message="+message+"";

                            smsUrl = "https://gateway.onewaysms.sg/api.aspx?apiusername=" + accountNo + "&apipassword=" + tokenNo + "&mobileno=" + mobileNo + "&senderid=" + sender + "&languagetype=1&message=" + message + "";

                        } else if (urlCode.equals("MY")) {
                            smsUrl = "http://gateway80.onewaysms.com.my/api2.aspx?apiusername=" + accountNo + "&apipassword=" + tokenNo + "&mobileno=" + mobileNo + "&senderid=" + sender + "&languagetype=1&message=" + message + "";
                        }

                    } else if (platform.equals("Platform 3")) { // Platform 3
                        // Changed on 21-Sept-2023-14-16
//                smsUrl = "https://www.commzgate.net/gateway/SendMsg?ID="+accountNo+"&Password="+tokenNo+"&Mobile="+mobileNo+"&Type=A&Message="+message+"&OTP=true";

                        // Platform WhatsApp Business && SMS to host only
                        if (platform_WB.equals("1")) {
                            smsUrl = "https://www.commzgate.net/gateway/SendMessage?ID=" + accountNo + "&Password=" + tokenNo + "&Mobile=" + mobileNo + "&Type=A&Message=" + "" + "&OTP=true"; // message is made empty as template in use

                        } else {
                            smsUrl = "https://www.commzgate.net/gateway/SendMessage?ID=" + accountNo + "&Password=" + tokenNo + "&Mobile=" + mobileNo + "&Type=A&Message=" + message + "&OTP=true";
                        }

                    } else if (platform.equals("Platform 4")) { // Platform 4

                        String ls_TrackId = "LS";
                        try {
                            ls_TrackId = "LS" + new CS_Utility(context).getDateTime().replace("-", "").replace(":", "").replace(".", "").replace(" ", "");

                        } catch (Exception ignored) {
                        }

                        smsUrl = "https://www.sendquickasp.com/client_api/index.php?username=" + accountNo + "&passwd=" + tokenNo + "&callerid=" + serviceNo + "&merchantid=" + sender + "&route_to=api_send_sms" + "&trackid=" + ls_TrackId + "&tar_num=" + mobileNo + "&tar_msg=" + message;
                    }

                    // Platform WhatsApp Business && SMS to host only
                    if (platform_WB.equals("1")) {

                        smsUrl = smsUrl.replace("OTP=true", "OTP=false");
                        smsUrl += "&WhatsApp=true&TemplateID=" + templateID;

                        if (!object_1.equals("")) {
                            smsUrl += "&PlaceHolder1=" + ph_Value_1;
                        }

                        if (!object_2.equals("")) {
                            smsUrl += "&PlaceHolder2=" + ph_Value_2;
                        }

                        if (!object_3.equals("")) {
                            smsUrl += "&PlaceHolder3=" + ph_Value_3;
                        }

                        if (!object_4.equals("")) {
                            smsUrl += "&PlaceHolder4=" + ph_Value_4;
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
                if (!smsUrl.equals("")) {
                    // Send SMS
                    new Send_Sms().execute();

                } else {
                    if (listener != null) {
                        listener.status(context.getResources().getString(R.string.sms_settings_not_available));

                    } else {
                        new CS_Utility(context).showToast(context.getResources().getString(R.string.sms_settings_not_available), 0);
                    }
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public class Send_Sms extends AsyncTask<Void, Void, Void> {

        private String result = "";
        OkHttpClient client = new OkHttpClient();

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Request.Builder builder = new Request.Builder();
                builder.url(smsUrl);
                Request request = builder.build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        result = "Error sending SMS: " + e.getMessage();
                        setCallback(result);
                        new CS_Utility(context).saveError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        try {
                            if (response.isSuccessful()) {
                                String responseBody = response.body().string();
                                try {
                                    String code = responseBody;
                                    if (Double.parseDouble(code) >= 0) {
                                        result = "success";
                                    } else if (Double.parseDouble(code) == -300) {
                                        result = "mobileno parameter is invalid";
                                    } else if (Double.parseDouble(code) == -500) {
                                        result = "Invalid characters in message";
                                    } else if (Double.parseDouble(code) == -600) {
                                        result = "Insufficient credit balance";
                                    }
                                    setCallback(result);
                                } catch (Exception e) {
                                    result = "Error sending SMS: " + e.getMessage();
                                    setCallback(result);
                                    new CS_Utility(context).saveError(e);
                                }
                            }
                        } catch (Exception e) {
                            result = "Error sending SMS: " + e.getMessage();
                            setCallback(result);
                            new CS_Utility(context).saveError(e);
                        }
                    }
                });
            } catch (Exception e) {
                result = "Error sending SMS: " + e.getMessage();
                setCallback(result);
                new CS_Utility(context).saveError(e);
            }
            return null;
        }
    }

    private void setCallback(String result) {
        try {
            if (listener != null) {
                listener.status(result);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public interface ResultListener {
        void status(String status);
    }

}