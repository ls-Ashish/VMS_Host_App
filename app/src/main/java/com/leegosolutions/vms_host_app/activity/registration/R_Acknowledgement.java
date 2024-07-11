package com.leegosolutions.vms_host_app.activity.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.leegosolutions.vms_host_app.R;
import com.leegosolutions.vms_host_app.activity.LoginOrRegistration;
import com.leegosolutions.vms_host_app.databinding.ActivityRAcknowledgementBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class R_Acknowledgement extends AppCompatActivity {

    private Context context = R_Acknowledgement.this;
    private ActivityRAcknowledgementBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_racknowledgement);
        try {
            viewBinding = ActivityRAcknowledgementBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            on_Click_Button_Done();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void on_Click_Button_Done() {
        viewBinding.btnDone.setOnClickListener(v -> {
            try {
                goToHomePage();

            } catch (Exception e) {
                new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
                }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
            }
        });
    }

    private void goToHomePage() {
        try {
            Intent intent = new Intent(context, LoginOrRegistration.class);
            // Clear
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

}