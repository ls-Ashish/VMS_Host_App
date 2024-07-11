package com.leegosolutions.vms_host_app.activity.visitors;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentVVisitorsDetailsBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorDetailsModel;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.ArrayList;

public class V_VisitorsDetails extends Fragment {

    private Context context;

    private FragmentVVisitorsDetailsBinding viewBinding;
    private String calledFrom = "", id = "";

    public V_VisitorsDetails() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public V_VisitorsDetails(Context context) {
        this.context = context;
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
//        return inflater.inflate(R.layout.fragment_v_visitors_details, container, false);
        try {
            viewBinding = FragmentVVisitorsDetailsBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getBundle();
            fetchVisitorDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void getBundle() {
        try {
            //Retrieve the value
            if (getArguments() != null) {
                calledFrom = getArguments().getString("calledFrom");
                id = getArguments().getString("id");
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void fetchVisitorDetails() {
        try {
            CS_VisitorDetailsModel model = new CS_Utility(context).getTestingVisitorsDetails();

            if (model != null) {
                setVisitorDetails(model);

            } else {

            }

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void setVisitorDetails(CS_VisitorDetailsModel model) {
        try {
            viewBinding.tvPageCaption.setText(model.getPageCaption());

            viewBinding.tvUnitName.setText(model.getUnitName());
            viewBinding.tvUserName.setText(model.getUserName());

            viewBinding.tvAppointmentNo.setText(model.getAppointmentNo());
            viewBinding.tvInvitationTitle.setText(model.getInvitationTitle());
            viewBinding.tvVisitorName.setText(model.getVisitorName());
            viewBinding.tvCompanyName.setText(model.getCompanyName());
            viewBinding.tvVisitDates.setText(model.getVisitDates());
            viewBinding.tvValidUntil.setText(model.getValidUntil());
            viewBinding.tvOvernight.setText(model.getOvernight());
            viewBinding.tvEntryType.setText(model.getEntryType());
            viewBinding.tvVehicleNo.setText(model.getVehicleNo());
            viewBinding.tvRemarks.setText(model.getRemarks());

            viewBinding.tvCreatedBy.setText(model.getCreatedBy());
            viewBinding.tvCreatedAt.setText(model.getCreatedAt());
            viewBinding.tvLastUpdationBy.setText(model.getLastUpdatedBy());

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}