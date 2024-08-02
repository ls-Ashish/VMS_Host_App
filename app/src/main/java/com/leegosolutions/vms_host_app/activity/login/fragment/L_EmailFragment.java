package com.leegosolutions.vms_host_app.activity.login.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.login.LoginVerify;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.email.CS_SendEmail;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class L_EmailFragment extends Fragment {

    private Context context;
    private FragmentLEmailBinding viewBinding;
    private String email = "", countryCode = "", mobileNo = "", password = "";
    private TabLayout tabLayout;
    private int targetTabPosition = 1; // Want to select the 2nd tab (index 1)

    public L_EmailFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public L_EmailFragment(Context context, TabLayout tabLayout) {
        try {
            this.context = context;
            this.tabLayout = tabLayout;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            this.context = context; // required - when orientation changed, need to re initialize context, as it becomes null

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onDetach() {
        try {
            super.onDetach();
            context = null;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            viewBinding = null; // Clear binding to avoid memory leaks

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_l_email, container, false);
        try {
            viewBinding = FragmentLEmailBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            on_Click_Text_LoginWithMobileNo();
            on_Click_Button_Next();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Text_LoginWithMobileNo() {
        viewBinding.tvLoginWithMobileNo.setOnClickListener(v -> {
            try {
                TabLayout.Tab targetTab = tabLayout.getTabAt(targetTabPosition);
                if (targetTab != null) {
                    targetTab.select();
                }


            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void on_Click_Button_Next() {
        viewBinding.btnNext.setOnClickListener(v -> {
            try {
                if (validate()) {
                    login();
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private boolean validate() {
        boolean result = false;
        try {
            boolean validEmail = false, validPassword = false;

            email = viewBinding.etEmail.getText().toString().trim();
            password = viewBinding.etPassword.getText().toString().trim();

            // Email
            if (email.equals("")) {
                viewBinding.tilEmail.setError(getResources().getString(R.string.login_email_empty_email));

            } else {
                // clear set error
                viewBinding.tilEmail.setError(null);
                validEmail = true;
            }

            // Password
            if (password.equals("")) {
                viewBinding.tilPassword.setError(getResources().getString(R.string.login_email_empty_password));
                viewBinding.tilPassword.setErrorIconDrawable(null);

            } else {
                // clear set error
                viewBinding.tilPassword.setError(null);
                validPassword = true;
            }

            // validate
            if (validEmail && validPassword) {
                result = true;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void login() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new Login().execute();

            } else {
                showSnackbar();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    class Login extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String baseURL = "", appToken = "", userType = "", userName = "", buildingId = "", tenantId = "", sourceId = "", enable2FA = "";
        private byte[] userPhoto = null;
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
                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();
                if (model != null) {
                    baseURL = CS_ED.Decrypt(model.getSD_BaseURL());
                    appToken = model.getSD_AppToken();
                    buildingId = model.getSD_BU_ID();
                    tenantId = model.getSD_TE_ID();
                }

                if (!baseURL.equals("")) {

                    JSONObject jObject = new JSONObject();
                    jObject.put("UserName", email);
                    jObject.put("Password", password);
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Validate_User");
                    jObject.put("LoginType", "Email");
                    jObject.put("CountryCode", countryCode);
                    jObject.put("MobileNo", mobileNo);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id", "0")
                            .addFormDataPart("Json_Data", String.valueOf(jObject))
                            .addFormDataPart("App_Token", appToken)
                            .build();

                    Request request = new Request.Builder()
                            .url(baseURL + CS_API_URL.Login)
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
                                    email = jsonObject.getString("Email");
                                    sourceId = jsonObject.getString("Source_Id");
                                    userType = jsonObject.getString("UserType");
                                    userName = jsonObject.getString("UserName");
                                    countryCode = jsonObject.getString("CountryCode");
                                    mobileNo = jsonObject.getString("ContactNo");
                                    enable2FA = jsonObject.getString("2FA_Enable");

                                    String base64_Image = jsonObject.getString("ProfilePhoto");
                                    if (!base64_Image.equals("null") && !base64_Image.equals("")) {
                                        userPhoto = Base64.decode(base64_Image, Base64.NO_WRAP);
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            try {
                if (result.equals("1")) {
                    // Save login details

                    // Clean
                    new CS_Action_LoginDetails(context).deleteLoginDetails();

                    // Model
                    CS_Entity_LoginDetails model = new CS_Entity_LoginDetails(sourceId, CS_ED.Encrypt(email), CS_ED.Encrypt(password), CS_ED.Encrypt(userType), CS_ED.Encrypt(userName), userPhoto, enable2FA.equals("1") ? 0 : 1, new CS_Utility(context).getDateTime(), "", CS_ED.Encrypt(countryCode), CS_ED.Encrypt(mobileNo));

                    // Insert
                    dataInserted = new CS_Action_LoginDetails(context).insertLoginDetails(model);

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

                    if (dataInserted) {

                        if (enable2FA.equals("1")) {
                            new CheckEmailDetailsFromSQLite().execute();

                        } else {
                            goToHomePage();
                            new CS_Utility(context).showToast(getResources().getString(R.string.login_success), 1);
                        }

                    } else {
                        showAlertDialog("Error", getResources().getString(R.string.login_data_save_error));
                    }

                } else if (baseURL.equals("")) {
                    showAlertDialog("Error", CS_Constant.invalidBaseURL);

                } else {
                    showAlertDialog("Error", !msg.equals("") ? msg : CS_Constant.serverConnectionErrorMessage);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public void showAlertDialog(String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
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

    private void nextPage(Class aClass, String calledFrom) {
        try {
            Intent intent = new Intent(context, aClass);
            // Clear
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("cameFrom", calledFrom);
            intent.putExtra("email", email);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void goToHomePage() {
        try {
            Intent intent = new Intent(context, Home.class);
            // Clear
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                login();

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

    private class CheckEmailDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        private boolean settingsAvailable = false;

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                // check building wise email setup
                boolean emailSetUpBuildingWise = new CS_Utility(context).checkEmailSetUpBuildingWise();
                boolean defaultEmail = new CS_Utility(context).checkDefaultEmail();

                if (emailSetUpBuildingWise) {
                    settingsAvailable = true;

                } else if (defaultEmail) {
                    settingsAvailable = true;
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
                if (settingsAvailable) {
                    // Settings available
                    nextPage(LoginVerify.class, "L_EmailFragment");

                } else {
                    showAlertDialog("Error - 2FA", getResources().getString(R.string.email_settings_not_available));
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

}