package com.leegosolutions.vms_host_app.activity.visitors;

import static java.util.Locale.filter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.adapter.CS_VisitorsAdapter;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class V_VisitorsFragment extends Fragment implements CS_VisitorsAdapter.OnItemClickListener {

    private Context context;
    private FragmentHVisitorsBinding viewBinding;
    private ArrayList<CS_VisitorsModel> al_Visitors;
    private CS_VisitorsAdapter adapter;
    private String calledFrom = "";
    private String sourceId="", appointmentNo="", name="", type="", countryCode="", contactNo="", arrivalDate="", arrivalDateTo="", overnight="", invitationStatus="", lastUpdationDate="";
    private SimpleDateFormat lastUpdationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.getDefault());

    public V_VisitorsFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
        try {

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public V_VisitorsFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_h_visitors, container, false);
        try {
            viewBinding = FragmentHVisitorsBinding.inflate(inflater, container, false);

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
            getVisitors();
            on_Click_Button_Todays();
            on_Click_Button_AllVisitors();
            on_Click_Button_History();
            search();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getBundle() {
        try {
            //Retrieve the value
            if (getArguments() != null) {
                calledFrom = getArguments().getString("calledFrom");

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getVisitors() {
        try {
            if (calledFrom.equals("btnTodaysVisitors") || calledFrom.equals("Visitors")) {
                getTodayVisitors();

            } else if (calledFrom.equals("btnAllVisitors")) {
                getAllVisitors();

            } else if (calledFrom.equals("btnPastVisitors")) {
                getVisitorsHistory();

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Button_Todays() {
        viewBinding.btnTodays.setOnClickListener(v -> {
            try {
                getTodayVisitors();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void on_Click_Button_AllVisitors() {
        viewBinding.btnAllVisitors.setOnClickListener(v -> {
            try {
                getAllVisitors();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void on_Click_Button_History() {
        viewBinding.btnHistory.setOnClickListener(v -> {
            try {
                getVisitorsHistory();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void getTodayVisitors() {
        try {
            calledFrom = "btnTodaysVisitors";
            setButtonTheme("btnTodays");
            al_Visitors = new ArrayList<>();
            if (new CS_Connection(context).getStatus()) {
                new FetchVisitors().execute();

            } else {
                // No internet connection
                CS_VisitorsModel model = new CS_VisitorsModel();
                model.setConnected(false);
                al_Visitors.add(model);
                setUpcomingVisitorAdapter(al_Visitors);
//                viewBinding.tabLayout.setVisibility(View.INVISIBLE);
//                showSnackbar();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getAllVisitors() {
        try {
            calledFrom = "btnAllVisitors";
            setButtonTheme("btnAllVisitors");

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void getVisitorsHistory() {
        try {
            calledFrom = "btnPastVisitors";
            setButtonTheme("btnHistory");

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setButtonTheme(String buttonClicked) {
        try {
            if (buttonClicked.equals("btnTodays")) {
                buttonSelected(viewBinding.btnTodays);
                buttonUnSelected(viewBinding.btnAllVisitors);
                buttonUnSelected(viewBinding.btnHistory);

            } else if (buttonClicked.equals("btnAllVisitors")) {
                buttonUnSelected(viewBinding.btnTodays);
                buttonSelected(viewBinding.btnAllVisitors);
                buttonUnSelected(viewBinding.btnHistory);

            } else if (buttonClicked.equals("btnHistory")) {
                buttonUnSelected(viewBinding.btnTodays);
                buttonUnSelected(viewBinding.btnAllVisitors);
                buttonSelected(viewBinding.btnHistory);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void buttonSelected(Button button) {
        try {
            button.setBackground(getResources().getDrawable(R.drawable.round_shape));
            button.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
            button.setTextColor(button.getContext().getResources().getColor(R.color.btn_background_text_color));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void buttonUnSelected(Button button) {
        try {
            button.setBackground(getResources().getDrawable(R.drawable.round_shape));
            button.setBackgroundTintList(context.getResources().getColorStateList(R.color.btn_background_white));
            button.setTextColor(button.getContext().getResources().getColor(R.color.btn_background_white_text_color));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void search() {
        viewBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    // inside on query text change method we are
                    // calling a method to filter our recycler view.
                    if (!newText.isEmpty()) {
                        filter(newText);
                    }

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
                return false;
            }
        });
    }

    private void setUpcomingVisitorAdapter(ArrayList<CS_VisitorsModel> al_Visitors) {
        try {
            // Adapter
            adapter = new CS_VisitorsAdapter(context, al_Visitors, V_VisitorsFragment.this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            viewBinding.recyclerView.setLayoutManager(mLayoutManager);
            viewBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

            viewBinding.recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void filter(String text) {
        try {
            // creating a new array list to filter our data.
            ArrayList<CS_VisitorsModel> filteredlist = new ArrayList<CS_VisitorsModel>();

            // running a for loop to compare elements.
            for (CS_VisitorsModel item : al_Visitors) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    // if the item is matched we are
                    // adding it to our filtered list.
                    filteredlist.add(item);
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                new CS_Utility(context).showToast("No Data Found..", 0);
            } else {
                // at last we are passing that filtered
                // list to our adapter class.
                adapter.filterList(filteredlist);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onItemClick(CS_VisitorsModel model) {
        try {
            goToVisitorsDetailsPage("V_VisitorsFragment", model.getId());

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void goToVisitorsDetailsPage(String calledFrom, String id) {
        try {
            Fragment fragment = new V_VisitorsDetails(context);

            Bundle args = new Bundle();
            args.putString("calledFrom", calledFrom);
            args.putString("id", id);

            fragment.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.rlMain, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                if (calledFrom.equals("btnTodaysVisitors") || calledFrom.equals("Visitors")) {
                                    getTodayVisitors();

                                } else if (calledFrom.equals("btnAllVisitors")) {
                                    getAllVisitors();

                                } else if (calledFrom.equals("btnPastVisitors")) {
                                    getVisitorsHistory();

                                }

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

    class FetchVisitors extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String appToken = "", baseURL = "", buildingId = "", tenantId = "", hostId = "";

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

                    // Fetch login id
                    hostId = new CS_Action_LoginDetails(context).getLoginDetails().getLD_SourceId();

                    JSONObject jObject = new JSONObject();
                    jObject.put("HostId", hostId);
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Fetch_Appointment");
//                    jObject.put("LastUpdationDate", lastUpdationDate.replace(" ", "T"));
                    jObject.put("LastUpdationDate", "");
                    jObject.put("Param_1", "Todays");

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

                                        sourceId = jsonObject.getString("Source_Id");
                                        appointmentNo = jsonObject.getString("AppointmentNo");
                                        name = jsonObject.getString("Name");
                                        type = jsonObject.getString("Type");
                                        countryCode = jsonObject.getString("CountryCode");
                                        contactNo = jsonObject.getString("ContactNo");
                                        arrivalDate = jsonObject.getString("ArrivalDate");
                                        arrivalDateTo = jsonObject.getString("ArrivalDateTo");
                                        overnight = jsonObject.getString("Overnight");
                                        invitationStatus = jsonObject.getString("InvitationStatus");
                                        String fetchedLastUpdationDate = jsonObject.getString("LastUpdationDate");

                                        if (!fetchedLastUpdationDate.isEmpty()) {
                                            if (fetchedLastUpdationDate.contains("T")) {
                                                fetchedLastUpdationDate = fetchedLastUpdationDate.replace("T", " ");
                                            }
                                        }

                                        if (lastUpdationDate.equals("")) {
                                            lastUpdationDate = fetchedLastUpdationDate;

                                        } else {
                                            Date fetchedDate = lastUpdationDateFormat.parse(fetchedLastUpdationDate);
                                            Date existinLastUpdationDate= lastUpdationDateFormat.parse(lastUpdationDate);

                                            // Compare dates
                                            int comparisonResult = fetchedDate.compareTo(existinLastUpdationDate);
                                            if (comparisonResult > 0) {
                                                lastUpdationDate = fetchedLastUpdationDate;

                                            }
                                        }

                                        CS_VisitorsModel visitorsModel = new CS_VisitorsModel();

                                        visitorsModel.setId(sourceId);
                                        visitorsModel.setAppointmentNo(appointmentNo);
                                        visitorsModel.setName(name);
                                        visitorsModel.setType(type);
                                        visitorsModel.setMobileNo("+" + countryCode + " " + contactNo);
                                        visitorsModel.setStartDate(arrivalDate);
                                        visitorsModel.setEndDate(arrivalDateTo);
                                        visitorsModel.setOvernights(overnight);
                                        visitorsModel.setStatus(invitationStatus);

                                        al_Visitors.add(visitorsModel);
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
                    // 'Found visitor data.
                    if (al_Visitors.size() > 0) {
                        setUpcomingVisitorAdapter(al_Visitors);

                    } else {
                        showAlertDialog(context.getResources().getString(R.string.visitor_success_no_visitor_data));
                    }

                } else if (result.equals("2")) {
                    // No update found.

                } else if (result.equals("0")) {
                    // No visitor data found.

                } else if (hostId.equals("")) {
                    showAlertDialog(context.getResources().getString(R.string.home_visitor_login_id_blank));

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

}