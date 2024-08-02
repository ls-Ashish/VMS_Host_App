package com.leegosolutions.vms_host_app.activity.login;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.adapter.CS_LoginFragmentAdapter;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class Login extends AppCompatActivity {

    private Context context = Login.this;
    private ActivityLoginBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        try {
            viewBinding = ActivityLoginBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            gapBetweenTabItem();
            setViewPagerAdapter();
            onTabSelectedListner();

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

    private void gapBetweenTabItem() {
        try {
            for (int i = 0; i < viewBinding.tabLayout.getTabCount(); i++) {
                View tab = ((ViewGroup) viewBinding.tabLayout.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(10, 0, 10, 0);
                tab.requestLayout();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setViewPagerAdapter() {
        try {
            viewBinding.viewPager.setAdapter(new CS_LoginFragmentAdapter(context, viewBinding.tabLayout, this, viewBinding.tabLayout.getTabCount()));
            // Disable swiping
            viewBinding.viewPager.setUserInputEnabled(false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void onTabSelectedListner() {
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, (tab, position) -> {
            try {
                gapBetweenTabItem();

                if (position == 0) {
                    tab.setText(context.getResources().getString(R.string.login_email));

                } else if (position == 1) {
                    tab.setText(context.getResources().getString(R.string.login_mobile_no));
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }).attach();
    }

}