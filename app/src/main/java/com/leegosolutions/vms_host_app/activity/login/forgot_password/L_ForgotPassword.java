package com.leegosolutions.vms_host_app.activity.login.forgot_password;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.databinding.ActivityLForgotPasswordBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class L_ForgotPassword extends AppCompatActivity {

    private Context context = L_ForgotPassword.this;
    private ActivityLForgotPasswordBinding viewBinding;
    private String email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_l_forgot_password);
        try {
            viewBinding = ActivityLForgotPasswordBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            loadFragment();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void loadFragment() {
        try {
            Bundle args = new Bundle();
            args.putString("email", email);
//            args.putString("mobileNo", mobileNo);
//            args.putString("cameFrom", "login");

            Fragment fragment = null;
//
//            if (cameFrom.equals("L_EmailFragment")) {
//                fragment = new L_EmailVerifyFragment(context);
//
//            } else if (cameFrom.equals("L_MobileNoFragment")) {
                fragment = new L_EmailForgotPassword(context);

//            }

            if (fragment != null) {

                // Clears the entire back stack of fragments
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragment.setArguments(args);

                getSupportFragmentManager()
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