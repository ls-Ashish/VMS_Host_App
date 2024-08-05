package com.leegosolutions.vms_host_app.activity.settings.update;

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

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.database.join.CS_LoginDetailsWithAccessDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentSSettingsBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSUpdateMobileNoBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class S_UpdateMobileNoFragment extends Fragment {

    private Context context;
    private FragmentSUpdateMobileNoBinding viewBinding;
    private String name="", email="", company="", countryCode = "", mobileNo = "", temp_CountryCode = "", temp_MobileNo = "";

    public S_UpdateMobileNoFragment() {
        // Required empty public constructor
    }

    public S_UpdateMobileNoFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_s_update_mobile_no, container, false);
        try {
            viewBinding = FragmentSUpdateMobileNoBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            fetchDataFromSQLite();
            on_Click_Back_Arrow();
            on_Click_Button_Update();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void fetchDataFromSQLite() {
        try {
            new FetchDataFromSQLite().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchDataFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CS_LoginDetailsWithAccessDetails model = new CS_Action_LoginDetails(context).getLoginDetailsWithAccessDetails();

                if (model != null) {
                    CS_Entity_LoginDetails loginModel = model.getEntity_loginDetails();
                    CS_Entity_AccessDetails accessModel = model.getEntity_accessDetails();

                    if (loginModel != null) {
                        name = CS_ED.Decrypt(loginModel.getLD_UserName());
                        countryCode = temp_CountryCode = CS_ED.Decrypt(loginModel.getLD_CountryCode());
                        mobileNo = temp_MobileNo = CS_ED.Decrypt(loginModel.getLD_MobileNo());
                        email = CS_ED.Decrypt(loginModel.getLD_Email());
                    }

                    if (accessModel != null) {
                        company = CS_ED.Decrypt(accessModel.getAD_E_Unit());
                    }
                }

                // Login
//                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
//                if (model != null) {
//                    name = CS_ED.Decrypt(model.getLD_UserName());
//                    email = CS_ED.Decrypt(model.getLD_Email());
////                    company = CS_ED.Decrypt(model.get);
//
//                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                setData();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void setData() {
        try {
            viewBinding.etName.setText(name);
            viewBinding.countryCodePicker.setCountryForPhoneCode(Integer.valueOf(countryCode));
            viewBinding.etMobileNo.setText(mobileNo);
            viewBinding.etEmail.setText(email);
            viewBinding.etCompany.setText(company);

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
                        updateMobileNo();
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
            boolean validCountryCode = false, validMobileNo = false;

            countryCode = viewBinding.countryCodePicker.getSelectedCountryCode().trim();
            mobileNo = viewBinding.etMobileNo.getText().toString().trim();


            if (countryCode.equals(temp_CountryCode) && mobileNo.equals(temp_MobileNo)) {
                // no data change
                new CS_Utility(context).showToast(getResources().getString(R.string.update_mobile_error_same), 0);

            } else {
                // Country Code
                if (countryCode.equals("")) {
                    new CS_Utility(context).showToast(getResources().getString(R.string.update_mobile_no_error_country_code_blank_update), 0);

                } else {
                    validCountryCode = true;
                }

                // Mobile No.
                if (mobileNo.equals("")) {
                    viewBinding.tilMobileNo.setError(getResources().getString(R.string.update_mobile_no_error_mobile_no_blank));

                } else {
                    // clear set error
                    viewBinding.tilMobileNo.setError(null);
                    validMobileNo = true;
                }

                // validate
                if (validCountryCode && validMobileNo) {
                    result = true;
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void updateMobileNo() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new UpdateMobileNo().execute();

            } else {
                showSnackbar();
            }

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
                                updateMobileNo();

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

    class UpdateMobileNo extends AsyncTask<Void, Void, Void> {

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
                    jObject.put("CountryCode", countryCode);
                    jObject.put("MobileNo", mobileNo);
                    jObject.put("Password", "");
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Update_Host_Mobile_No");
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

                    saveUpdateStatus = new CS_Action_LoginDetails(context).updateMobileNo(hostId, CS_ED.Encrypt(countryCode), CS_ED.Encrypt(mobileNo));

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
                        showAlertDialog("Success", R.drawable.ic_success, context.getResources().getString(R.string.update_mobile_no_change_success), "success");

                    } else {
                        showAlertDialog("Error", R.drawable.ic_error, context.getResources().getString(R.string.update_mobile_save_error), "");

                    }

                } else if (hostId.equals("")) {
                    showAlertDialog("Error", R.drawable.ic_error, context.getResources().getString(R.string.update_mobile_id_empty), "");

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