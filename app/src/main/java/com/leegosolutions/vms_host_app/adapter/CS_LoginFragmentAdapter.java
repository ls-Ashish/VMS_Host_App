package com.leegosolutions.vms_host_app.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.leegosolutions.vms_host_app.activity.login.fragment.L_EmailFragment;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_MobileNoFragment;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class CS_LoginFragmentAdapter extends FragmentStateAdapter {

    private Context context;
    int totalTabs;

    public CS_LoginFragmentAdapter(Context context, @NonNull FragmentActivity fragmentActivity, int totalTabs) {
        super(fragmentActivity);
        try {
            this.context=context;
            this.totalTabs=totalTabs;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            switch (position) {
                case 0:
                    return new L_EmailFragment(context);
                case 1:
                    return new L_MobileNoFragment(context);
                default:
                    throw new IllegalStateException("Unexpected position " + position);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        throw new IllegalStateException("Unexpected position " + position);
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
