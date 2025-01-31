package com.leegosolutions.vms_host_app.activity.settings.server;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.Splash;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentNNortificationsBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSServerDetailsBinding;
import com.leegosolutions.vms_host_app.utility.CS_Check_Server_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class S_ServerDetailsFragment extends Fragment implements CS_Check_Server_Connection.ResultListener {

    private Context context;
    private FragmentSServerDetailsBinding viewBinding;
    private String buildingName = "", buildingID = "", unitName = "", unitID = "", buildingCountry = "", buildingAddressLine_1 = "", buildingAddressLine_2 = "";

    public S_ServerDetailsFragment() {
        // Required empty public constructor
    }

    public S_ServerDetailsFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_s_server_details, container, false);
        try {
            viewBinding = FragmentSServerDetailsBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            fetchServerDetails();
            on_Click_Back_Icon();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Back_Icon() {
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

    private void fetchServerDetails() {
        try {
            new FetchServerDetailsFromSQLite().execute();

            if (new CS_Connection(context).getStatus()) {
                new CS_Check_Server_Connection(context, S_ServerDetailsFragment.this).execute();

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchServerDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();
                if (model != null) {
                    buildingName = model.getSD_BuildingName();
                    buildingID = CS_ED.Decrypt(model.getSD_BCode());
                    unitName = model.getSD_TenantName();
                    unitID = CS_ED.Decrypt(model.getSD_TCode());
                    buildingCountry = model.getSD_Country();
                    buildingAddressLine_1 = model.getSD_Address_Line_1();
                    buildingAddressLine_2 = model.getSD_Address_Line_2();

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
                setData();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void setData() {
        try {
            // Property
            viewBinding.tvPropertyName.setText(buildingName);
            viewBinding.tvPropertyID.setText(buildingID);

            // Unit Name
            if (!unitName.isEmpty()) {
                viewBinding.trUnitName.setVisibility(VISIBLE);
                viewBinding.tvUnitName.setText(unitName);

            } else {
                viewBinding.trUnitName.setVisibility(View.GONE);
            }

            // Unit ID
            if (!unitID.isEmpty()) {
                viewBinding.trUnitID.setVisibility(View.VISIBLE);
                viewBinding.tvUnitID.setText(unitID);

            } else {
                viewBinding.trUnitID.setVisibility(View.GONE);
            }

            // Country
            if (!buildingCountry.isEmpty()) {
                viewBinding.trCountry.setVisibility(View.VISIBLE);
                viewBinding.tvCountry.setText(buildingCountry);

            } else {
                viewBinding.trCountry.setVisibility(View.GONE);
            }

            // Address Line_1
            if (!buildingAddressLine_1.isEmpty()) {
                viewBinding.trAddressLine1.setVisibility(View.VISIBLE);
                viewBinding.tvAddressLine1.setText(buildingAddressLine_1);

            } else {
                viewBinding.trAddressLine1.setVisibility(View.GONE);
            }

            // Address Line_2
            if (!buildingAddressLine_2.isEmpty()) {
                viewBinding.trAddressLine2.setVisibility(View.VISIBLE);
                viewBinding.tvAddressLine2.setText(buildingAddressLine_2);

            } else {
                viewBinding.trAddressLine2.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }

        try {
            // Show at last
            viewBinding.llAccountPreference.setVisibility(VISIBLE);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void connectionStatus(String result, String msg, boolean dataInserted) {
        try {
            if (result.equals("1")) {
                new FetchServerDetailsFromSQLite().execute();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}