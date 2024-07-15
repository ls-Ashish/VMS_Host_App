package com.leegosolutions.vms_host_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.ActivityScanServerDetailsBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.camera.CameraXScanner;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanServerDetails extends AppCompatActivity {

    private final Context context = ScanServerDetails.this;
    private ActivityScanServerDetailsBinding viewBinding;
    private final int LAUNCH_SCANNING_ACTIVITY = 1;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", bu_Id = "", te_Id = "", buildingName = "", tenantName = "", errorPostingURL = "", appToken = "";
    private byte[] attachedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_server_details);
        try {
            viewBinding = ActivityScanServerDetailsBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

//            startAnimation();
            on_Click_Scan_button();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void startAnimation() {
        try {
            viewBinding.lavImage.setAnimation("qr_code_scanner.json");
            viewBinding.lavImage.loop(true);
            viewBinding.lavImage.playAnimation();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Scan_button() {
        viewBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startScanningActivityForResult();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                    }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                }
            }
        });
    }

    private void startScanningActivityForResult() {
        try {
            Intent intent = new Intent(this, CameraXScanner.class);
            startActivityForResult(intent, LAUNCH_SCANNING_ACTIVITY);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == LAUNCH_SCANNING_ACTIVITY) {
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra("result");
                    if (result != null) {
                        if (!result.isEmpty()) {
                            // Connect Server
                            readQRCode(result);

                        } else {
                            new CS_Utility(context).showToast(context.getResources().getString(R.string.scan_server_details_scan_error_empty), 1);
                        }
                    } else {
                        new CS_Utility(context).showToast(context.getResources().getString(R.string.scan_server_details_scan_error_null), 1);
                    }
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    // Write your code if there's no result
                    new CS_Utility(context).showToast("Canceled", 1);
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void readQRCode(String result) {
        try {
            // --------------------------------------------------------API----------------------------------------------------------------
            // BaseURL
            int startBu = result.indexOf("<mbu>");
            int endBu = result.indexOf("</mbu>");

            if (!String.valueOf(startBu).equals("-1") && !String.valueOf(endBu).equals("-1") && startBu <= endBu) {
                baseURL = result.substring(startBu + 5, endBu).trim();
                baseURL = CS_ED.Decrypt(baseURL);
            }

            // API - BCode
            int startBcode = result.indexOf("<mbc>");
            int endBcode = result.indexOf("</mbc>");

            if (!String.valueOf(startBcode).equals("-1") && !String.valueOf(endBcode).equals("-1") && startBcode <= endBcode) {
                bCode = result.substring(startBcode + 5, endBcode).trim();
            }

            // API - TCode
            int startTCode = result.indexOf("<mtc>");
            int endTcode = result.indexOf("</mtc>");

            if (!String.valueOf(startTCode).equals("-1") && !String.valueOf(endTcode).equals("-1") && startTCode <= endTcode) {
                tCode = result.substring(startTCode + 5, endTcode).trim();
            }

            // API - ClientSecret
            int startCs = result.indexOf("<mcs>");
            int endTCs = result.indexOf("</mcs>");

            if (!String.valueOf(startCs).equals("-1") && !String.valueOf(endTCs).equals("-1") && startCs <= endTCs) {
                clientSecret = result.substring(startCs + 5, endTCs).trim();
            }

            // --------------------------------------------------------API----------------------------------------------------------------

            // Connect Server
//            if (!str_Ip.equals("") && !str_DatabaseName.equals("") && !str_Db_UserName.equals("") && !str_Db_Password.equals("") && !str_Db_BuildingIdName.equals("")) {
            if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {
                connect();

            } else {
                showAlertDialog("Invalid QR Code");
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    public void showAlertDialog(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setIcon(R.drawable.ic_error);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("OK".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    public void connect() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new ConnectServer().execute();

            } else {
                showSnackbar();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                connect();

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                            }
                        }
                    });
            snackbar.show();


        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    class ConnectServer extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private boolean dataInserted = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressdialog = new ProgressDialog(context);
                progressdialog.setCancelable(false);
                progressdialog.setMessage("Please wait...");
                progressdialog.show();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!bCode.equals("") && !tCode.equals("") && !clientSecret.equals("") && !baseURL.equals("")) {

                    OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                    JSONObject jObject = new JSONObject();
                    jObject.put("bcode", bCode);
                    jObject.put("tcode", tCode);
                    jObject.put("client_secret", clientSecret);
                    jObject.put("Flag", "Validate_Building");

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id","0")
                            .addFormDataPart("Json_Data",String.valueOf(jObject))
                            .addFormDataPart("App_Token",appToken)
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

                                result = jsonObject.getString("Result");
                                msg = jsonObject.getString("Msg");

                                if (result.equals("1")) {
                                    bu_Id = jsonObject.getString("BU_Id");
                                    te_Id = jsonObject.getString("TE_Id");
                                    buildingName = jsonObject.getString("BU_BuildingName");
                                    tenantName = jsonObject.getString("TenantName");
                                    errorPostingURL = jsonObject.getString("ErrorPostingURL");

                                    String base64_Image = jsonObject.getString("AttachedPhoto");
                                    if (!base64_Image.equals("null") && !base64_Image.equals("")) {
                                        attachedPhoto = Base64.decode(base64_Image, Base64.NO_WRAP);
                                    }

                                    // Generate New App Token
//                                    try {
//                                        client = new CS_Utility(context).getOkHttpClient();
//                                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//
//                                        body = RequestBody.create(mediaType, "bcode=" + CS_ED.Decrypt(bCode)
//                                                        + "&tcode=" + CS_ED.Decrypt(tCode)
//                                                        + "&client_secret=" + CS_ED.Decrypt(clientSecret)
////                                            + "&redirect_uri=" + CS_ED.Decrypt(redirectURL)
////                                            + "&deviceId=" + deviceId)
//                                        );
//
//                                        request = new Request.Builder()
//                                                .url(baseURL + CS_API_URL.token)
//                                                .method("POST", body)
//                                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                                                .build();
//
//                                        response = client.newCall(request).execute();
//                                        if (response.isSuccessful()) {
//                                            if (response != null) {
//                                                responseBody = response.body().string();
//                                                if (!responseBody.equals("")) {
//
//                                                    jsonData = responseBody;
//                                                    jsonObject = new JSONObject(jsonData);
//
//                                                    String tokenCode = jsonObject.getString("code");
//                                                    String tokenMsg = jsonObject.getString("msg");
//
//                                                    if (tokenCode.equals("200") && tokenMsg.equals("Success")) {
//                                                        appToken = jsonObject.getString("appToken");
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
//                                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
//                                    }
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            try {
                if (result.equals("1")) {
//                if (result.equals("1") && !appToken.equals("")) {
                    // Save Server Details

                    // Clean
                    new CS_Action_ServerDetails(context).deleteServerDetails();
                    new CS_Action_LoginDetails(context).deleteLoginDetails();
                    new CS_Action_AccessDetails(context).deleteAccessDetails();

                    // Model
                    CS_Entity_ServerDetails model = new CS_Entity_ServerDetails(CS_ED.Encrypt(baseURL), bu_Id, te_Id, bCode, tCode, clientSecret, buildingName, tenantName, attachedPhoto, appToken, errorPostingURL, new CS_Utility(context).getDateTime(), "", "Active");

                    // Insert
                    dataInserted = new CS_Action_ServerDetails(context).insertServerDetails(model);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                progressdialog.dismiss();

                if (result.equals("1")) {
//                if (result.equals("1") && !appToken.equals("")) {

                    if (dataInserted) {
                        nextPage(Login.class);
                        new CS_Utility(context).showToast(getResources().getString(R.string.scan_server_details_connect_success), 1);

                    } else {
                        showAlertDialog(getResources().getString(R.string.scan_server_details_data_save_error));
                    }

                } else if (bCode.equals("") || tCode.equals("") || clientSecret.equals("") || baseURL.equals("")) {
                    showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_server_details));

                }
//                else if (appToken.equals("")) {
//                    showAlertDialog(getResources().getString(R.string.scan_server_details_app_token_not_found));
//
//                }
                else {
                    showAlertDialog(msg.equals("") ? CS_Constant.serverConnectionErrorMessage : msg);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
    }

    private void checkLoginDetails() {
        try {
            CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
            if (model != null) {

            } else {
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}