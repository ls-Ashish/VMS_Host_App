package com.leegosolutions.vms_host_app.activity;


import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_EmailDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_SMSDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.ActivityScanServerDetailsBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.email.CS_Fetch_Email_Setup;
import com.leegosolutions.vms_host_app.utility.sms.CS_Fetch_SMS_Setup;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.camera.CameraXScanner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanServerDetails extends AppCompatActivity {

    private final AppCompatActivity appCompatActivity = ScanServerDetails.this;
    private final Context context = ScanServerDetails.this;
    private ActivityScanServerDetailsBinding viewBinding;
    private final int LAUNCH_SCANNING_ACTIVITY = 1;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", appToken = "", bu_Id = "", te_Id = "", buildingName = "", tenantName = "", errorPostingURL = "", buildingCountry = "", buildingAddressLine_1 = "", buildingAddressLine_2 = "";
    private byte[] attachedPhoto;
    private static final int PERMISSION_CODE = 1001;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final int OPEN_GALLERY = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_server_details);
        try {
            viewBinding = ActivityScanServerDetailsBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

//            startAnimation();
            on_Click_Scan_button();
            on_Click_Gallery_Icon();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void startAnimation() {
        try {
            viewBinding.lavImage.setAnimation("qr_code_scanner.json");
            viewBinding.lavImage.loop(true);
            viewBinding.lavImage.playAnimation();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Scan_button() {
        viewBinding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getCameraPermissions();
//                    showCameraOrGalleryDialog();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void on_Click_Gallery_Icon() {
        viewBinding.tvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openGallery();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    // custom AlertDialog with layout
//    private void showCameraOrGalleryDialog() {
//        try {
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//            LayoutInflater inflater = this.getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.single_row_camera_or_gallery, null);
//            dialogBuilder.setCancelable(false);
//            dialogBuilder.setView(dialogView);
//
//            TextView tv_Camera = dialogView.findViewById(R.id.tv_Camera);
//            TextView tv_Gallery = dialogView.findViewById(R.id.tv_Gallery);
//            TextView tv_Cancel = dialogView.findViewById(R.id.tv_Cancel);
//
//            AlertDialog alertDialog = dialogBuilder.create();
//            alertDialog.show();
//
//            tv_Camera.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        alertDialog.dismiss();
//                        getCameraPermissions();
//
//                    } catch (Exception e) {
//                        new CS_Utility(context).saveError(e);
//                    }
//                }
//            });
//
//            tv_Gallery.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        alertDialog.dismiss();
//                        openGallery();
//
//                    } catch (Exception e) {
//                        new CS_Utility(context).saveError(e);
//                    }
//                }
//            });
//
//            tv_Cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        alertDialog.dismiss();
//
//                    } catch (Exception e) {
//                        new CS_Utility(context).saveError(e);
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e);
//        }
//    }

    private void getCameraPermissions() {
        try {
            if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                startScanningActivityForResult();

            } else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION}, PERMISSION_CODE);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        try {
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_DENIED) {
                    new CS_Utility(context).showToast(getResources().getString(R.string.camera_permission_denied), 1);
                    return;
                }
            }

            if (requestCode == PERMISSION_CODE) {
                startScanningActivityForResult();
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void startScanningActivityForResult() {
        try {
            Intent intent = new Intent(this, CameraXScanner.class);
            startActivityForResult(intent, LAUNCH_SCANNING_ACTIVITY);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void openGallery() {
        try {
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType( android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(pickIntent, OPEN_GALLERY);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == LAUNCH_SCANNING_ACTIVITY) {
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        String result = data.getStringExtra("result");
                        readScanResult(result);

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        // Write your code if there's no result
                        new CS_Utility(context).showToast("Cancelled", 1);

                    } else {
                        new CS_Utility(context).showToast("resultCode : " + String.valueOf(Activity.RESULT_OK), 1);
                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }

            } else if (requestCode == OPEN_GALLERY) {
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        if(data == null || data.getData()==null) {
                            new CS_Utility(context).showToast("The uri is null.", 1);
                            return;
                        }
                        Uri uri = data.getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            if (bitmap == null) {
                                Toast.makeText(this, "Uri is not a bitmap," + uri.toString(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            int width = bitmap.getWidth(), height = bitmap.getHeight();
                            int[] pixels = new int[width * height];
                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                            bitmap.recycle();
                            bitmap = null;

                            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                            MultiFormatReader reader = new MultiFormatReader();
                            try {
                                String result = reader.decode(bBitmap).getText();
                                readScanResult(result);
//                                Toast.makeText(this, "The content of the QR image is: " + result, Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_qr_code));
                            }

                        } catch (Exception e) {
                            new CS_Utility(context).showToast(getResources().getString(R.string.scan_server_details_cannot_read_qr_code) + " " + uri.toString(), 1);
                            new CS_Utility(context).saveError(e);
                        }

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        // Write your code if there's no result
                        new CS_Utility(context).showToast("Cancelled", 1);

                    }  else {
                        new CS_Utility(context).showToast("resultCode : " + String.valueOf(Activity.RESULT_OK), 1);
                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void readScanResult(String result) {
        try {
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

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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
                showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_qr_code));
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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
            new CS_Utility(context).saveError(e);
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
            new CS_Utility(context).saveError(e);
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                connect();

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }
                    });
            snackbar.show();


        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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
                new CS_Utility(context).saveError(e);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {

                    OkHttpClient client = new CS_Utility(context).getOkHttpClient();

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

                                result = jsonObject.getString("Result");
                                msg = jsonObject.getString("Msg");

                                if (result.equals("1")) {
                                    bu_Id = jsonObject.getString("BU_Id");
                                    te_Id = jsonObject.getString("TE_Id");
                                    buildingName = jsonObject.getString("BU_BuildingName");
                                    tenantName = jsonObject.getString("TenantName");
                                    errorPostingURL = jsonObject.getString("ErrorPostingURL");
                                    buildingCountry = jsonObject.getString("Country");
                                    buildingAddressLine_1 = jsonObject.getString("AddressLine_1");
                                    buildingAddressLine_2 = jsonObject.getString("AddressLine_2");

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
                msg = e.toString();
                new CS_Utility(context).saveError(e);
            }
            try {
                if (result.equals("1")) {
//                if (result.equals("1") && !appToken.equals("")) {
                    // Save Server Details

                    // Clean
                    cleanExistingTables();

                    // Model
                    CS_Entity_ServerDetails model = new CS_Entity_ServerDetails(CS_ED.Encrypt(baseURL), bu_Id, te_Id, bCode, tCode, clientSecret, buildingName, tenantName, attachedPhoto, appToken, errorPostingURL, new CS_Utility(context).getDateTime(), "", "Active", buildingCountry, buildingAddressLine_1, buildingAddressLine_2);

                    // Insert
                    dataInserted = new CS_Action_ServerDetails(context).insertServerDetails(model);
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
                progressdialog.dismiss();

                if (result.equals("1")) {
//                if (result.equals("1") && !appToken.equals("")) {

                    if (dataInserted) {

                        // Fetch Data
                        fetchData(context, baseURL, bCode, tCode, clientSecret, appToken);

                        nextPage(Login.class);
                        new CS_Utility(context).showToast(getResources().getString(R.string.scan_server_details_connect_success), 1);

                    } else {
                        showAlertDialog(getResources().getString(R.string.scan_server_details_data_save_error));
                    }

                } else if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {
                    showAlertDialog(msg.isEmpty() ? getResources().getString(R.string.scan_server_details_invalid_server_details) : msg);

                }
//                else if (appToken.equals("")) {
//                    showAlertDialog(getResources().getString(R.string.scan_server_details_app_token_not_found));
//
//                }
                else {
                    showAlertDialog(msg.equals("") ? CS_Constant.serverConnectionErrorMessage : msg);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void cleanExistingTables() {
        try {
            new CS_Action_ServerDetails(context).deleteServerDetails();
            new CS_Action_LoginDetails(context).deleteLoginDetails();
            new CS_Action_AccessDetails(context).deleteAccessDetails();
            new CS_Action_EmailDetails(context).deleteEmailDetails();
            new CS_Action_SMSDetails(context).deleteSMSDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void fetchData(Context context, String baseURL, String bCode, String tCode, String clientSecret, String appToken) {
        try {
            // Default Email and Email Setup
            new CS_Fetch_Email_Setup(context, baseURL, bCode, tCode, clientSecret, appToken, bu_Id, te_Id).execute();
            // SMS Setup
            new CS_Fetch_SMS_Setup(context, baseURL, bCode, tCode, clientSecret, appToken, bu_Id, te_Id).execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(1);

            if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                showAppExitDialog();

            } else {
                super.onBackPressed();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void showAppExitDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.app_exit_caption));
            builder.setIcon(R.drawable.ic_exit);
            builder.setMessage(getResources().getString(R.string.app_exit_message));
            builder.setCancelable(false);
            builder.setPositiveButton("Yes".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}