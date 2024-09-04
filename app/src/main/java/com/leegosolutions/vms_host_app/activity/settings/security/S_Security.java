package com.leegosolutions.vms_host_app.activity.settings.security;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.ScanServerDetails;
import com.leegosolutions.vms_host_app.activity.Splash;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentSSecurityBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSSettingsBinding;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class S_Security extends Fragment {

    private Context context;
    private FragmentSSecurityBinding viewBinding;

    public S_Security() {
        // Required empty public constructor
    }

    public S_Security(Context context) {
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
            viewBinding = FragmentSSecurityBinding.inflate(inflater, container, false);

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
            fetchSecuritySettings();
            onClick_Button_Set_Pin();
            onClick_Button_Set_Fingerprint();

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

    private void onClick_Button_Set_Pin() {
        viewBinding.llSecurityPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_Set_PIN(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void onClick_Button_Set_Fingerprint() {
        viewBinding.swFingerprintStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        if (checkBiometricAvailability()) {
                            enableFingerprint(isChecked);

                        } else {
                            viewBinding.swFingerprintStatus.setChecked(false);
                        }
                    } else {
                        enableFingerprint(isChecked);
                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void fetchSecuritySettings() {
        try {
            new FetchSecuritySettings().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchSecuritySettings extends AsyncTask<Void, Void, Void> {

        String pinStatus="", pin="", fingerprintStatus="";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
                if (model != null) {
                    pinStatus = model.getLD_S_PIN_Status();
                    pin = CS_ED.Decrypt(model.getLD_S_PIN());
                    fingerprintStatus = model.getLD_S_Fingerprint_Status();
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
                if (pinStatus.equals("1")) {
                    viewBinding.tvPINStatus.setText(requireActivity().getResources().getString(R.string.status_on));

                } else {
                    viewBinding.tvPINStatus.setText(requireActivity().getResources().getString(R.string.status_off));
                }

                // Fingerprint
                if (fingerprintStatus.equals("1")) {
                    viewBinding.swFingerprintStatus.setChecked(true);

                } else {
                    viewBinding.swFingerprintStatus.setChecked(false);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void enableFingerprint(boolean status) {
        try {
            new EnableFingerprint().execute(status ? "1" : "0");

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class EnableFingerprint extends AsyncTask<String, Void, Void> {

        private boolean result = false;
        private String sourceId = "";

        @Override
        protected Void doInBackground(String... strings) {
            try {
                CS_Entity_LoginDetails model = new CS_Action_LoginDetails(context).getLoginDetails();
                if (model != null) {
                    sourceId = model.getLD_SourceId();
                }

                if (!sourceId.equals("")) {
                    String status = strings[0];
                    result = new CS_Action_LoginDetails(context).enableFingerprint(sourceId, status);
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
                // Check
                if (result) {
//                    new CS_Utility(context).showToast("", 0);

                } else if (sourceId.equals("")) {
                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_source_id_error), 1);

                } else {
                    new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_save_error), 1);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
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
                        new CS_Utility(context).saveError(e);
                    }
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    try {
                        // Handle the case where the device doesn't have biometric hardware
                        new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_biometric_error_1), 1);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    try {
                        // Handle the case where biometric features are unavailable
                        new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_biometric_error_2), 1);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    try {
                        // Prompt the user to enroll biometrics in device settings
                        new CS_Utility(context).showToast(requireActivity().getResources().getString(R.string.security_pin_biometric_error_3), 1);
                        break;

                    } catch (Exception e) {
                        new CS_Utility(context).saveError(e);
                    }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void goToNextFragment(Fragment fragment) {
        try {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}