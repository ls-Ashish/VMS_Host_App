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
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.activity.notifications.N_NotificationsFragment;
import com.leegosolutions.vms_host_app.activity.settings.S_SettingsFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailVerifyBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentLMobileNoVerifyBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.email.CS_SendEmail;
import com.leegosolutions.vms_host_app.utility.sms.CS_SendSMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class L_MobileNoVerifyFragment extends Fragment implements CS_SendSMS.ResultListener {

    private Context context;
    private FragmentLMobileNoVerifyBinding viewBinding;
    private CountDownTimer otpCountDownTimer;
    private boolean isRunning = false, isExpired = false;
    private long timeRemainingInMinutes = 0;
    private String verificationCode = "", mobileNo = "", cameFrom = "", countryCode = "", mobileCode = "";
    private final long resendDurationInMinutes = 5;
    private final long countDownTimeInMinutes = 1;
    private Handler handler = null;
    private Runnable myRunnable = null;

    public L_MobileNoVerifyFragment() {
        // Required empty public constructor
    }

    public L_MobileNoVerifyFragment(Context context) {
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
            stopTask();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            viewBinding = null; // Clear binding to avoid memory leaks
            stopTask();

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
//        return inflater.inflate(R.layout.fragment_l__mobile_no_verify, container, false);
        try {
            viewBinding = FragmentLMobileNoVerifyBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getBundle();
            showResendButton();
            setExpiredText();
            sendSMS();
            on_Click_Button_Resend();
            on_Click_Button_Submit();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getBundle() {
        try {
            //Retrieve the value
            if (getArguments() != null) {

                cameFrom = getArguments().getString("cameFrom");
                mobileNo = getArguments().getString("mobileNo");

                if (cameFrom.equals("settings")) {

                    countryCode = getArguments().getString("countryCode");

                    mobileCode = mobileNo;
                    mobileNo = countryCode + mobileNo;

                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setExpiredText() {
        try {
            String text_1 = getResources().getString(R.string.login_email_verify_verification_code_expiry_text_1);
            String text_2 = getResources().getString(R.string.login_email_verify_verification_code_expiry_text_2);
            viewBinding.tvExpiredIn.setText(text_1 + " " + String.valueOf(resendDurationInMinutes) + " " + text_2);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showResendButton() {
        try {
            // Show resend button after 1 minute
            viewBinding.tvResendVCode.setVisibility(View.INVISIBLE);

            handler = new Handler(Looper.getMainLooper());
            myRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // Code to be executed after 1 minute
                        viewBinding.tvResendVCode.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            };
            handler.postDelayed(myRunnable, 60000); // 60000 milliseconds = 1 minute

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void sendSMS() {
        try {
            if (!isRunning) {
                generateAndSendVerificationCode();

            } else {
                String text_1 = getResources().getString(R.string.login_retry_text_1);
                String text_2 = getResources().getString(R.string.login_retry_text_2);
                new CS_Utility(context).showToast(text_1 + " " + (timeRemainingInMinutes + 1) + " " + text_2, 0);
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void generateAndSendVerificationCode() {
        try {
            if (new CS_Connection(context).getStatus()) {
                try {
                    // Generate OTP
                    Random random = new Random();
                    verificationCode = String.format("%06d", random.nextInt(1000000));

                    // Send Verification Code
                    String text_1 = getResources().getString(R.string.login_verification_code_text_1);
                    String text_2 = getResources().getString(R.string.login_verification_code_text_2);
                    String text_3 = getResources().getString(R.string.login_verification_code_text_3);
                    String message = text_1 + " " + verificationCode + " " + text_2 + " " + String.valueOf(resendDurationInMinutes) + " " + text_3;

                    isExpired = false;
                    new CS_SendSMS(context, L_MobileNoVerifyFragment.this).sendSMS(mobileNo, message);

                    otpCountDownTimer = new CountDownTimer(TimeUnit.MINUTES.toMillis(resendDurationInMinutes), TimeUnit.MINUTES.toMillis(countDownTimeInMinutes)) {

                        public void onTick(long millisUntilFinished) {
                            try {
                                isRunning = true;
                                timeRemainingInMinutes = millisUntilFinished / TimeUnit.MINUTES.toMillis(countDownTimeInMinutes);

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }

                        public void onFinish() {
                            try {
                                isRunning = false;
                                isExpired = true;

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            } else {
                try {
                    new CS_Utility(context).showToast(CS_Constant.serverConnectionErrorMessage, 0);

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Button_Resend() {
        viewBinding.tvResendVCode.setOnClickListener(v -> {
            try {
                if (!isRunning) {
                    // Clear
                    viewBinding.etVCode.setText("");
                    generateAndSendVerificationCode();
                    // Again hide for 1 min
                    showResendButton();

                } else {
                    String text_1 = getResources().getString(R.string.login_retry_text_1);
                    String text_2 = getResources().getString(R.string.login_retry_text_2);
                    new CS_Utility(context).showToast(text_1 + " " + (timeRemainingInMinutes + 1) + " " + text_2, 0);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void on_Click_Button_Submit() {
        viewBinding.btnSubmit.setOnClickListener(v -> {
            try {
                if (validate()) {
                    stopTask();
                    if (cameFrom.equals("login")) {
                        new UpdateLoginDetails().execute();

                    } else if (cameFrom.equals("settings")) {
                        if (new CS_Connection(context).getStatus()) {
                            new UpdateMobileNo().execute();

                        } else {
                            showSnackbar();
                        }

                    } else {
                        new CS_Utility(context).showToast("cameFrom : " + cameFrom, 0);

                    }
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                viewBinding.btnSubmit.performClick();

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

    private boolean validate() {
        boolean result = false;
        try {
            String codeToVerify = viewBinding.etVCode.getText().toString().trim();

            // Entered verification code
            if (codeToVerify.equals("")) {
                viewBinding.tilVCode.setError(getResources().getString(R.string.login_verification_code_enter));

            } else if (codeToVerify.equals(verificationCode)) {

                if (!isExpired) {
                    // clear set error
                    viewBinding.tilVCode.setError(null);
                    result = true;

                } else {
                    viewBinding.tilVCode.setError(getResources().getString(R.string.login_verification_code_expired));
                }

            } else {
                viewBinding.tilVCode.setError(getResources().getString(R.string.login_verification_code_invalid));
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    @Override
    public void status(String status) {
        try {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (status.equals("success")) {
                            new CS_Utility(context).showToast(getResources().getString(R.string.login_mobile_verification_code_send), 0);

                        } else {
                            new CS_Utility(context).showToast(status, 1);
                            viewBinding.tvExpiredIn.setVisibility(View.INVISIBLE);
                            viewBinding.tvResendVCode.setVisibility(View.INVISIBLE);
                        }

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            });
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private class UpdateLoginDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                result = new CS_Action_LoginDetails(context).setIsLogin(1);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                if (result) {
                    goToHomePage();
                    new CS_Utility(context).showToast(getResources().getString(R.string.login_success), 1);

                } else {

                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
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

    private void stopTask() {
        try {
            if (handler != null) {
                handler.removeCallbacks(myRunnable); // Cancel the task
            }

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
                    jObject.put("MobileNo", mobileCode);
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

                    saveUpdateStatus = new CS_Action_LoginDetails(context).updateMobileNo(hostId, CS_ED.Encrypt(countryCode), CS_ED.Encrypt(mobileCode));

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
                        gotoSettings();

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

    private void gotoSettings() {
        try {
            Fragment fragment = new S_SettingsFragment(context);

            if (fragment != null) {

                // Clears the entire back stack of fragments
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();

            } else {
                new CS_Utility(context).showToast("Fragment is null", 0);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}