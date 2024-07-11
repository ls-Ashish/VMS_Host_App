package com.leegosolutions.vms_host_app.activity.home.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsDetails;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.adapter.CS_UpcomingVisitorsAdapter;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentHHomeBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.ArrayList;

public class H_HomeFragment extends Fragment implements CS_UpcomingVisitorsAdapter.OnItemClickListener {

    private Context context;

    private FragmentHHomeBinding viewBinding;
    private BottomNavigationView bottomNavigationView;
    ArrayList<CS_VisitorsModel> al_Visitors;

    public H_HomeFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public H_HomeFragment(Context context, BottomNavigationView bottomNavigationView) {
        try {
            this.context = context;
            this.bottomNavigationView = bottomNavigationView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        view = inflater.inflate(R.layout.fragment_h_home, container, false);
        try {
            viewBinding = FragmentHHomeBinding.inflate(inflater, container, false);

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
            on_Click_Button_MyAccess();
            on_Click_Button_TodaysVisitors();
            on_Click_Button_AllVisitors();
            on_Click_Button_PastVisitors();
            on_Click_FAB_MakeAppointment();
            upcomingVisitors();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void buttonSelected(Button button) {
        try {
            button.setBackground(getResources().getDrawable(R.drawable.round_shape));
            button.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorPrimary));
            button.setTextColor(button.getContext().getResources().getColor(R.color.btn_background_text_color));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void buttonUnSelected(Button button) {
        try {
            button.setBackground(getResources().getDrawable(R.drawable.round_shape));
            button.setBackgroundTintList(context.getResources().getColorStateList(R.color.btn_background_white));
            button.setTextColor(button.getContext().getResources().getColor(R.color.btn_background_white_text_color));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_FAB_MakeAppointment() {
        viewBinding.fabMakeAppointment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

//                    CS_ServerDetails model = new CS_ServerDetails("1", "", "", "", "", "", "", "", "", "", "", "", "");
//                    new InsertServerDetails().execute(model);
                    new FetchServerDetails().execute();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                    }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                }
            }
        });
    }

    class InsertServerDetails extends AsyncTask<CS_Entity_ServerDetails, Void, Void> {

        @Override
        protected Void doInBackground(CS_Entity_ServerDetails... csServerDetails) {
            new CS_Action_ServerDetails(context).insertServerDetails(csServerDetails[0]);
//            if (al_ServerDetails.size() > 0) {
//
//            }
            return null;
        }
    }

    class FetchServerDetails extends AsyncTask<CS_Entity_ServerDetails, Void, Void> {

        @Override
        protected Void doInBackground(CS_Entity_ServerDetails... csServerDetails) {
//            CS_ServerDetails al_ServerDetails = new CS_Action(context).getServerDetails();
//            if (al_ServerDetails.size() > 0) {
//
//            }
            return null;
        }
    }

    private void upcomingVisitors() {
        try {
            if (new CS_Connection(context).getStatus()) {
                fetchUpcomingVisitors();

            } else {
                al_Visitors = new ArrayList<>();
                CS_VisitorsModel model = new CS_VisitorsModel();
                model.setConnected(false);

                al_Visitors.add(model);

                setUpcomingVisitorAdapter(al_Visitors);

                viewBinding.tabLayout.setVisibility(View.INVISIBLE);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void fetchUpcomingVisitors() {
        try {
            al_Visitors = new ArrayList<>();

            // get testing data
            al_Visitors = new CS_Utility(context).getTestingVisitorsData();

            if (al_Visitors.size() > 0) {
                setUpcomingVisitorAdapter(al_Visitors);

            } else {

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onItemClick(CS_VisitorsModel model) {
        try {
            goToVisitorsDetailsPage("H_HomeFragment", model.getId());

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

//    private int SD_Auto_Id;
//
//    private String SD_BaseURL;
//    private String SD_BU_ID;
//    private String SD_TE_ID;
//    private String SD_BCode;
//    private String SD_TCode;
//    private String SD_ClientSecret;
//    private String SD_BuildingName;
//    private String SD_TenantName;
//    private String SD_AppToken;
//    private String SD_ErrorPostingURL;
//    private String SD_CreationDate;
//    private String SD_UpdationDate;
//    private String SD_Status;

}