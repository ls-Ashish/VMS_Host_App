package com.leegosolutions.vms_host_app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.ActivityHomeBinding;
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

public class Splash extends AppCompatActivity {

    private Context context = Splash.this;
    private RelativeLayout rl_main;
    private String baseURL = "", bCode = "", tCode = "", clientSecret = "", appToken = "";
    private ImageView iv_Logo;
    private byte[] logo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try {
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

//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            fetchServerDetails();

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

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(rl_main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
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

    public class FetchServerDetails extends AsyncTask<Void, Void, Void> {

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
                showPropertyUnitLogo();

                if (!baseURL.equals("") && !bCode.equals("") && !tCode.equals("") && !clientSecret.equals("")) {
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
                new CheckServerConnection().execute();

            } else {
                showSnackbar();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    class CheckServerConnection extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private int isLogin = 0;

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
            try {
                // If connected - Check login status
                if (result.equals("1")) {
                    isLogin = new CS_Action_LoginDetails(context).getLoginDetails().getLD_IsLogin();
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

                    if (isLogin == 1) {
                        nextPage(Home.class);

                    } else {
                        nextPage(Login.class);
                    }

                } else {
                    new CS_Utility(context).showToast(msg.equals("") ? "Error" : CS_Constant.serverConnectionErrorMessage, 0);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void showPropertyUnitLogo() {
        try {
            if (logo != null) {
                // Building / Tenant logo
                Glide.with(context)
                        .asBitmap()
                        .load(logo)
                        .into(iv_Logo);
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

}