package com.leegosolutions.vms_host_app.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_EmailFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_MobileNoFragment;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_LoginFragmentAdapter extends FragmentStateAdapter {

    private Context context;
    int totalTabs;
    private TabLayout tabLayout;

    public CS_LoginFragmentAdapter(Context context, TabLayout tabLayout, @NonNull FragmentActivity fragmentActivity, int totalTabs) {
        super(fragmentActivity);
        try {
            this.context = context;
            this.tabLayout = tabLayout;
            this.totalTabs = totalTabs;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            switch (position) {
                case 0:
                    return new L_EmailFragment(context, tabLayout);
                case 1:
                    return new L_MobileNoFragment(context);
                default:
                    throw new IllegalStateException("Unexpected position " + position);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        throw new IllegalStateException("Unexpected position " + position);
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
