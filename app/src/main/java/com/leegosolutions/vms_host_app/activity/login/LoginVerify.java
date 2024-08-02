package com.leegosolutions.vms_host_app.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_EmailVerifyFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_MobileNoFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_MobileNoVerifyFragment;
import com.leegosolutions.vms_host_app.activity.notifications.N_NotificationsFragment;
import com.leegosolutions.vms_host_app.activity.settings.S_SettingsFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginBinding;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginVerifyBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class LoginVerify extends AppCompatActivity {

    private Context context = LoginVerify.this;
    private ActivityLoginVerifyBinding viewBinding;
    private String cameFrom = "", email = "", mobileNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verify);
        try {
            viewBinding = ActivityLoginVerifyBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            getIntentData();
            loadFragment();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
//                super.onBackPressed();
                //additional code
                showAppExitDialog();
            } else {
                super.onBackPressed();
//                getSupportFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void showAppExitDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.app_exit_caption));
            builder.setIcon(R.drawable.ic_exit);
            builder.setMessage(getResources().getString(R.string.app_exit_message));
            builder.setCancelable(false);
            builder.setPositiveButton("Yes".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getIntentData() {
        try {
            Intent intent = getIntent();
            if (getIntent().getExtras() != null) {
                if (intent.getStringExtra("cameFrom") != null) {
                    cameFrom = intent.getStringExtra("cameFrom");
                }
                if (intent.getStringExtra("email") != null) {
                    email = intent.getStringExtra("email");
                }
                if (intent.getStringExtra("mobileNo") != null) {
                    mobileNo = intent.getStringExtra("mobileNo");
                }
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void loadFragment() {
        try {
            Bundle args = new Bundle();
            args.putString("email", email);
            args.putString("mobileNo", mobileNo);

            Fragment fragment = null;

            if (cameFrom.equals("L_EmailFragment")) {
                fragment = new L_EmailVerifyFragment(context);

            } else if (cameFrom.equals("L_MobileNoFragment")) {
                fragment = new L_MobileNoVerifyFragment(context);

            }

            if (fragment != null) {

                // Clears the entire back stack of fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

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