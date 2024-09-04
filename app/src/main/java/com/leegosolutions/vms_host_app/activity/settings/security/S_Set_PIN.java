package com.leegosolutions.vms_host_app.activity.settings.security;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.settings.change_password.S_ChangePassword;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentSSecurityBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSSetPINBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class S_Set_PIN extends Fragment {

    private Context context;
    private FragmentSSetPINBinding viewBinding;
    private String appPINStatus = "", appPIN = "", newPinToUpdate = "", appPINStatus_Initial = "";

    public S_Set_PIN() {
        // Required empty public constructor
    }

    public S_Set_PIN(Context context) {
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
//        return inflater.inflate(R.layout.fragment_s_settings, container, false);
        try {
            viewBinding = FragmentSSetPINBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            on_Click_Back_Arrow();
            fetchAppPIN();
            onToggle_Change_PIN();
            onClick_Button_Save_Pin();

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

    private void onToggle_Change_PIN() {
        viewBinding.swEnablePIN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
//                    if (isChecked) {
//
//                        if (!appPIN.equals("")) {
//                            // Pin never set
//                            viewBinding.tvEnterOldPinCaption.setVisibility(View.VISIBLE);
//                            viewBinding.tilOldPin.setVisibility(View.VISIBLE);
//                        }
//
//                        viewBinding.tvEnterNewPinCaption.setVisibility(View.VISIBLE);
//                        viewBinding.tilNewPin.setVisibility(View.VISIBLE);
//
//                    } else {
//                        viewBinding.tvEnterOldPinCaption.setVisibility(View.GONE);
//                        viewBinding.tilOldPin.setVisibility(View.GONE);
//
////                        viewBinding.tvEnterNewPinCaption.setVisibility(View.GONE);
////                        viewBinding.tilNewPin.setVisibility(View.GONE);
//
//                        viewBinding.tvEnterNewPinCaption.setText("");
//
//                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void onClick_Button_Save_Pin() {
        viewBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validate()) {
                        updatePIN();
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
            boolean validOldPin = false, validNewPin = false;

            if (viewBinding.swEnablePIN.isChecked()) {

                String oldPin = viewBinding.etOldPin.getText().toString().trim();
                newPinToUpdate = viewBinding.etNewPin.getText().toString().trim();

                // New
                if (newPinToUpdate.equals("")) {
                    viewBinding.tilNewPin.setError(getResources().getString(R.string.security_s_enter_new_pin));
                    viewBinding.tilNewPin.setErrorIconDrawable(null);

                } else if (newPinToUpdate.length() < 6) {
                    viewBinding.tilNewPin.setError(getResources().getString(R.string.security_s_invalid_new_pin));
                    viewBinding.tilNewPin.setErrorIconDrawable(null);

                } else if (newPinToUpdate.equals(appPIN) && !appPIN.equals("")) {

                    viewBinding.tilNewPin.setError(getResources().getString(R.string.security_s_pin_new_old_pin_same));
                    viewBinding.tilNewPin.setErrorIconDrawable(null);

                } else {
                    // clear set error
                    viewBinding.tilNewPin.setError(null);
                    viewBinding.tilNewPin.setErrorIconDrawable(null);
                    validNewPin = true;
                }

                // Old
                if (appPIN.equals("")) {
                    // Pin never set
                    validOldPin = true;

                } else {
                    if (oldPin.equals("")) {
                        viewBinding.tilOldPin.setError(getResources().getString(R.string.security_s_enter_old_pin));
                        viewBinding.tilOldPin.setErrorIconDrawable(null);

                    } else if (!oldPin.equals(appPIN)) {
                        viewBinding.tilOldPin.setError(getResources().getString(R.string.security_s_pin_old_pin_did_not_match));
                        viewBinding.tilOldPin.setErrorIconDrawable(null);

                    } else {
                        // clear set error
                        viewBinding.tilOldPin.setError(null);
                        viewBinding.tilOldPin.setErrorIconDrawable(null);
                        validOldPin = true;
                    }
                }
            } else if (appPINStatus_Initial.equals("1") ){
                result = true;
            }

            // validate
            if (validNewPin && validOldPin) {
                result = true;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void fetchAppPIN() {
        try {
            new FetchAppPIN().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchAppPIN extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
                if (model != null) {
                    appPINStatus_Initial = appPINStatus = model.getLD_S_PIN_Status();
                    appPIN = CS_ED.Decrypt(model.getLD_S_PIN());
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
                // Set data

                // PIN
                if (appPINStatus.equals("1")) {
                    viewBinding.btnSave.setText(requireActivity().getResources().getString(R.string.security_s_pin_button_update));
                    viewBinding.swEnablePIN.setChecked(true);

                } else {
//                    viewBinding.trOnOff.setVisibility(View.GONE);
                }

                if (appPIN.equals("")) {
                    // Pin never set
                    viewBinding.tvEnterOldPinCaption.setVisibility(View.GONE);
                    viewBinding.tilOldPin.setVisibility(View.GONE);

                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void updatePIN() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new UpdateAppPIN().execute();

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
                                viewBinding.btnSave.performClick();

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

//    public class UpdatePIN extends AsyncTask<String, Void, Void> {
//
//        private boolean result = false;
//        private String sourceId = "";
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            try {
//                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
//                if (model != null) {
//                    sourceId = model.getLD_SourceId();
//                }
//
//                if (!sourceId.equals("")) {
//
//                    result = new CS_Action_LoginDetails(context).setPIN(sourceId, viewBinding.swEnablePIN.isChecked() ? "1" : "0"
//                            , viewBinding.swEnablePIN.isChecked() ? CS_ED.Encrypt(newPinToUpdate) : "");
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
//                // Check
//                if (result) {
//                    fetchAppPIN();
//                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_s_pin_change_success), 0);
//
//                } else if (sourceId.equals("")) {
//                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_source_id_error), 1);
//
//                } else {
//                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_save_error), 1);
//                }
//
//            } catch (Exception e) {
//                new CS_Utility(context).saveError(e);
//            }
//        }
//    }

    class UpdateAppPIN extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String appToken = "", baseURL = "", buildingId = "", tenantId = "", hostId = "0", hostType = "", sourceId = "";
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
//                if (viewBinding.swEnablePIN.isChecked()) {
                    // Update in portal
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
                            jObject.put("Password", newPinToUpdate);
                            jObject.put("Logged_BU_Id", buildingId);
                            jObject.put("Logged_TE_Id", tenantId);
                            jObject.put("Flag", "Update_HostApp_PIN");
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
//                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            try {
//                if ((viewBinding.swEnablePIN.isChecked() && result.equals("1")) || !viewBinding.swEnablePIN.isChecked()) {
                if (result.equals("1")) {

                    CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
                    if (model != null) {
                        sourceId = model.getLD_SourceId();
                    }

                    if (!sourceId.equals("")) {

                        saveUpdateStatus = new CS_Action_LoginDetails(context).setPIN(sourceId, viewBinding.swEnablePIN.isChecked() ? "1" : "0"
                                , viewBinding.swEnablePIN.isChecked() ? CS_ED.Encrypt(newPinToUpdate) : "");
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
                progressdialog.dismiss();

//                    if ((viewBinding.swEnablePIN.isChecked() && result.equals("1")) || !viewBinding.swEnablePIN.isChecked()) {
                if (result.equals("1")) {
                    if (saveUpdateStatus) {

                        if (appPINStatus_Initial.equals("1")) {

                            if (viewBinding.swEnablePIN.isChecked()) {
                                showAlertDialog("Status", R.drawable.ic_success, context.getResources().getString(R.string.security_s_pin_change_success), "success");

                            } else {
                                showAlertDialog("Status", R.drawable.ic_success, context.getResources().getString(R.string.security_s_pin_success ), "success");
                            }

                        } else {
                            if (viewBinding.swEnablePIN.isChecked()) {
                                showAlertDialog("Status", R.drawable.ic_success, context.getResources().getString(R.string.security_s_pin_set), "success");

                            } else {
                                showAlertDialog("Status", R.drawable.ic_success, context.getResources().getString(R.string.security_s_pin_success ), "success");
                            }
                        }

                    } else {
                        showAlertDialog("Error", R.drawable.ic_error, context.getResources().getString(R.string.security_s_pin_save_error), "");

                    }

                } else if (sourceId.equals("")) {
                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_source_id_error), 1);

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

}