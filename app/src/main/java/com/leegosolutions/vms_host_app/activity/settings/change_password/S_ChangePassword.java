package com.leegosolutions.vms_host_app.activity.settings.change_password;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_EmailFragment;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.database.join.CS_LoginDetailsWithAccessDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentSChangePasswordBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSUpdateMobileNoBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class S_ChangePassword extends Fragment {

    private Context context;
    private FragmentSChangePasswordBinding viewBinding;
    private String oldPasswordFromSQLite="", newPassword="";

    public S_ChangePassword() {
        // Required empty public constructor
    }

    public S_ChangePassword(Context context) {
        try {
            this.context = context;

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
//        return inflater.inflate(R.layout.fragment_s_change_password, container, false);
        try {
            viewBinding = FragmentSChangePasswordBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setPasswordInfoText();
            fetchData();
            on_Click_Back_Arrow();
            on_Click_Button_Update();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setPasswordInfoText() {
        try {
            viewBinding.tvPasswordInfo.setText(context.getResources().getString(R.string.change_password_info) + " " + CS_Constant.symbolAcceptedInPasswords);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void fetchData() {
        try {
            new FetchDataFromSQLite().execute();
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Back_Arrow() {
        viewBinding.ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    requireActivity().onBackPressed();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void on_Click_Button_Update() {
        viewBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validate()) {
                        updatePassword();
                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private boolean validate() {
        boolean result = false;
        try {
            boolean validOldPassword = false, validNewPassword = false, validConfirmPassword = false;

            String oldPassword = viewBinding.etOldPassword.getText().toString().trim();
            newPassword = viewBinding.etNewPassword.getText().toString().trim();
            String confirmNewPassword = viewBinding.etConfirmNewPassword.getText().toString().trim();

            // Old
            if (oldPassword.equals("")) {
                viewBinding.tilOldPassword.setError(getResources().getString(R.string.change_password_enter_old_password));
                viewBinding.tilOldPassword.setErrorIconDrawable(null);

            } else if (!oldPassword.equals(oldPasswordFromSQLite)) {
                viewBinding.tilOldPassword.setError(getResources().getString(R.string.change_password_old_password_did_not_match));
                viewBinding.tilOldPassword.setErrorIconDrawable(null);

            } else {
                // clear set error
                viewBinding.tilOldPassword.setError(null);
                viewBinding.tilOldPassword.setErrorIconDrawable(null);
                validOldPassword = true;
            }

            // New
            if (newPassword.equals("")) {
                viewBinding.tilNewPassword.setError(getResources().getString(R.string.change_password_enter_new_password));
                viewBinding.tilNewPassword.setErrorIconDrawable(null);

            } else if (newPassword.length() < 12
                    || !new CS_Utility(context).containsUpperCase(newPassword)
                    || !new CS_Utility(context).containsLowerCase(newPassword)
                    || !new CS_Utility(context).containsSpecificSymbols(newPassword, CS_Constant.symbolAcceptedInPasswords)) {

                viewBinding.tilNewPassword.setError(getResources().getString(R.string.change_password_invalid_new_password));
                viewBinding.tilNewPassword.setErrorIconDrawable(null);

            } else if (newPassword.equals(oldPasswordFromSQLite)) {

                viewBinding.tilNewPassword.setError(getResources().getString(R.string.change_password_new_old_password_same));
                viewBinding.tilNewPassword.setErrorIconDrawable(null);

            } else {
                // clear set error
                viewBinding.tilNewPassword.setError(null);
                viewBinding.tilNewPassword.setErrorIconDrawable(null);
                validNewPassword = true;
            }

            // Confirm
            if (confirmNewPassword.equals("")) {
                viewBinding.tilConfirmPassword.setError(getResources().getString(R.string.change_password_password_confirm));
                viewBinding.tilConfirmPassword.setErrorIconDrawable(null);

            } else if (!newPassword.equals(confirmNewPassword)) {
                viewBinding.tilConfirmPassword.setError(getResources().getString(R.string.change_password_password_not_match));
                viewBinding.tilConfirmPassword.setErrorIconDrawable(null);

            } else {
                // clear set error
                viewBinding.tilConfirmPassword.setError(null);
                validConfirmPassword = true;
            }

            // validate
            if (validOldPassword && validNewPassword && validConfirmPassword) {
                result = true;

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void updatePassword() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new UpdatePassword().execute();

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
                                viewBinding.btnUpdate.performClick();

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

    class UpdatePassword extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String appToken = "", baseURL = "", buildingId = "", tenantId = "", hostId = "0", hostType = "";
        private boolean saveUpdateStatus = false;

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

                    // Fetch login details
                    CS_Entity_LoginDetails loginModel = new CS_Action_LoginDetails(context).getLoginDetails();
                    if (loginModel != null) {
                        hostId = loginModel.getLD_SourceId();
                        hostType = CS_ED.Decrypt(loginModel.getLD_UserType());

                    }

                    JSONObject jObject = new JSONObject();
                    jObject.put("CountryCode", "");
                    jObject.put("MobileNo", "");
                    jObject.put("Password", newPassword);
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Change_Host_Password");
                    jObject.put("Logged_User_Id", hostId);
                    jObject.put("Logged_User_Type", hostType);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id", "0")
                            .addFormDataPart("Json_Data", String.valueOf(jObject))
                            .addFormDataPart("App_Token", appToken)
                            .build();

                    Request request = new Request.Builder()
                            .url(baseURL + CS_API_URL.Settings)
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
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            try {
                if (result.equals("1")) {

                    saveUpdateStatus = new CS_Action_LoginDetails(context).updatePassword(hostId, CS_ED.Encrypt(newPassword));

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
                    if (saveUpdateStatus) {
                        showAlertDialog("Success", R.drawable.ic_success, context.getResources().getString(R.string.change_password_success), "success");

                    } else {
                        showAlertDialog("Error", R.drawable.ic_error, context.getResources().getString(R.string.change_password_save_error), "");

                    }

                } else if (hostId.equals("")) {
                    showAlertDialog("Error", R.drawable.ic_error, context.getResources().getString(R.string.change_password_id_empty), "");

                } else if (baseURL.equals("")) {
                    showAlertDialog("Error", R.drawable.ic_error, CS_Constant.invalidBaseURL, result);

                } else {
                    showAlertDialog("Error", R.drawable.ic_error, !msg.equals("") ? msg : CS_Constant.serverConnectionErrorMessage, "");
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public void showAlertDialog(String title, int iconId, String message, String cameFrom) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setIcon(iconId);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("OK".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (cameFrom.equals("success")) {
                        requireActivity().onBackPressed();

                    }

                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchDataFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                oldPasswordFromSQLite = CS_ED.Decrypt(new CS_Action_LoginDetails(context).getLoginDetails().getLD_Password());

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return null;
        }
    }
}