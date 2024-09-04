package com.leegosolutions.vms_host_app.activity.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.activity.notifications.N_NotificationsFragment;
import com.leegosolutions.vms_host_app.activity.settings.S_SettingsFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.databinding.ActivityHomeBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class Home extends AppCompatActivity {

    private Context context = Home.this;
    private ActivityHomeBinding viewBinding;
    private boolean bnn_Menu_Home = false, bnn_Menu_Visitors = false, bnn_Menu_Access = false, bnn_Menu_Nortifications = false, bnn_Menu_Settings = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        try {
            viewBinding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            setBottomNavigationView();
            bottomNavigationViewOnNavigationItemSelectedListener();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
//                super.onBackPressed();
                //additional code
                showAppExitDialog();
            } else {
                super.onBackPressed();
//                getSupportFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void setBottomNavigationView() {
        try {
            Menu menu = viewBinding.bottomNavigationView.getMenu();

            MenuItem menu_Home = menu.findItem(R.id.nav_home);
            MenuItem menu_Visitors = menu.findItem(R.id.nav_visitors);
            MenuItem menu_Access = menu.findItem(R.id.nav_access);
            MenuItem menu_Notifications = menu.findItem(R.id.nav_notifications);
            MenuItem menu_Settings = menu.findItem(R.id.nav_settings);

            // Make dynamic as per requirement
            bnn_Menu_Home = false;
            bnn_Menu_Visitors = false;
            bnn_Menu_Access = true;
            bnn_Menu_Nortifications = false;
            bnn_Menu_Settings = true;

            menu_Home.setVisible(bnn_Menu_Home);
            menu_Visitors.setVisible(bnn_Menu_Visitors);
            menu_Access.setVisible(bnn_Menu_Access);
            menu_Notifications.setVisible(bnn_Menu_Nortifications);
            menu_Settings.setVisible(bnn_Menu_Settings);

            // Default
            if (bnn_Menu_Home) {
                viewBinding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
                nextFragment("Home");

            } else if (bnn_Menu_Access) {
                viewBinding.bottomNavigationView.setSelectedItemId(R.id.nav_access);
                nextFragment("Access");
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void bottomNavigationViewOnNavigationItemSelectedListener() {
        viewBinding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    int currentItemId = viewBinding.bottomNavigationView.getSelectedItemId();
                    int id = item.getItemId();

                    if (id != currentItemId) {

                        if (id == R.id.nav_home) {
                            nextFragment("Home");
                            return true;

                        } else if (id == R.id.nav_visitors) {
                            nextFragment("Visitors");
                            return true;

                        } else if (id == R.id.nav_access) {
                            nextFragment("Access");
                            return true;

                        } else if (id == R.id.nav_notifications) {
                            nextFragment("Notifications");
                            return true;

                        } else if (id == R.id.nav_settings) {
                            nextFragment("Settings");
                            return true;

                        } else {
                            return false;
                        }
                    } else {
                        // Ignore the click as it's on the current item
                        return false;
                    }
                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }

                return false;
            }
        });
    }

//    private void fragmentTransaction(Fragment fragment) {
//        try {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frameLayout, fragment)
////                    .addToBackStack(null)
//                    .commit();
//
//        } catch (Exception e) {
//            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
//            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
//        }
//    }

    private void nextFragment(String calledFrom) {
        try {
            Bundle args = new Bundle();
            args.putString("calledFrom", calledFrom);

            Fragment fragment = null;

            if (calledFrom.equals("Home")) {
                fragment = new H_HomeFragment(context, viewBinding.bottomNavigationView);

            } else if (calledFrom.equals("Visitors")) {
                fragment = new V_VisitorsFragment(context);

            } else if (calledFrom.equals("Access")) {
                fragment = new A_AccessFragment(context);

            } else if (calledFrom.equals("Notifications")) {
                fragment = new N_NotificationsFragment(context);

            } else if (calledFrom.equals("Settings")) {
                fragment = new S_SettingsFragment(context, viewBinding.bottomNavigationView);

            }

            if (fragment != null) {

                // Clears the entire back stack of fragments
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                fragment.setArguments(args);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment)
                        .commit();
            } else {
                new CS_Utility(context).showToast("Fragment is null", 0);

            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    public void showAppExitDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.app_exit_caption));
            builder.setIcon(R.drawable.ic_exit);
            builder.setMessage(getResources().getString(R.string.app_exit_message));
            builder.setCancelable(false);
            builder.setPositiveButton("Yes".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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