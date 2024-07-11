package com.leegosolutions.vms_host_app.activity.settings;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.database.action.CS_Action;
import com.leegosolutions.vms_host_app.databinding.FragmentAAccessBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSSettingsBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.CS_VersionDetails;

public class S_SettingsFragment extends Fragment {

    private Context context;
    private FragmentSSettingsBinding viewBinding;

    public S_SettingsFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public S_SettingsFragment(Context context) {
        try {
            this.context = context;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            this.context = context; // required - when orientation changed, need to re initialize context, as it becomes null

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onDetach() {
        try {
            super.onDetach();
            context = null;

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onDestroyView() {
        try {
            super.onDestroyView();
            viewBinding = null; // Clear binding to avoid memory leaks

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            viewBinding = FragmentSSettingsBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            showAppVersion();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void showAppVersion() {
        try {
            String version = new CS_Utility(context).appVersion();
            viewBinding.tvAppVersion.setText(version);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}