package com.leegosolutions.vms_host_app.activity.home.fragment;

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
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.settings.server.S_ServerDetailsFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsDetails;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.adapter.CS_UpcomingVisitorsAdapter;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentHHomeBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class H_HomeFragment extends Fragment implements CS_UpcomingVisitorsAdapter.OnItemClickListener {

    private Context context;

    private FragmentHHomeBinding viewBinding;
    private BottomNavigationView bottomNavigationView;
    private ArrayList<CS_VisitorsModel> al_Visitors;
    private String sourceId="", appointmentNo="", name="", type="", countryCode="", contactNo="", arrivalDate="", arrivalDateTo="", overnight="", invitationStatus="", lastUpdationDate="";
    private SimpleDateFormat lastUpdationDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss", Locale.getDefault());

    public H_HomeFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public H_HomeFragment(Context context, BottomNavigationView bottomNavigationView) {
        try {
            this.context = context;
            this.bottomNavigationView = bottomNavigationView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_h_home, container, false);
        try {
            viewBinding = FragmentHHomeBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            setGreeting();
            fetchLoginDetails();
            fetchUpcomingVisitors();
            on_Click_Button_MyAccess();
            on_Click_Button_TodaysVisitors();
            on_Click_Button_AllVisitors();
            on_Click_Button_PastVisitors();
            on_Click_FAB_MakeAppointment();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Button_MyAccess() {
        viewBinding.btnMyAccess.setOnClickListener(v -> {
            try {
                // Update button theme
//                setButtonTheme("btnMyAccess");

                // Update bottom navigation selection
//                bottomNavigationView.setSelectedItemId(R.id.nav_access);

                nextFragment("btnMyAccess", 2);

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e);
            }
        });
    }

    private void on_Click_Button_TodaysVisitors() {
        viewBinding.btnTodaysVisitors.setOnClickListener(v -> {
            try {
                // Update button theme
//                setButtonTheme("btnTodaysVisitors");

                // Update bottom navigation selection
//                bottomNavigationView.setSelectedItemId(R.id.nav_visitors);

                nextFragment("btnTodaysVisitors", 1);

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e);
            }
        });
    }

    private void on_Click_Button_AllVisitors() {
        viewBinding.btnAllVisitors.setOnClickListener(v -> {
            try {
                // Update button theme
//                setButtonTheme("btnAllVisitors");

                // Update bottom navigation selection
//                bottomNavigationView.setSelectedItemId(R.id.nav_visitors);

                nextFragment("btnAllVisitors", 1);

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e);
            }
        });
    }

    private void on_Click_Button_PastVisitors() {
        viewBinding.btnPastVisitors.setOnClickListener(v -> {
            try {
                // Update button theme
//                setButtonTheme("btnPastVisitors");

                // Update bottom navigation selection
//                bottomNavigationView.setSelectedItemId(R.id.nav_visitors);

                nextFragment("btnPastVisitors", 1);

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e);
            }
        });
    }

    private void nextFragment(String calledFrom, int iconIndex) {
        try {
            // Update button theme
            setButtonTheme(calledFrom);

            Bundle args = new Bundle();
            args.putString("calledFrom", calledFrom);

            Fragment fragment = null;

            if (calledFrom.equals("btnMyAccess")) {
                fragment = new A_AccessFragment(context);

            } else if (calledFrom.equals("btnTodaysVisitors")) {
                fragment = new V_VisitorsFragment(context);

            } else if (calledFrom.equals("btnAllVisitors")) {
                fragment = new V_VisitorsFragment(context);

            } else if (calledFrom.equals("btnPastVisitors")) {
                fragment = new V_VisitorsFragment(context);

            }

            if (fragment != null) {

                fragment.setArguments(args);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();

                // Update selected icon
                bottomNavigationView.getMenu().getItem(iconIndex).setChecked(true);

            } else {
                new CS_Utility(context).showToast("Fragment is null", 0);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setButtonTheme(String buttonClicked) {
        try {
            if (buttonClicked.equals("btnMyAccess")) {
                buttonSelected(viewBinding.btnMyAccess);
                buttonUnSelected(viewBinding.btnTodaysVisitors);
                buttonUnSelected(viewBinding.btnAllVisitors);
                buttonUnSelected(viewBinding.btnPastVisitors);

            } else if (buttonClicked.equals("btnTodaysVisitors")) {
                buttonUnSelected(viewBinding.btnMyAccess);
                buttonSelected(viewBinding.btnTodaysVisitors);
                buttonUnSelected(viewBinding.btnAllVisitors);
                buttonUnSelected(viewBinding.btnPastVisitors);

            } else if (buttonClicked.equals("btnAllVisitors")) {
                buttonUnSelected(viewBinding.btnMyAccess);
                buttonUnSelected(viewBinding.btnTodaysVisitors);
                buttonSelected(viewBinding.btnAllVisitors);
                buttonUnSelected(viewBinding.btnPastVisitors);

            } else if (buttonClicked.equals("btnPastVisitors")) {
                buttonUnSelected(viewBinding.btnMyAccess);
                buttonUnSelected(viewBinding.btnTodaysVisitors);
                buttonUnSelected(viewBinding.btnAllVisitors);
                buttonSelected(viewBinding.btnPastVisitors);

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

    private void on_Click_FAB_MakeAppointment() {
        viewBinding.fabMakeAppointment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {


                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void fetchUpcomingVisitors() {
        try {
            al_Visitors = new ArrayList<>();
            if (new CS_Connection(context).getStatus()) {
                new FetchVisitors().execute();

            } else {
                CS_VisitorsModel model = new CS_VisitorsModel();
                model.setConnected(false);

                al_Visitors.add(model);

                setUpcomingVisitorAdapter(al_Visitors);

                viewBinding.tabLayout.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setUpcomingVisitorAdapter(ArrayList<CS_VisitorsModel> al_Visitors) {
        try {
            // Adapter
            CS_UpcomingVisitorsAdapter adapter = new CS_UpcomingVisitorsAdapter(context, al_Visitors, H_HomeFragment.this);
            viewBinding.vpUpcomingVisitors.setAdapter(adapter);

            // Using for dot indicator
            TabLayoutMediator tabLayoutMediator =
                    new TabLayoutMediator(viewBinding.tabLayout, viewBinding.vpUpcomingVisitors, true,
                            new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(
                                        @NonNull TabLayout.Tab tab, int position) {
                                }
                            }
                    );
            tabLayoutMediator.attach();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);        }
    }

    @Override
    public void onViewPager2TabItemClick(CS_VisitorsModel model) {
        try {
            goToVisitorsDetailsPage("H_HomeFragment", model.getId());

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);        }
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

    private void fetchLoginDetails() {
        try {
            new FetchLoginDetailsFromSQLite().execute();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public class FetchLoginDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        String userName = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                userName = CS_ED.Decrypt(new CS_Action_LoginDetails(context).getLoginDetails().getLD_UserName());

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                viewBinding.tvUserName.setText(userName);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    private void setGreeting() {
        try {
            String greetings = "";
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            if (hour >= 5 &&hour < 12) {
                greetings = getResources().getString(R.string.home_welcome_text_gm);

            } else if (hour >= 12 && hour < 17) {
                greetings = getResources().getString(R.string.home_welcome_text_ga);

            } else {
                greetings = getResources().getString(R.string.home_welcome_text_ge);
            }

            if (!greetings.isEmpty()) {
                viewBinding.tvGreetingsText.setText(greetings);
            }

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
                    jObject.put("Param_1", "Upcoming");

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
                    viewBinding.tvCaptionUpcomingVisitors.setVisibility(View.VISIBLE);
                    if (al_Visitors.size() > 0) {
                        setUpcomingVisitorAdapter(al_Visitors);

                    } else {

                    }

                } else if (result.equals("2")) {
                    // No update found.
                    viewBinding.tvCaptionUpcomingVisitors.setVisibility(View.VISIBLE);

                } else if (result.equals("0")) {
                    // No visitor data found.
                    viewBinding.tvCaptionUpcomingVisitors.setVisibility(View.GONE);

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