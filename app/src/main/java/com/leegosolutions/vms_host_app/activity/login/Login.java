package com.leegosolutions.vms_host_app.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void setViewPagerAdapter() {
        try {
            viewBinding.viewPager.setAdapter(new CS_LoginFragmentAdapter(context, this, viewBinding.tabLayout.getTabCount()));
            // Disable swiping
            viewBinding.viewPager.setUserInputEnabled(false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }).attach();
    }

}