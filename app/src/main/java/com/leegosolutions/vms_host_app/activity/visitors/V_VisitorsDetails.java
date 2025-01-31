package com.leegosolutions.vms_host_app.activity.visitors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentVVisitorsDetailsBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorDetailsModel;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class V_VisitorsDetails extends Fragment {

    private Context context;

    private FragmentVVisitorsDetailsBinding viewBinding;
    private String calledFrom="", apdID="", hostName="", tenantName="", appointmentNo="", purpose="", visitorName="", arrivalDate="", arrivalDateTo="", overnight="", entryType="", vehicleNo="", remarks="", createdBy="", creationDate="", invitationStatus="", lastUpdationDate="", floorUnit="";

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
//        return inflater.inflate(R.layout.fragment_v_visitors_details, container, false);
        try {
            viewBinding = FragmentVVisitorsDetailsBinding.inflate(inflater, container, false);

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
            fetchVisitorDetails();
            on_Click_Back_Arrow();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getBundle() {
        try {
            //Retrieve the value
            if (getArguments() != null) {
                calledFrom = getArguments().getString("calledFrom");
                apdID = getArguments().getString("id");
            }

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

    private void fetchVisitorDetails() {
        try {
            if (new CS_Connection(context).getStatus()) {
            new FetchVisitorsDetails().execute();

        } else {
            showSnackbar();
        }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.rlMain, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                fetchVisitorDetails();

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e);
                            }
                        }
                    });
            snackbar.show();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    class FetchVisitorsDetails extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String appToken = "", baseURL = "", buildingId = "", tenantId = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressdialog = new ProgressDialog(context);
                progressdialog.setCancelable(false);
                progressdialog.setMessage("Please wait...");
                progressdialog.show();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();
                if (model != null) {
                    baseURL = CS_ED.Decrypt(model.getSD_BaseURL());
                    appToken = model.getSD_AppToken();
                    buildingId = model.getSD_BU_ID();
                    tenantId = model.getSD_TE_ID();
                }

                if (!baseURL.equals("")) {

                    JSONObject jObject = new JSONObject();
                    jObject.put("HostId", "0");
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Fetch_Appointment_Details");
                    jObject.put("LastUpdationDate", "");
                    jObject.put("Param_1", "");
                    jObject.put("Param_2", apdID);

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id", "71")
                            .addFormDataPart("Json_Data", String.valueOf(jObject))
                            .addFormDataPart("App_Token", appToken)
                            .build();

                    Request request = new Request.Builder()
                            .url(baseURL + CS_API_URL.Visitors)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer " + appToken)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response != null) {
                            String responseBody = response.body().string();
                            if (!responseBody.equals("")) {

                                String jsonData = responseBody;
                                JSONArray jsonArray = new JSONArray(jsonData);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    result = jsonObject.getString("Result");
                                    msg = jsonObject.getString("Msg");

                                    if (result.equals("1")) {

                                        hostName = jsonObject.getString("HostName");
                                        tenantName = jsonObject.getString("TenantName");
                                        appointmentNo = jsonObject.getString("AppointmentNo");
                                        purpose = jsonObject.getString("Purpose");
                                        visitorName = jsonObject.getString("VisitorName");
                                        arrivalDate = jsonObject.getString("ArrivalDate");
                                        arrivalDateTo = jsonObject.getString("ArrivalDateTo");
                                        overnight = jsonObject.getString("Overnight");
                                        entryType = jsonObject.getString("MultipleEntry");
                                        vehicleNo = jsonObject.getString("VehicleNo");
                                        remarks = jsonObject.getString("Remarks");
                                        createdBy = jsonObject.getString("CreatedBy");
                                        creationDate = jsonObject.getString("CreationDate");
                                        invitationStatus = jsonObject.getString("V_InvitationStatus");
                                        lastUpdationDate = jsonObject.getString("LastUpdationDate");
                                        floorUnit = jsonObject.getString("FloorUnit");
                                    }
                                }
                            }
                        }
                    }
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
                progressdialog.dismiss();
                if (result.equals("1")) {
                    setData();

                } else if (baseURL.equals("")) {
                    showAlertDialog(CS_Constant.invalidBaseURL);

                } else {
                    showAlertDialog(!msg.equals("") ? msg : CS_Constant.serverConnectionErrorMessage);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public void showAlertDialog(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setIcon(R.drawable.ic_error);
            builder.setMessage(message);
            builder.setCancelable(false);
            builder.setPositiveButton("OK".toUpperCase(), new DialogInterface.OnClickListener() {
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

    private void setData() {
        try {
            // Page Caption
            if (!floorUnit.isEmpty()) {
                viewBinding.tvPageCaption.setText(floorUnit);

            } else {
                viewBinding.tvPageCaption.setText("");
            }

            // Unit Name
            if (!tenantName.isEmpty()) {
                viewBinding.tvUnitName.setText(tenantName);

            } else {
                viewBinding.trUnitName.setVisibility(View.GONE);
            }

            // User Name
            if (!hostName.isEmpty()) {
                viewBinding.tvUserName.setText(hostName);

            } else {
                viewBinding.trUserName.setVisibility(View.GONE);
            }

            // Appointment No.
            if (!appointmentNo.isEmpty()) {
                viewBinding.tvAppointmentNo.setText(appointmentNo);

            } else {
//                viewBinding.zzzz.setVisibility(View.GONE);
            }

            // Visitor Name
//            if (!xxxx.isEmpty()) {
//                viewBinding.zzzz.setText(xxxx);
//
//            } else {
//                viewBinding.zzzz.setVisibility(View.GONE);
//            }
//
//            //
//            if (!xxxx.isEmpty()) {
//                viewBinding.zzzz.setText(xxxx);
//
//            } else {
//                viewBinding.zzzz.setVisibility(View.GONE);
//            }
//            Later - complete





//            viewBinding..setText();
            viewBinding.tvInvitationTitle.setText(purpose);
            viewBinding.tvVisitorName.setText(visitorName);
            viewBinding.tvCompanyName.setText(tenantName);
            viewBinding.tvVisitDates.setText(arrivalDate);
            viewBinding.tvValidUntil.setText(arrivalDateTo);
            viewBinding.tvOvernight.setText(overnight);
            viewBinding.tvEntryType.setText(entryType);
            viewBinding.tvVehicleNo.setText(vehicleNo);
            viewBinding.tvRemarks.setText(remarks);

            viewBinding.tvCreatedBy.setText(hostName);
            viewBinding.tvCreatedAt.setText(creationDate);
            viewBinding.tvLastUpdationBy.setText(lastUpdationDate);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}