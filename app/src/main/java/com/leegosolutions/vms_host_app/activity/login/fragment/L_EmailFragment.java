package com.leegosolutions.vms_host_app.activity.login.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentLEmailBinding;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class L_EmailFragment extends Fragment {

    private Context context;

    private FragmentLEmailBinding viewBinding;
    private String email="", password="";

    public L_EmailFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public L_EmailFragment(Context context) {
        try {
            this.context = context;

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
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
//        return inflater.inflate(R.layout.fragment_l_email, container, false);
        try {
            viewBinding = FragmentLEmailBinding.inflate(inflater,container,false);

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
            on_Click_Button_Next();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Button_Next() {
        viewBinding.btnNext.setOnClickListener(v -> {
            try {
                if (validate()) {
                    login();
                }

            } catch (Exception e) {
                new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private boolean validate() {
        boolean result = false;
        try {
            email = viewBinding.etEmail.getText().toString().trim();
            password = viewBinding.etPassword.getText().toString().trim();

            if (email.equals("")) {
                new CS_Utility(context).showToast( getResources().getString(R.string.login_email_empty_email), 0);

            } else if (password.equals("")) {
                new CS_Utility(context).showToast( getResources().getString(R.string.login_email_empty_password), 0);

            } else {
                result = true;
            }

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    private void login() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new Login().execute();

            } else {
                showSnackbar();
            }

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    class Login extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String bCode="", tCode="", clientSecret="", baseURL="", userType="", userName="", buildingId="", tenantId="", sourceId="";
        private byte[] userPhoto=null;
        private boolean dataInserted = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressdialog = new ProgressDialog(context);
                progressdialog.setCancelable(false);
                progressdialog.setMessage("Please wait...");
                progressdialog.show();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }

        @Override
        protected Void doInBackground(Void ... voids) {
            try {
                OkHttpClient client = new CS_Utility(context).getOkHttpClient();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();

                if (model != null) {
                    bCode = model.getSD_BCode();
                    tCode = model.getSD_TCode();
                    clientSecret = model.getSD_ClientSecret();
                    baseURL = model.getSD_BaseURL();
                    buildingId = model.getSD_BU_ID();
                    tenantId = model.getSD_TE_ID();

                }
                if (!bCode.equals("") && !tCode.equals("") && !clientSecret.equals("") && !baseURL.equals("")) {

                    JSONObject jObject = new JSONObject();
                    jObject.put("Id", "0");
                    jObject.put("UserName", email);
                    jObject.put("Password", password);
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Validate_User");

                    RequestBody body = RequestBody.create(mediaType, String.valueOf(jObject));

                    Request request = new Request.Builder()
                            .url(CS_ED.Decrypt(baseURL) + CS_API_URL.validateBuilding)
                            .method("POST", body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Authorization", "Bearer " + "")
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        if (response != null) {
                            String responseBody = response.body().string();

                            // fixme - till api is not ready
                            responseBody = "[{\"Result\":1,\"Msg\":\"Login Successfully\",\"Source_Id\":63,\"UserType\":\"Host\",\"UserName\":\"Unir A host\",\"ProfilePhoto\":\"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\"}]";

                            if (!responseBody.equals("")) {

                                String jsonData = responseBody;
                                JSONArray jsonArray = new JSONArray(jsonData);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                result = jsonObject.getString("Result");
                                msg = jsonObject.getString("Msg");

                                if (result.equals("1")) {
                                    sourceId = jsonObject.getString("Source_Id");
                                    userType = jsonObject.getString("UserType");
                                    userName = jsonObject.getString("UserName");

                                    String base64_Image = jsonObject.getString("ProfilePhoto");
                                    if (!base64_Image.equals("null") && !base64_Image.equals("")) {
                                        userPhoto = Base64.decode(base64_Image, Base64.NO_WRAP);
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            try {
                if (result.equals("1")) {
                    // Save login details

                    // Clean
                    new CS_Action_LoginDetails(context).deleteLoginDetails();

                    // Model
                    CS_Entity_LoginDetails model = new CS_Entity_LoginDetails(sourceId, CS_ED.Encrypt(email), CS_ED.Encrypt(password), CS_ED.Encrypt(userType), CS_ED.Encrypt(userName), userPhoto, 1, new CS_Utility(context).getDateTime(), "");

                    // Insert
                    dataInserted = new CS_Action_LoginDetails(context).insertLoginDetails(model);

                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                progressdialog.dismiss();

                if (result.equals("1")) {

                    if (dataInserted) {
                        nextPage(Home.class);
                        new CS_Utility(context).showToast(getResources().getString(R.string.login_success), 1);

                    } else {
                        showAlertDialog(getResources().getString(R.string.login_data_save_error));
                    }

                } else if (bCode.equals("") || tCode.equals("") || clientSecret.equals("") || baseURL.equals("")) {
                    showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_server_details));

                } else {
                    showAlertDialog(msg.equals("") ? "Error" : CS_Constant.serverConnectionErrorMessage);
                }
            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
    }

    public void showAlertDialog(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Invalid");
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            // Clear
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(getActivity()).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void showSnackbar() {
        try {
            Snackbar snackbar = Snackbar.make(viewBinding.main, context.getResources().getText(R.string.no_connection), Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                login();

                            } catch (Exception e) {
                                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                            }
                        }
                    });
            snackbar.show();


        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}