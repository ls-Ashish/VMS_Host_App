package com.leegosolutions.vms_host_app.activity.login.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailVerifyBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.email.CS_SendEmail;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class L_EmailVerifyFragment extends Fragment implements CS_SendEmail.ResultListener {

    private Context context;
    private FragmentLEmailVerifyBinding viewBinding;
    private CountDownTimer otpCountDownTimer;
    private boolean isRunning = false, isExpired = false;
    private long timeRemainingInMinutes = 0;
    private String verificationCode = "", email = "", emailBody = "";
    private byte[] logo = null;
    private final long resendDurationInMinutes = 5;
    private final long countDownTimeInMinutes = 1;
    private Handler handler = null;
    private Runnable myRunnable = null;

    public L_EmailVerifyFragment() {
        // Required empty public constructor
    }

    public L_EmailVerifyFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_l_email_verify, container, false);
        try {
            viewBinding = FragmentLEmailVerifyBinding.inflate(inflater, container, false);

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
            new FetchLogoFromSQLite().execute();
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
                email = getArguments().getString("email");
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

    private void sendEmail() {
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

                    emailBody = "<div style='margin: 0; padding: 0;'>" + "<table border='1' cellpadding='0' cellspacing='0' width='450' align='center' bgcolor='#fff' style='background-color:#fff;'>" + "<tr>" + "<td style='padding:5px 30px;'>";

                    if (logo != null) {
                        String _ImageId = "img_buildingLogo";
                        emailBody += "<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='table-layout: fixed;min-width:100%;'>" + "<tr>" + "<td align='center' style='padding: 20px 0 0 0;'>" + "<img src='cid:" + _ImageId + "' alt='' height='auto' width='125' style='height:auto; width:125;' border='0'>" + "</td>" + "</tr>" + "</table>";
                    }

                    emailBody += "<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='table-layout: fixed;'>" + "<tr>" + "<td style='padding: 20px 0 20px 0;line-height: 1.5;'>" + message + "</td>" + "</tr>" + "</table>";

                    emailBody += "</td>" + "</tr>" + "</table>" + "</div>";

                    isExpired = false;
                    new CS_SendEmail(context, L_EmailVerifyFragment.this).sendEmail(logo, email, "", getResources().getString(R.string.login_email_verification_code_subject), emailBody);

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
                    new UpdateLoginDetails().execute();
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
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
            if (status.equals("success")) {
                new CS_Utility(context).showToast(getResources().getString(R.string.login_email_verification_code_send), 0);

            } else {
                new CS_Utility(context).showToast(status, 1);
                viewBinding.tvExpiredIn.setVisibility(View.INVISIBLE);
                viewBinding.tvResendVCode.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }


    private class FetchLogoFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... strings) {
            try {
                if (logo == null) {
                    logo = new CS_Action_ServerDetails(context).getServerDetails().getSD_Logo();
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
                sendEmail();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
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

}