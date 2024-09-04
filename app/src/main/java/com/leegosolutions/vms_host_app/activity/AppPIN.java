package com.leegosolutions.vms_host_app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewbinding.ViewBinding;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.TestingCode;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.settings.security.S_Set_PIN;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.databinding.ActivityAppPinBinding;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginBinding;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AppPIN extends AppCompatActivity {

    private Context context = AppPIN.this;
    private String appPINStatus = "", appPIN = "", userEntered = "";
    private ActivityAppPinBinding viewBinding;
    private final int PIN_LENGTH = 6;
    private boolean keyPadLockedFlag = false;
    private TextView[] pinBoxArray;
    private long keypadLockDuration = 30000; // 30 seconds
    private int wrongPINCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_app_pin);
        try {
            // Full screen
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            viewBinding = ActivityAppPinBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            showHidePage(false);
            getIntentData();
            setPinBox();
            on_Click_Backspace_Button();
            on_Click_Biometric_Icon();
//            showRandomKeypad();

            View.OnClickListener pinButtonHandler = new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if (keyPadLockedFlag == true) {
                            return;
                        }

                        Button pressedButton = (Button) v;

                        if (userEntered.length() < PIN_LENGTH) {

                            userEntered = userEntered + pressedButton.getText();
                            Log.v("PinView", "User entered=" + userEntered);

                            //Update pin boxes
                            pinBoxArray[userEntered.length() - 1].setText("*");
//                            pinBoxArray[userEntered.length() - 1].setTransformationMethod(PasswordTransformationMethod.getInstance());

                            if (userEntered.length() == PIN_LENGTH) {
                                //Check if entered PIN is correct
                                if (userEntered.equals(appPIN)) {
                                    viewBinding.tvPINMessage.setTextColor(Color.parseColor("#387F39"));
                                    viewBinding.tvPINMessage.setText(getResources().getString(R.string.app_pin_correct));
                                    success();

                                } else {
                                    wrongPINCounter++;

                                    viewBinding.tvPINMessage.setTextColor(Color.parseColor("#A02334"));

//                                    if (wrongPINCounter == 5) {
//                                        viewBinding.tvPINMessage.setText(getResources().getText(R.string.app_pin_error_invalid_lock));
//
//                                        wrongPINCounter = 0;
//                                        keyPadLockedFlag = true;
//                                        lockKeyPadOperation();
//
//                                    } else {
                                        viewBinding.tvPINMessage.setText(getResources().getText(R.string.app_pin_error_invalid));

//                                    }
                                }
                            }
                        } else {
                            //Roll over
                            pinBoxArray[0].setText("-");
                            pinBoxArray[1].setText("-");
                            pinBoxArray[2].setText("-");
                            pinBoxArray[3].setText("-");
                            pinBoxArray[4].setText("-");
                            pinBoxArray[5].setText("-");

                            userEntered = "";

                            viewBinding.tvPINMessage.setText("");

                            userEntered = userEntered + pressedButton.getText();

                            //Update pin boxes
                            pinBoxArray[userEntered.length() - 1].setText("*");
//                            pinBoxArray[userEntered.length() - 1].setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            };

            viewBinding.btn0.setOnClickListener(pinButtonHandler);
            viewBinding.btn1.setOnClickListener(pinButtonHandler);
            viewBinding.btn2.setOnClickListener(pinButtonHandler);
            viewBinding.btn3.setOnClickListener(pinButtonHandler);
            viewBinding.btn4.setOnClickListener(pinButtonHandler);
            viewBinding.btn5.setOnClickListener(pinButtonHandler);
            viewBinding.btn6.setOnClickListener(pinButtonHandler);
            viewBinding.btn7.setOnClickListener(pinButtonHandler);
            viewBinding.btn8.setOnClickListener(pinButtonHandler);
            viewBinding.btn9.setOnClickListener(pinButtonHandler);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
//            finishAffinity();
            //App not allowed to go back to Parent activity until correct pin entered.
            return;
            //super.onBackPressed();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            if (getIntent().getExtras() != null) {

                if (intent.getStringExtra("appPINStatus") != null) {
                    appPINStatus = intent.getStringExtra("appPINStatus");
                }

                if (intent.getStringExtra("appPIN") != null) {
                    appPIN = intent.getStringExtra("appPIN");
                }

                if (intent.getStringExtra("message") != null) {
                    String message = intent.getStringExtra("message");
                    viewBinding.tvMessage.setText(message);
                }

                if (intent.getStringExtra("appBiometricStatus") != null) {
                    String appBiometricStatus = intent.getStringExtra("appBiometricStatus");
                    if (appBiometricStatus.equals("1")) {

                        showHidePage(false);
                        showBiometricPrompt();

                    } else {
                        showHidePage(true);
                        viewBinding.ivUseBiometrics.setVisibility(View.INVISIBLE);
                    }
                }
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setPinBox() {
        try {
            pinBoxArray = new TextView[PIN_LENGTH];
            pinBoxArray[0] = viewBinding.tvPinBox1;
            pinBoxArray[1] = viewBinding.tvPinBox2;
            pinBoxArray[2] = viewBinding.tvPinBox3;
            pinBoxArray[3] = viewBinding.tvPinBox4;
            pinBoxArray[4] = viewBinding.tvPinBox5;
            pinBoxArray[5] = viewBinding.tvPinBox6;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void success() {
        try {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", "1");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

//            new CS_Utility(context).showToast("Success", 0);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showBiometricPrompt() {
        try {
            String negativeButtonText = "Cancel";
            if (appPINStatus.equals("1")) {
                negativeButtonText = "Use PIN";
            }

            BiometricPrompt.PromptInfo promptInfo = getBiometricPrompt(negativeButtonText);
            if (promptInfo != null) {
                getBiometricPromptHandler().authenticate(promptInfo);

            } else {
                new CS_Utility(context).showToast(getResources().getString(R.string.app_pin_biometric_error_prompt), 0);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private BiometricPrompt.PromptInfo getBiometricPrompt(String negativeButtonText) {
        BiometricPrompt.PromptInfo promptInfo = null;
        try {
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getResources().getString(R.string.app_pin_biometric_prompt_title))
                    .setSubtitle(getResources().getString(R.string.app_pin_biometric_prompt_subtitle))
                    .setNegativeButtonText(negativeButtonText)
                    .setConfirmationRequired(false)
//                    .setDescription("setDescription")
                    .build();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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
                            try {
                                showHidePage(true);
                                if (errString.equals("Use PIN")) {

                                } else {
//                                    finishAffinity(); // Close the current activity and all activities below it in the task
                                    new CS_Utility(context).showToast(String.valueOf(errString), 1);
                                }

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                            super.onAuthenticationSucceeded(result);
                            try {
                                onBiometricSuccess();

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            super.onAuthenticationFailed();
                            try {
                                new CS_Utility(context).showToast(getResources().getString(R.string.app_pin_biometric_error_authentication), 0);

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }
                    }
            );

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return biometricPrompt;
    }

    private void onBiometricSuccess() {
        try {
            success();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void exitApp() {
        try {
            //Exit app
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            context.startActivity(i);
            finish();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Backspace_Button() {
        viewBinding.ivBackspace.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {
                     try {
                         if (keyPadLockedFlag == true) {
                             return;
                         }

                         if (userEntered.length() > 0) {
                             userEntered = userEntered.substring(0, userEntered.length() - 1);
                             pinBoxArray[userEntered.length()].setText("-");

                             viewBinding.tvPINMessage.setText("");
                         }

                     } catch (Exception e) {
                         new CS_Utility(context).saveError(e);
                     }
                 }
             }
        );
    }

    private void on_Click_Biometric_Icon() {
        viewBinding.ivUseBiometrics.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {
                     try {
                         showHidePage(false);
                         showBiometricPrompt();

                     } catch (Exception e) {
                         new CS_Utility(context).saveError(e);
                     }
                 }
             }
        );
    }

    private void lockKeyPadOperation() {
        try {
            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // DelayedCode - This code is executed after 30 seconds

                        viewBinding.tvPINMessage.setText("");

                        //Roll over
                        pinBoxArray[0].setText("-");
                        pinBoxArray[1].setText("-");
                        pinBoxArray[2].setText("-");
                        pinBoxArray[3].setText("-");
                        pinBoxArray[4].setText("-");
                        pinBoxArray[5].setText("-");

                        userEntered = "";

                        keyPadLockedFlag = false;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                }
            };
            handler.postDelayed(runnable, keypadLockDuration);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showRandomKeypad() {
        try {
            List<String> numbers = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                numbers.add(String.valueOf(i));
            }

            Collections.shuffle(numbers);

            viewBinding.btn0.setText(numbers.get(0));
            viewBinding.btn1.setText(numbers.get(1));
            viewBinding.btn2.setText(numbers.get(2));
            viewBinding.btn3.setText(numbers.get(3));
            viewBinding.btn4.setText(numbers.get(4));
            viewBinding.btn5.setText(numbers.get(5));
            viewBinding.btn6.setText(numbers.get(6));
            viewBinding.btn7.setText(numbers.get(7));
            viewBinding.btn8.setText(numbers.get(8));
            viewBinding.btn9.setText(numbers.get(9));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showHidePage(boolean status) {
        try {
            if (status) {
                viewBinding.rlMain.setVisibility(View.VISIBLE);

            } else {
                viewBinding.rlMain.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}