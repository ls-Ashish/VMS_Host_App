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
            builder.setTitle("Invalid");
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
                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                    JSONObject jObject = new JSONObject();
                    jObject.put("Id", "0");
                    jObject.put("bcode", bCode);
                    jObject.put("tcode", tCode);
                    jObject.put("client_secret", clientSecret);
                    jObject.put("Flag", "Validate_Building");

                    RequestBody body = RequestBody.create(mediaType, String.valueOf(jObject));

                    Request request = new Request.Builder()
                            .url(CS_ED.Decrypt(baseURL) + CS_API_URL.validateBuilding)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer " + "")
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response != null) {
                            String responseBody = response.body().string();

                            // fixme - till api is not ready
                            responseBody = "[{\"Result\":1,\"Msg\":\"Valid\",\"BU_Id\":1,\"TE_Id\":\"0\",\"BU_BuildingName\":\"DLF Cyber City\",\"TenantName\":\"\",\"ErrorPostingURL\":\"http:\\/\\/192.168.1.121\\/Portal\\/\",\"AttachedPhoto\":\"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\"}]";

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
                                    try {
                                        client = new CS_Utility(context).getOkHttpClient();
                                        mediaType = MediaType.parse("application/x-www-form-urlencoded");

                                        body = RequestBody.create(mediaType, "bcode=" + CS_ED.Decrypt(bCode)
                                                        + "&tcode=" + CS_ED.Decrypt(tCode)
                                                        + "&client_secret=" + CS_ED.Decrypt(clientSecret)
//                                            + "&redirect_uri=" + CS_ED.Decrypt(redirectURL)
//                                            + "&deviceId=" + deviceId)
                                        );

                                        request = new Request.Builder()
                                                .url(CS_ED.Decrypt(baseURL) + CS_API_URL.token)
                                                .method("POST", body)
                                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                                .build();

                                        response = client.newCall(request).execute();
                                        if (response.isSuccessful()) {
                                            if (response != null) {
                                                responseBody = response.body().string();
                                                if (!responseBody.equals("")) {

                                                    jsonData = responseBody;
                                                    jsonObject = new JSONObject(jsonData);

                                                    String tokenCode = jsonObject.getString("code");
                                                    String tokenMsg = jsonObject.getString("msg");

                                                    if (tokenCode.equals("200") && tokenMsg.equals("Success")) {
                                                        appToken = jsonObject.getString("appToken");
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                                    }
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
                if (result.equals("1") && !appToken.equals("")) {
                    // Save Server Details

                    // Clean
                    new CS_Action_ServerDetails(context).deleteServerDetails();
                    new CS_Action_LoginDetails(context).deleteLoginDetails();
                    new CS_Action_AccessDetails(context).deleteAccessDetails();

                    // Model
                    CS_Entity_ServerDetails model = new CS_Entity_ServerDetails(baseURL, bu_Id, te_Id, bCode, tCode, clientSecret, buildingName, tenantName, attachedPhoto, appToken, errorPostingURL, new CS_Utility(context).getDateTime(), "", "Active");

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

                if (result.equals("1") && !appToken.equals("")) {

                    if (dataInserted) {
                        nextPage(Login.class);
                        new CS_Utility(context).showToast(getResources().getString(R.string.scan_server_details_connect_success), 1);

                    } else {
                        showAlertDialog(getResources().getString(R.string.scan_server_details_data_save_error));
                    }

                } else if (bCode.equals("") || tCode.equals("") || clientSecret.equals("") || baseURL.equals("")) {
                    showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_server_details));

                } else {
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