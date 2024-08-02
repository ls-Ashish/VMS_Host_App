package com.leegosolutions.vms_host_app.activity.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.access.A_AccessFragment;
import com.leegosolutions.vms_host_app.activity.home.fragment.H_HomeFragment;
import com.leegosolutions.vms_host_app.activity.login.Login;
import com.leegosolutions.vms_host_app.activity.notifications.N_NotificationsFragment;
import com.leegosolutions.vms_host_app.activity.settings.change_password.S_ChangePassword;
import com.leegosolutions.vms_host_app.activity.settings.server.S_ServerDetailsFragment;
import com.leegosolutions.vms_host_app.activity.settings.update.S_UpdateMobileNoFragment;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsDetails;
import com.leegosolutions.vms_host_app.activity.visitors.V_VisitorsFragment;
import com.leegosolutions.vms_host_app.database.action.CS_Action;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentAAccessBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentSSettingsBinding;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;
import com.leegosolutions.vms_host_app.utility.CS_VersionDetails;
import com.leegosolutions.vms_host_app.utility.email.CS_SendEmail;
import com.leegosolutions.vms_host_app.utility.sms.CS_SendSMS;

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
//        return inflater.inflate(R.layout.fragment_s_settings, container, false);
        try {
            viewBinding = FragmentSSettingsBinding.inflate(inflater, container, false);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            showAppVersion();
            onClick_Button_LogOut();
            on_Click_AccountPreference();
            on_Click_Security();
            on_Click_ServerDetails();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void showAppVersion() {
        try {
            String version = new CS_Utility(context).appVersion();
            viewBinding.tvAppVersion.setText(context.getResources().getText(R.string.settings_app_version) + " " + version);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void onClick_Button_LogOut() {
        viewBinding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showLogoutConfirmation();

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void showLogoutConfirmation() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Alert");
            builder.setIcon(R.drawable.ic_logout);
            builder.setMessage("Are you sure want to logout ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes".toUpperCase(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    new LogOut().execute();
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

    public class LogOut extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                // Clean
                new CS_Action_LoginDetails(context).deleteLoginDetails();
                new CS_Action_AccessDetails(context).deleteAccessDetails();

                result = true;

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            try {
                if (result) {
                    new CS_Utility(context).showToast(getResources().getString(R.string.settings_logout_success), 1);
                    goToLoginPage();

                } else {
                    showAlertDialog(context.getResources().getString(R.string.settings_logout_error));
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        }
    }

    public void goToLoginPage() {
        try {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            Intent intent = new Intent(requireActivity(), Login.class);
            startActivity(intent);
            requireActivity().finish();
            requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
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

    public void on_Click_AccountPreference() {
        viewBinding.llAccountPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_UpdateMobileNoFragment(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void on_Click_Security() {
        viewBinding.llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_ChangePassword(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    public void on_Click_ServerDetails() {
        viewBinding.llServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goToNextFragment(new S_ServerDetailsFragment(context));

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e);
                }
            }
        });
    }

    private void goToNextFragment(Fragment fragment) {
        try {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack(null)
                    .commit();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}