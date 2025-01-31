package com.leegosolutions.vms_host_app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.activity.settings.security.S_Set_PIN;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.ActivityHomeBinding;
import com.leegosolutions.vms_host_app.utility.CS_Check_Server_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Splash extends AppCompatActivity implements CS_Check_Server_Connection.ResultListener{

    private Context context = Splash.this;
    private RelativeLayout rl_main;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", appToken = "", appPINStatus = "", appPIN = "", appBiometricStatus = "";
    private ImageView iv_Logo;
    private byte[] logo = null;
    private int isLogin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // Block dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            // Full screen
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.splash);

            rl_main = findViewById(R.id.rl_main);
            iv_Logo = findViewById(R.id.iv_Logo);

            // Switch to night mode
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//
//            // Switch to day mode
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//            // Let the system decide based on time of day
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);


//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            fetchServerDetails();
//            startWithDelay();


        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void fetchServerDetails() {
        try {
            new FetchServerDetails().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchServerDetails extends AsyncTask<Void, Void, Void> {

        boolean isServerDetailsAvailable = false;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
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

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            try {
                // Check login status
                if (isServerDetailsAvailable) {
//                    isLogin = new CS_Action_LoginDetails(context).getLoginDetails().getLD_IsLogin();

                    CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
                    if (model != null) {
                        isLogin = model.getLD_IsLogin();
                        appPINStatus = model.getLD_S_PIN_Status();
                        appPIN = CS_ED.Decrypt(model.getLD_S_PIN());
                        appBiometricStatus = model.getLD_S_Fingerprint_Status();
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
                // Logo
                showPropertyUnitLogo(isServerDetailsAvailable);

                if (isServerDetailsAvailable) {
                    checkServerConnection();

                } else {
                    nextPageWithDelay(ScanServerDetails.class);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void checkServerConnection() {
        try {
            if (new CS_Connection(context).getStatus()) {
//                new ServerConnection().execute();
                new CS_Check_Server_Connection(context, Splash.this).execute();

            } else if (appPINStatus.equals("1") || appBiometricStatus.equals("1")) {
                // Require App PIN
                requireAppPINWithDelay();

            } else if (isLogin == 1) {
                // Offline but, is logged in
                nextPageWithDelay(Home.class);

            } else if (isLogin == 0) {
                // Server connected but not login
                nextPageWithDelay(Login.class);

            } else {
                new CS_Utility(context).showToast("Error: check server connection", 0);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    class ServerConnection extends AsyncTask<Void, Void, Void> {

        //        private ProgressDialog progressdialog;
        private String result = "", msg = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
//                progressdialog = new ProgressDialog(context);
//                progressdialog.setCancelable(false);
//                progressdialog.setMessage("Please wait...");
//                progressdialog.show();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
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

                        }
                    }
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
//            try {
//                // If connected - Check login status
//                if (result.equals("1")) {
//                    isLogin = new CS_Action_LoginDetails(context).getLoginDetails().getLD_IsLogin();
//                }
//
//            } catch (Exception e) {
//                new CS_Utility(context).saveError(e);
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
//                progressdialog.dismiss();

                if (result.equals("1")) {

                    if (appPINStatus.equals("1") || appBiometricStatus.equals("1")) {
                        // Require App PIN
                        requireAppPINWithDelay();

                    } else if (isLogin == 1) {
                        nextPageWithDelay(Home.class);

                    } else if (isLogin == 0) {
                        nextPageWithDelay(Login.class);

                    } else {
                        new CS_Utility(context).showToast("Error: server connection", 0);

                    }
                } else {
                    new CS_Utility(context).showToast(msg.equals("") ? "Error" : CS_Constant.serverConnectionErrorMessage, 0);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(rl_main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                checkServerConnection();

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

//    private void startWithDelay() {
//        try {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        fetchAppPIN();
//
//                    } catch (Exception e) {
//                        new CS_Utility(context).saveError(e);
//                    }
//                }
//            }, CS_Constant.splashPageDelay);
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e);
//        }
//    }

//    private void fetchAppPIN() {
//        try {
//            new FetchAppPIN().execute();
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e);
//        }
//    }

//    public class FetchAppPIN extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
//                if (model != null) {
//                    appPINStatus = model.getLD_S_PIN_Status();
//                    appPIN = CS_ED.Decrypt(model.getLD_S_PIN());
//                    appBiometricStatus = model.getLD_S_Fingerprint_Status();
//                }
//
//            } catch (Exception e) {
//                new CS_Utility(context).saveError(e);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//            try {
//                if (appPINStatus.equals("1") || appBiometricStatus.equals("1")) {
//                    // Require App PIN
//                    requireAppPIN();
//
//
//                } else {
////                    startApp();
//
//                }
//
//            } catch (Exception e) {
//                new CS_Utility(context).saveError(e);
//            }
//        }
//    }

    private void requireAppPINWithDelay() {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
//                        Intent intent = new Intent(context, AppPIN.class);
//                        intent.putExtra("appPINStatus", appPINStatus);
//                        intent.putExtra("appPIN", appPIN);
//                        intent.putExtra("message", getResources().getText(R.string.splash_app_pin_message));
//                        intent.putExtra("appBiometricStatus", appBiometricStatus);
//                        startActivityForResult(intent, 1001);
//                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                        Intent intent = new Intent(context, AppPIN.class);
                        // Clear
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            }, CS_Constant.splashPageDelay);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            if (requestCode == 1001) {
//                if(resultCode == Activity.RESULT_OK){
//
//                    String result = data.getStringExtra("result");
//                    if (result.equals("1")) {
//                        nextPage(Home.class);
//
//                    } else {
//                        new CS_Utility(context).showToast("Result : " + result, 1);
//                    }
//                }
//                if (resultCode == Activity.RESULT_CANCELED) {
//                    // Write your code if there's no result
//                    finish();
//                    new CS_Utility(context).showToast("Cancelled", 1);
//                }
//            }
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e);
//        }
//    }

    private void nextPageWithDelay(Class aClass) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent = new Intent(context, aClass);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            }, CS_Constant.splashPageDelay);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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

    private void showPropertyUnitLogo(boolean isServerDetailsAvailable) {
        try {
//            if (logo != null) {
            if (isServerDetailsAvailable) {

                if (logo != null) {
                    // Building / Tenant logo
                    Glide.with(context)
                            .asBitmap()
                            .load(logo)
                            .into(iv_Logo);

                } else {
                    // If once connected and no logo available then blank
                }
            } else {
                // Default
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_app_logo)
                        .placeholder(R.drawable.ic_app_logo)
                        .into(iv_Logo);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }

    }

    @Override
    public void connectionStatus(String result, String msg, boolean dataInserted) {
        try {
            if (result.equals("1")) {

                if (appPINStatus.equals("1") || appBiometricStatus.equals("1")) {
                    // Require App PIN
                    requireAppPINWithDelay();

                } else if (isLogin == 1) {
                    nextPageWithDelay(Home.class);

                } else if (isLogin == 0) {
                    nextPageWithDelay(Login.class);

                } else {
                    new CS_Utility(context).showToast("Error: server connection", 0);

                }
            } else if (!msg.isEmpty()) {
                new CS_Utility(context).showToast(msg, 0);

            } else {
                new CS_Utility(context).showToast(msg.equals("") ? "Error" : CS_Constant.serverConnectionErrorMessage, 0);
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}