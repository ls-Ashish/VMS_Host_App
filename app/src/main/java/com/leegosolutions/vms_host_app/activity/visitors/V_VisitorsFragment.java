package com.leegosolutions.vms_host_app.activity.visitors;

import static java.util.Locale.filter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.adapter.CS_VisitorsAdapter;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.ArrayList;

public class V_VisitorsFragment extends Fragment implements CS_VisitorsAdapter.OnItemClickListener {

    private Context context;
    private FragmentHVisitorsBinding viewBinding;
    private ArrayList<CS_VisitorsModel> al_Visitors;
    private CS_VisitorsAdapter adapter;
    private String calledFrom = "";

    public V_VisitorsFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
        try {

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    public V_VisitorsFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_h_visitors, container, false);
        try {
            viewBinding = FragmentHVisitorsBinding.inflate(inflater, container, false);

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
            getBundle();
            getVisitors();
            on_Click_Button_Todays();
            on_Click_Button_AllVisitors();
            on_Click_Button_History();
            search();

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

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Button_Todays() {
        viewBinding.btnTodays.setOnClickListener(v -> {
            try {
                getTodayVisitors();

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void on_Click_Button_AllVisitors() {
        viewBinding.btnAllVisitors.setOnClickListener(v -> {
            try {
                getAllVisitors();

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void on_Click_Button_History() {
        viewBinding.btnHistory.setOnClickListener(v -> {
            try {
                getVisitorsHistory();

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void getTodayVisitors() {
        try {
            setButtonTheme("btnTodays");
            upcomingVisitors();

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void getAllVisitors() {
        try {
            setButtonTheme("btnAllVisitors");

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void getVisitorsHistory() {
        try {
            setButtonTheme("btnHistory");

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
                    filter(newText);

                } catch (Exception e) {
                    new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                    }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                }
                return false;
            }
        });
    }

    private void upcomingVisitors() {
        try {
            if (new CS_Connection(context).getStatus()) {
                fetchUpcomingVisitors();

            } else {
//                al_Visitors = new ArrayList<>();
//                CS_VisitorsModel model = new CS_VisitorsModel();
//                model.setConnected(false);
//
//                al_Visitors.add(model);
//
//                setUpcomingVisitorAdapter(al_Visitors);
//
//                viewBinding.tabLayout.setVisibility(View.INVISIBLE);

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
            adapter = new CS_VisitorsAdapter(context, al_Visitors, V_VisitorsFragment.this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            viewBinding.recyclerView.setLayoutManager(mLayoutManager);
            viewBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());

            viewBinding.recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onItemClick(CS_VisitorsModel model) {
        try {
            goToVisitorsDetailsPage("V_VisitorsFragment", model.getId());

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

}