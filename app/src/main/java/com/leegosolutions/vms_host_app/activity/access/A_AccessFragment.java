package com.leegosolutions.vms_host_app.activity.access;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.ScanServerDetails;
import com.leegosolutions.vms_host_app.activity.home.Home;
import com.leegosolutions.vms_host_app.activity.login.fragment.L_EmailFragment;
import com.leegosolutions.vms_host_app.api.CS_API_URL;
import com.leegosolutions.vms_host_app.database.Dao.CS_Dao_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_AccessDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_LoginDetails;
import com.leegosolutions.vms_host_app.database.action.CS_Action_ServerDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_AccessDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_LoginDetails;
import com.leegosolutions.vms_host_app.database.entity.CS_Entity_ServerDetails;
import com.leegosolutions.vms_host_app.databinding.FragmentAAccessBinding;
import com.leegosolutions.vms_host_app.databinding.FragmentHVisitorsBinding;
import com.leegosolutions.vms_host_app.model.CS_VisitorsModel;
import com.leegosolutions.vms_host_app.utility.CS_Connection;
import com.leegosolutions.vms_host_app.utility.CS_Constant;
import com.leegosolutions.vms_host_app.utility.CS_ED;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class A_AccessFragment extends Fragment {

    private Context context;
    private FragmentAAccessBinding viewBinding;
    private BarcodeEncoder barcodeEncoder;
    private Bitmap bitmap;
    private long refreshTime = 5000; // 5 x 1000 = 5 sec
    private Handler handler = new Handler();
    private Runnable runnable;
    private String sourceId = "", employeeNo = "", employeeName = "", employeeUnit = "", employeeVehicleNo = "", employeeNo_Encrypted = "";

    public A_AccessFragment() {
        // default constructor required, if no default constructor than will crash at orientation change
    }

    public A_AccessFragment(Context context) {
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
//        return inflater.inflate(R.layout.fragment_a_access, container, false);
        try {
            viewBinding = FragmentAAccessBinding.inflate(inflater, container, false);

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
            // todo ------------------ testing -----------------------------------------------------------------------------------------
            employeeNo = "34387";
            employeeNo_Encrypted = "yjepDd8wZZ/kstpGJmgycQ==";
            generateQRCode();
            viewBinding.tvHostEmployeeNo.setText(getResources().getString(R.string.access_host_employee_no) + " " + employeeNo);
            // todo ------------------ testing -----------------------------------------------------------------------------------------

//            fetchAccessData(); // todo - comment for testing apk
            onClick_Button_Update();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            refreshQRCode();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            handler.removeCallbacks(runnable);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void fetchAccessData() {
        try {
            // check internet connection
            if (new CS_Connection(context).getStatus()) {
                new FetchAccessData().execute();

            } else {
                new FetchAccessDetailsFromSQLite().execute();
//                showSnackbar();
            }

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
                                fetchAccessData();

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

    private void onClick_Button_Update() {
        viewBinding.btnUpdateEmployeeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    getEmployeeId();
//                    new CS_Utility(context).showToast("Updated", 0);

                } catch (Exception e) {
                    new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                    }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
                }
            }
        });
    }

    private void refreshQRCode() {
        try {
            if (refreshTime > 0) {
                handler.postDelayed(runnable = new Runnable() {
                    public void run() {
                        handler.postDelayed(runnable, refreshTime);

                        generateQRCode();
                    }
                }, refreshTime);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    class FetchAccessData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressdialog;
        private String result = "", msg = "";
        private String bCode = "", tCode = "", clientSecret = "", appToken = "", baseURL = "", buildingId = "", tenantId = "", loginId = "";

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
        protected Void doInBackground(Void... voids) {
            try {
                OkHttpClient client = new CS_Utility(context).getOkHttpClient();

                CS_Entity_ServerDetails model = new CS_Action_ServerDetails(context).getServerDetails();

                if (model != null) {
                    bCode = model.getSD_BCode();
                    tCode = model.getSD_TCode();
                    clientSecret = model.getSD_ClientSecret();
                    appToken = model.getSD_AppToken();
                    baseURL = CS_ED.Decrypt(model.getSD_BaseURL());
                    buildingId = model.getSD_BU_ID();
                    tenantId = model.getSD_TE_ID();

                }
                if (!bCode.equals("") && !tCode.equals("") && !clientSecret.equals("") && !baseURL.equals("")) {

                    // Fetch login id
                    CS_Entity_LoginDetails loginModel = new CS_Action_LoginDetails(context).getLoginDetails();
                    loginId = loginModel.getLD_SourceId();

                    // todo - test photo is there in assert, remove after testing

                    InputStream ims = context.getAssets().open("test_image.jpg");
                    Drawable d = Drawable.createFromStream(ims, null);

                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] bitmapdata = stream.toByteArray();

                    ims.close();

                    File imageFile = bitmapToFile(bitmap, "image.jpg");

                    JSONObject jObject = new JSONObject();
                    jObject.put("HostId", loginId);
                    jObject.put("Logged_BU_Id", buildingId);
                    jObject.put("Logged_TE_Id", tenantId);
                    jObject.put("Flag", "Fetch_Access_Data");

                    RequestBody body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("Id", "70")
                            .addFormDataPart("Json_Data", String.valueOf(jObject))
                            .addFormDataPart("App_Token", appToken)
//                            .addFormDataPart("App_Token","1161")
//                            .addFormDataPart("Binary_Data_1", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpeg"), imageFile))
//                            .addFormDataPart("Binary_Data_1", imageFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                            .build();

                    Request request = new Request.Builder()
                            .url(baseURL + CS_API_URL.Access)
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
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                result = jsonObject.getString("Result");
                                msg = jsonObject.getString("Msg");

                                if (result.equals("1")) {
                                    sourceId = jsonObject.getString("Source_Id");
                                    employeeName = jsonObject.getString("EmployeeName");
                                    employeeNo = jsonObject.getString("EmployeeNo");
                                    employeeUnit = jsonObject.getString("TenantName");
                                    employeeVehicleNo = jsonObject.getString("VehicleNo");
                                    employeeNo_Encrypted = jsonObject.getString("EmployeeNo_Encrypted");
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

                    // Clean
                    new CS_Action_AccessDetails(context).deleteAccessDetails();

                    // Insert access data
                    CS_Entity_AccessDetails model = new CS_Entity_AccessDetails(sourceId, CS_ED.Encrypt(employeeNo), CS_ED.Encrypt(employeeName), CS_ED.Encrypt(employeeUnit), CS_ED.Encrypt(employeeVehicleNo), new CS_Utility(context).getDateTime(), "", employeeNo_Encrypted);
                    new CS_Action_AccessDetails(context).insertAccessDetails(model);

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
                    new FetchAccessDetailsFromSQLite().execute();


//                    if (dataInserted) {
//
//                    } else {
//                        showAlertDialog(getResources().getString(R.string.access_data_save_error));
//                    }

                } else if (loginId.equals("")) {
                    showAlertDialog("Login id is blank.");

                } else if (bCode.equals("") || tCode.equals("") || clientSecret.equals("") || baseURL.equals("")) {
                    showAlertDialog(getResources().getString(R.string.scan_server_details_invalid_server_details));

                } else {
                    showAlertDialog(!msg.equals("") ? msg : CS_Constant.serverConnectionErrorMessage);
                }

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
    }

    // Example for Bitmap:
    private File bitmapToFile(Bitmap bitmap, String fileName) throws IOException {
        File file = new File(context.getCacheDir(), fileName);
        file.createNewFile();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos); // Adjust quality as needed
        byte[] bitmapData = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return file;
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
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    public class FetchAccessDetailsFromSQLite extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CS_Entity_AccessDetails model = new CS_Action_AccessDetails(context).getAccessDetails();
                if (model != null) {
                    sourceId = model.getAD_SourceId();
                    employeeNo = CS_ED.Decrypt(model.getAD_E_No());
                    employeeName = CS_ED.Decrypt(model.getAD_E_Name());
                    employeeUnit = CS_ED.Decrypt(model.getAD_E_Unit());
                    employeeVehicleNo = CS_ED.Decrypt(model.getAD_E_VehicleNo());
                    employeeNo_Encrypted = model.getAD_E_No_Encrypted();

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
                generateQRCode();
                setAccessDetails();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        }
    }

    private void setAccessDetails() {
        try {
            viewBinding.tvHostEmployeeNo.setText(getResources().getString(R.string.access_host_employee_no) + " " + employeeNo);
            viewBinding.tvHostName.setText(employeeName);
            viewBinding.tvHostVehicleNo.setText(employeeVehicleNo);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void generateQRCode() {
        try {
            if (!employeeNo_Encrypted.isEmpty()) {
                String currentDateTime = new CS_Utility(context).getDateTime().replace(" ", "T");

                String qrcodeValue = "<body><en>" + employeeNo_Encrypted + "</en><mct>" + CS_ED.Encrypt(currentDateTime) + "</mct></body>";
                String qrcodeValueDecrypt = "<body><en>" + employeeNo_Encrypted + "</en><mct>" + currentDateTime + "</mct></body>";

                barcodeEncoder = new BarcodeEncoder();
                bitmap = barcodeEncoder.encodeBitmap(qrcodeValue, BarcodeFormat.QR_CODE, 400, 400);
                viewBinding.ivAccessQRCode.setImageBitmap(bitmap);

                viewBinding.tvQRCodeValue.setText(qrcodeValue + "\n\n" + qrcodeValueDecrypt);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}