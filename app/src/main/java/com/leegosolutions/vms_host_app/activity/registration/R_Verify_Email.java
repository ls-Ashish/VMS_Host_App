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
import com.leegosolutions.vms_host_app.databinding.ActivityRVerifyEmailBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

public class R_Verify_Email extends AppCompatActivity {

    private Context context = R_Verify_Email.this;
    private ActivityRVerifyEmailBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rverify_email);
        try {
            viewBinding = ActivityRVerifyEmailBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

            on_Click_Button_Submit();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

    private void on_Click_Button_Submit() {
        viewBinding.btnSubmit.setOnClickListener(v -> {
            try {
                nextPage(R_Acknowledgement.class);

            } catch (Exception e) {
                new CS_Utility(context).saveError(e);
            }
        });
    }

    private void nextPage(Class aClass) {
        try {
            Intent intent = new Intent(context, aClass);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e);
        }
    }

}