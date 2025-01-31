package com.leegosolutions.vms_host_app.activity.login.forgot_password;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailForgotPasswordBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailForgotPasswordVerifyBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class L_EmailForgotPasswordVerify extends Fragment {

    private Context context;
    private FragmentLEmailForgotPasswordVerifyBinding viewBinding;
    private String email = "", verificationCode="";

    public L_EmailForgotPasswordVerify() {
        // Required empty public constructor
    }

    public L_EmailForgotPasswordVerify(Context context) {
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
//        return inflater.inflate(R.layout.fragment_l__email_forgot_password_verify, container, false);
        try {
            viewBinding = FragmentLEmailForgotPasswordVerifyBinding.inflate(inflater, container, false);

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
            on_Click_Back_Arrow();
            on_Click_Button_Next();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getBundle() {
        try {
            //Retrieve the value
            if (getArguments() != null) {
                email = getArguments().getString("email");
                verificationCode = getArguments().getString("verificationCode");
            }

            String text_1 = requireActivity().getResources().getString(R.string.app_forgot_password_verify_verification_code_1);
            String text_2 = requireActivity().getResources().getString(R.string.app_forgot_password_verify_verification_code_2);

            viewBinding.tvPageInfo1.setText(text_1 + " " + email + text_2);

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
                    requireActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void on_Click_Button_Next() {
        viewBinding.btnNext.setOnClickListener(v -> {
            try {
                if (validate()) {
                    next();
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private boolean validate() {
        boolean result = false;
        try {
            boolean validCode = false;

            String enteredVerificationCode = viewBinding.etVCode.getText().toString().trim();

            // Email
            if (enteredVerificationCode.equals("")) {
                viewBinding.tilEmail.setError(getResources().getString(R.string.app_forgot_password_verify_blank_code));

            } else if (enteredVerificationCode.equals(verificationCode)) {
                // clear set error
                viewBinding.tilEmail.setError(null);
                validCode = true;

            } else {
                viewBinding.tilEmail.setError(getResources().getString(R.string.app_forgot_password_verify_invalid_code));
            }

            // validate
            if (validCode) {
                result = true;
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return result;
    }

    private void next() {
        try {
            loadFragment(new L_EmailNewPassword(context));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void loadFragment(Fragment fragment) {
        try {
            Bundle args = new Bundle();
//            args.putString("email", email);
//            args.putString("verificationCode", verificationCode);

//            Fragment fragment = null;

//            if (cameFrom.equals("L_EmailFragment")) {
//                fragment = new L_EmailVerifyFragment(context);
//
//            } else if (cameFrom.equals("L_MobileNoFragment")) {
//                fragment = new L_EmailForgotPassword(context);

//            }

            if (fragment != null) {

                // Clears the entire back stack of fragments
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragment.setArguments(args);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            } else {
                new CS_Utility(context).showToast("Fragment is null", 0);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}