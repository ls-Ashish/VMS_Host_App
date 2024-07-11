package com.leegosolutions.vms_host_app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

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
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Splash extends AppCompatActivity {

    private Context context = Splash.this;
    private RelativeLayout rl_main;
    private String bCode="", tCode="", clientSecret="", baseURL="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        try {
            rl_main = findViewById(R.id.rl_main);

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

//            fetchServerDetails(); // fixme
            nextPageWithDelay(Home.class);

//            nextPage(LoginOrRegistration.class); //fixme
//            nextPage(Home.class);
//            nextPage(CameraXScanner.class);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void fetchServerDetails() {
        try {
            new FetchServerDetails().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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

    private void nextPageWithDelay(Class aClass) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, aClass);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            }, CS_Constant.splashPageDelay);

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

    public class FetchServerDetails extends AsyncTask<Void, Void, CS_Entity_ServerDetails> {

        @Override
        protected CS_Entity_ServerDetails doInBackground(Void... voids) {
            CS_Entity_ServerDetails model = null;
            try {
                model = new CS_Action_ServerDetails(context).getServerDetails();

                if (model != null) {
                    bCode = model.getSD_BCode();
                    tCode = model.getSD_TCode();
                    clientSecret = model.getSD_ClientSecret();
                    baseURL = model.getSD_BaseURL();

                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            return model;
        }

        @Override
        protected void onPostExecute(CS_Entity_ServerDetails model) {
            super.onPostExecute(model);
            try {
                if (!bCode.equals("") && !tCode.equals("") && !tCode.equals("") && !baseURL.equals("")) {
                    checkServerConnection();

                } else {
                    nextPageWithDelay(ScanServerDetails.class);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
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
                        responseBody = "[{\"Result\":1,\"Msg\":\"Valid\",\"BU_Id\":1,\"TE_Id\":\"0\",\"BU_BuildingName\":\"DLF Cyber City\",\"TenantName\":\"\",\"ErrorPostingURL\":\"http:\\/\\/192.168.1.121\\/Portal\\/\",\"AttachedPhoto\":\"iVBORw0KGgoAAAANSUhEUgAAAMwAAADMCAMAAAAI/LzAAAAAZlBMVEX///8bGxsAAAD7+/tGRkYHBwfz8/M8PDwVFRW2trYoKCgZGRkODg7g4OBPT09mZmZXV1ft7e3Z2dnT09Ovr6+EhITFxcVhYWHMzMzn5+cwMDCpqakgICCPj49tbW2ioqJ6enqYmJjK8/t/AAAIx0lEQVR4nO2a6baqOBCFpQIhQkBwYFCc3v8lOyBiKgPoPafbdXvV90/BkEoqOzuFqxVBEARBEARBEARBEARBEARBEARBEARBEARBEARBEARBEARBEARBEARBEP8xfF99uwu/xw5u7Nt9+C3YBZL1tzvxWzQQwKX9di9+iTAKRLT/di9+hxMEQRDl/4upYYFQwQRw/T9owKGfmCAQUH67Jz8n64aJUVNz+3ZXfk4RBSOw/XZffso6Ec9gouO3O/NDeA3BBPzlO+e1E69gRMK/3Z+fkBXaxKipOX+7Qz+A3aNUD0Ycs2936c/ZxTJAyNu3u/TH8DPgWNTU/LUHmy0II5jgrz3YlLU5MWpq5Onzhhh34x4XfI+nSU+L/rbXIG2g+FgD+OEYI45d1x3DYnPe7zLL8GX6zceLxxDGSxhbIityF+Zty2Q1yEhDDYmQMoKe4LLe4kHcaTdLOLinhuMWTSRYa7v1zOCnwTShtNJ1SluA+Io831rPbTi5U3FnLwBEVGAfWa742sV2VX6oAevA0hEk95Brp1h2QMF4xHO/EAwY6XmreA42cs/PH7rn08KTA5DX6WZkBoVs3E0eloI5oAHPIGeVQ5ovbB0dPpoadl8KJhDRfQom0XJShp5xiyN/Y0M37+j2jRouZg2AiJtyA7D7JJjythhMIKdSVomyzFcTskcZd7ND5ZdKTXFYNqadiW59ukL4STCZY7uyiOoxydHShrNbbdhCizLW1xqrVRTRYWVkiDiWWRh9eLCxRsRF+ky0ExYzT5NLwSAxu4u0j6/JCpScKhWH8AR8EAzS2kCOW42ZJ0/9wWLmGTQsKRLMfQY22oxu8yEGuK2uUjsDiKPKgkfd6YODDZJRmW8UdREmEQ5HjtucvrS9thavwvyyMaiv2s1nGEIQybqstcahYmMzqXy77oTFbNQOvt3fEqxI8OgAaIMX5R4x03dhEc8b+SnL4cK1ggbkr2RVSvAmWMxgcnZsfURrCc694PO3xKzTIpa+iB/wKW+F3LPL85ECtn3VefyQvHuwyXJtBmSizShexnDre16hYDwbWhvo01fP+qtKn4x2l6bPodPTP9q86dG2ekkErcxVjnpe95N2RV/d3U1WQg/mMvf08qJXl65PfZFJxsSrX8a+5Gdm40DS8Fgg+rOF8CjzVc9PeZh7eqWvQRGU7PFZaf7BHsllsAve64mT2cEgM+Nb2jctcdPg6r5pgGPfA+POCUWbIfeb+nY0o7mzQ8xGSjsY9E3hWdr5O/I9cDV2V6U/IIf9C2+gQRS+Y9E4Shxcri6trrdYzNz6z7AytwyBHm46BYiH14A3XaTHK/c33HMbIxeMUhObyk1mitnN3eQ21jvShYhOV/ONZXvUnNSglNjyi0J4Ths62czGga/d1DygDTby2Aw8qik6QANoT9g6CjIB2ynFR1Xn5/OX5XnOBeN56DdNfShF4lmU9xSVWRFR/kpNllumTUV7Ztcqc1wB36lWH0YsZugadsi9Kh31Mr1XzGZcuD6+VYF41GPComR8n9sX8nBxahgWM9w9pA2pcshMTyCZe7R/M3PM1Ndx5mK7VVPXOi80SxLA0JE+wYtMPwfIXhu3HfIp7sbLem5mXsPFV5WzItOuWLV3fL/nq4Wp4freZLhgbunw+h2fspupXGkbWXPjO0gsunjF+KmzL6QHXs06I+OAa4jZHq9/9c39HZ+yPvoLAPI4DdcBmlUIwgQqfrjyjXVBDrfPm5qdJVgv9COAOPaHyo3+jc/83YVfzJRPmUIGwHvw44ZQCfamtPbM3tReZRTPBjNzpEdxPkx4jsJ7q2aGy7TDZtXTn6JU/6x3MlDyEJRwmhWjKNyWoUx91tZ+coqVWT/DprLPMm74FGeLDPVDGvL7NLL73h0D4x1eX8ppqr1ChtstztVUKhsdpSqmOQ3QXSt2wWv06jfuc30XI3lzt5jpgxB4KiuP8hbcjDqu6DiXcligB2yn63bQlVR4jlAD6Eivu2D8hlEOOoIyWdbuFpGYecpEbMxu2JWFkedD4omu4WjFJvsxh+YO4bb6jjTolJmmw/pAS/stZwaF855sHClZs7V2oISCt+Ph7ML0OYs2rHrkncp379aJjvkwaW15DfH5fzN8rR+6guidmpm75DXZDqWI2hITyjKPpiNVm+trBYik4s/bZOx1z+jJY62fN+cCr8v0YXWZ7lNS4Slm4cqVM+JsqsnJoqymfFK+rXnOkxLhl5qq7Xk9uRE4+KpoSHnSeBCc8CiMAuBYMmsLPcQ0L0zySi8dDb907nKvdZLK+ySofUHp5YTUM5/WMJVt+8p6IX3uGZeZxeO1qLnpPevwhk9xvE9VE5ihAxe4Elw/WSgH9WxWpeTpZa7SfktNx7lAoqeUzR0M+Dfr1/PkqO17a1s26KULleGlc8dG7zv6nTMawyr1g7/S7UfGKkVsUap4Kty2nXDEEjxT5b4Qugj6YRe6mLmcIS5iyGNV9saif81wRg+A7HGkV303TEXiTN5qMZgU4snnWt7DYOg6dqeOMlMpjAWpDskqsaOiNdI4qgefoJI8Mx7sLj6alR4Lob1J4Utv2PquG2V4x1q9GUc3taBZIXvbagyWEoSyhlS55cJ4sHDqymXp1WN3eP0M+xTX3Y2/DD/RWAcEqHkFavtsjsY5KCr4ulNLx84fcLmPuWOUyjDYVJqvm/u7wOMR3Hin6PhDL3MUCJS/raGxJ150J3YT2tsA7Sf2zslirzwJALhUSAPtM4YZvLpppw8v5JbHdZ3c5JG3l1VlvazrTz/rvXMtRLbL3SZmRWf6O0FUn3bGsJ4chSH02/7/rg1od9mlrnbjakRtJGUZuqpLd87a2PkTS1qq0CTP82Jzu++3pf2Xpmuy8N+e3tpVidbacW/9d8l64kBSqnl3fB/Xu9Xd/ZPCzmAn1gzO3m38cKEl7y8/vfCX/q2OIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiCIAiC+Df5B67xhSmw1YtdAAAAAElFTkSuQmCC\"}]";

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
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            try {
                // If connected - Check login status
                if (result.equals("1")) {
                    isLogin = new CS_Action_LoginDetails(context).getLoginDetails().getLD_IsLogin();
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

                    if (isLogin == 1) {
                        nextPage(Home.class);

                    } else {
                        nextPage(Login.class);
                    }

                } else {
                    new CS_Utility(context).showToast(msg.equals("") ? "Error" : CS_Constant.serverConnectionErrorMessage, 0);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
    }

    private void checkIsLogin() {
        try {


        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}