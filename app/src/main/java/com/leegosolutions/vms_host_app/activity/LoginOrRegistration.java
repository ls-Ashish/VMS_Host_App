package com.leegosolutions.vms_host_app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.activity.registration.R_Registration;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginOrRegistrationBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

public class LoginOrRegistration extends AppCompatActivity {

    private Context context = LoginOrRegistration.this;
    private ActivityLoginOrRegistrationBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.LoginOrRegistration);
        try {
            viewBinding = ActivityLoginOrRegistrationBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            on_Click_Button_NewRegistration();
            on_Click_Button_Login();
            on_Click_Button_Scan();
//            displayBiometricButton();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Button_NewRegistration() {
        viewBinding.btnNewRegistration.setOnClickListener(v -> {
            try {
                nextPage(R_Registration.class);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void on_Click_Button_Login() {
        viewBinding.btnLogin.setOnClickListener(v -> {
            try {
                nextPage(Login.class);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void on_Click_Button_Scan() {
        viewBinding.btnScan.setOnClickListener(v -> {
            try {
                // https://stackoverflow.com/questions/53738969/face-authentication-to-unlock-my-app-programmatically
                if (checkBiometricAvailability()) {
                    BiometricPrompt.PromptInfo promptInfo = getBiometricPrompt();
                    if (promptInfo != null) {
                        getBiometricPromptHandler().authenticate(promptInfo);

                    } else {
                        new CS_Utility(context).showToast("PromptInfo is null", 0);
                    }
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private boolean checkBiometricAvailability() {
        boolean result = false;
        try {
            BiometricManager biometricManager = BiometricManager.from(context);
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    try {
                        // Proceed with biometric prompt
                        result = true;
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                    }
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    try {
                        // Handle the case where the device doesn't have biometric hardware
                        new CS_Utility(context).showToast("This device does not have biometric hardware.", 0);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                    }
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    try {
                        // Handle the case where biometric features are unavailable
                        new CS_Utility(context).showToast("Biometric features are unavailable.", 0);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                    }
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    try {
                        // Prompt the user to enroll biometrics in device settings
                        new CS_Utility(context).showToast("Enroll biometrics in device settings.", 0);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                        }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                    }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    private BiometricPrompt.PromptInfo getBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo = null;
        try {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Login with your biometric credential")
                    .setNegativeButtonText("Cancel")
                    .setConfirmationRequired(false)
                    .build();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return promptInfo;
    }

    private BiometricPrompt getBiometricPromptHandler() {
        BiometricPrompt biometricPrompt = null;
        try {
            biometricPrompt = new BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                    new BiometricPrompt.AuthenticationCallback() {

                        @Override
                        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                            super.onAuthenticationError(errorCode, errString);
                            new CS_Utility(context).showToast(String.valueOf(errString), 1);
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            onBiometricSuccess();
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            new CS_Utility(context).showToast("Biometric Authentication Failed", 0);
                        }
                    }
            );

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return biometricPrompt;
    }

    private void onBiometricSuccess() {
        try {
            new CS_Utility(context).showToast("Biometric Success", 0);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}