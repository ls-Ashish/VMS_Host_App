package com.leegosolutions.vms_host_app.activity.settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.activity.notifications.N_NotificationsFragment;
import com.leegosolutions.vms_host_app.activity.settings.change_password.S_ChangePassword;
import com.leegosolutions.vms_host_app.activity.settings.security.S_Security;
import com.leegosolutions.vms_host_app.activity.settings.server.S_ServerDetailsFragment;
import com.leegosolutions.vms_host_app.activity.settings.update.S_UpdateMobileNoFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsDetails;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentAAccessBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSSettingsBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.CS_VersionDetails;
import com.leegosolutions.vms_host_app.utility.email.CS_SendEmail;
import com.leegosolutions.vms_host_app.utility.sms.CS_SendSMS;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class S_SettingsFragment extends Fragment {

    private Context context;
    private FragmentSSettingsBinding viewBinding;
    private BottomNavigationView bottomNavigationView;

    public S_SettingsFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public S_SettingsFragment(Context context) {
        try {
            this.context = context;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public S_SettingsFragment(Context context, BottomNavigationView bottomNavigationView) {
        try {
            this.context = context;
            this.bottomNavigationView = bottomNavigationView;

            // Show
            bottomNavigationView.setVisibility(View.VISIBLE);

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
    public void onResume() {
        super.onResume();
        try {
            if (bottomNavigationView != null) {
                // Show
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

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
//        return inflater.inflate(R.layout.fragment_s_settings, container, false);
        try {
            viewBinding = FragmentSSettingsBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            showAppVersion();
            onClick_Button_LogOut();
            on_Click_AccountPreference();
            on_Click_Security();
            on_Click_ServerDetails();
            checkForUpdate();
            on_Click_Security_Pin();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showAppVersion() {
        try {
            String version = new CS_Utility(context).appVersion();
            viewBinding.tvAppVersion.setText(context.getResources().getText(R.string.settings_app_version) + " " + version);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void onClick_Button_LogOut() {
        viewBinding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showLogoutConfirmation();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void showLogoutConfirmation() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert");
            builder.setIcon(R.drawable.ic_logout);
            builder.setMessage("Are you sure want to logout ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    new LogOut().execute();
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

    public class LogOut extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                // Clean
                new CS_Action_LoginDetails(context).deleteLoginDetails();
                new CS_Action_AccessDetails(context).deleteAccessDetails();

                result = true;

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            try {
                if (result) {
                    new CS_Utility(context).showToast(getResources().getString(R.string.settings_logout_success), 1);
                    goToLoginPage();

                } else {
                    showAlertDialog(context.getResources().getString(R.string.settings_logout_error));
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public void goToLoginPage() {
        try {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            Intent intent = new Intent(requireActivity(), Login.class);
            startActivity(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

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

    public void on_Click_AccountPreference() {
        viewBinding.llAccountPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_UpdateMobileNoFragment(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void on_Click_Security() {
        viewBinding.llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_ChangePassword(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void on_Click_Security_Pin() {
        viewBinding.llSecurityPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_Security(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void on_Click_ServerDetails() {
        viewBinding.llServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_ServerDetailsFragment(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void goToNextFragment(Fragment fragment) {
        try {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

            // Hide
            bottomNavigationView.setVisibility(View.GONE);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void checkForUpdate() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new UpdateLoginDetails().execute();

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    class UpdateLoginDetails extends AsyncTask<Void, Void, Void> {

//        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String baseURL = "", appToken = "", email = "", userType = "", userName = "", password = "", buildingId = "", tenantId = "", sourceId = "", enable2FA = "", lastUpdationDate = "", countryCode = "", mobileNo = "";
        private byte[] userPhoto = null;
        private boolean dataInserted = false;

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
                // Fetch lastUpdationDate from sqlite
                String sqLiteLastUpdationDate = new CS_Action_LoginDetails(context).getLoginDetails().getLD_UpdationDate();
                sourceId = new CS_Action_LoginDetails(context).getLoginDetails().getLD_SourceId();

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
                    jObject.put("UserName", "");
                    jObject.put("Password", "");
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Fetch_Login_Details");
                    jObject.put("LoginType", "");
                    jObject.put("CountryCode", "");
                    jObject.put("MobileNo", "");
                    jObject.put("LastUpdationDate", sqLiteLastUpdationDate);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id", sourceId)
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
                                    password = jsonObject.getString("Password");
                                    countryCode = jsonObject.getString("CountryCode");
                                    mobileNo = jsonObject.getString("ContactNo");
                                    enable2FA = jsonObject.getString("2FA_Enable");
                                    lastUpdationDate = jsonObject.getString("LastUpdationDate");

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

                    // Update
                    dataInserted = new CS_Action_LoginDetails(context).updateLoginDetails(sourceId, CS_ED.Encrypt(email), CS_ED.Encrypt(password), CS_ED.Encrypt(userType), CS_ED.Encrypt(userName), userPhoto, lastUpdationDate, CS_ED.Encrypt(countryCode), CS_ED.Encrypt(mobileNo));
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
//                progressdialog.dismiss();

                if (result.equals("1")) {

                    if (dataInserted) {
//                        new CS_Utility(context).showToast(msg, 1);

                    } else {
                        new CS_Utility(context).showToast(getResources().getString(R.string.login_data_save_error), 1);
                    }

                } else if (result.equals("2")) {
                    // No update available.

                } else if (baseURL.equals("")) {
                    new CS_Utility(context).showToast(CS_Constant.invalidBaseURL, 1);

                } else {
                    new CS_Utility(context).showToast("Error", 1);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

}