package com.leegosolutions.vms_host_app.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.databinding.ActivityRegistrationBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class R_Registration extends AppCompatActivity {

    private Context context = R_Registration.this;
    private ActivityRegistrationBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
        try {
            viewBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            on_Click_Button_Next();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Button_Next() {
        viewBinding.btnNext.setOnClickListener(v -> {
            try {
                nextPage(R_Verify_MobileNo.class);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}