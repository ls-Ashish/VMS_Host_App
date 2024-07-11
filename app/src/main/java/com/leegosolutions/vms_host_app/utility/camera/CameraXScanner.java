package com.leegosolutions.vms_host_app.utility.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.leegosolutions.vms_host_app.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.leegosolutions.vms_host_app.activity.LoginOrRegistration;
import com.leegosolutions.vms_host_app.databinding.ActivityCameraXscannerBinding;
import com.leegosolutions.vms_host_app.databinding.ActivityLoginOrRegistrationBinding;
import com.leegosolutions.vms_host_app.utility.CS_Utility;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraXScanner extends AppCompatActivity {

    private Context context = CameraXScanner.this;
    private ActivityCameraXscannerBinding viewBinding;

    private static final String TAG = "MLKit Barcode";
    private static final int PERMISSION_CODE = 1001;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private CameraSelector cameraSelector;
    private ProcessCameraProvider cameraProvider;
    private Preview previewUseCase;
    private ImageAnalysis analysisUseCase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera_xscanner);
        try {
            viewBinding = ActivityCameraXscannerBinding.inflate(getLayoutInflater());
            setContentView(viewBinding.getRoot());

//            viewBinding.scanningOverlay.startAnimation();
//            viewBinding.scanOverlay.setBackgroundColor(Color.argb(128, 0, 0, 0));

//            LottieAnimationView lav_Image;
//            viewBinding.animationView.setAnimation("sca5n.json");
//            viewBinding.animationView.playAnimation();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            startCamera();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    public void startCamera() {
        try {
            if(ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                setupCamera();
            } else {
                getPermissions();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void getPermissions() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA_PERMISSION}, PERMISSION_CODE);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        try {
            for (int r : grantResults) {
                if (r == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (requestCode == PERMISSION_CODE) {
                setupCamera();
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void setupCamera() {
        try {
            final ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                    ProcessCameraProvider.getInstance(this);

            int lensFacing = CameraSelector.LENS_FACING_BACK;
            cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

            cameraProviderFuture.addListener(() -> {
                try {
                    cameraProvider = cameraProviderFuture.get();
                    bindAllCameraUseCases();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(TAG, "cameraProviderFuture.addListener Error", e);
                }
            }, ContextCompat.getMainExecutor(this));

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void bindAllCameraUseCases() {
        try {
            if (cameraProvider != null) {
                cameraProvider.unbindAll();
                bindPreviewUseCase();
                bindAnalysisUseCase();
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void bindPreviewUseCase() {
        try {
            if (cameraProvider == null) {
                return;
            }

            if (previewUseCase != null) {
                cameraProvider.unbind(previewUseCase);
            }

            Preview.Builder builder = new Preview.Builder();
            builder.setTargetRotation(getRotation());

            previewUseCase = builder.build();
            previewUseCase.setSurfaceProvider(viewBinding.previewView.getSurfaceProvider());

            try {
                cameraProvider
                        .bindToLifecycle(this, cameraSelector, previewUseCase);
            } catch (Exception e) {
                Log.e(TAG, "Error when bind preview", e);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void bindAnalysisUseCase() {
        try {
            if (cameraProvider == null) {
                return;
            }

            if (analysisUseCase != null) {
                cameraProvider.unbind(analysisUseCase);
            }

            Executor cameraExecutor = Executors.newSingleThreadExecutor();

            ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
            builder.setTargetRotation(getRotation());

            analysisUseCase = builder.build();
            analysisUseCase.setAnalyzer(cameraExecutor, this::analyze);

            try {
                cameraProvider
                        .bindToLifecycle(this, cameraSelector, analysisUseCase);
            } catch (Exception e) {
                Log.e(TAG, "Error when bind analysis", e);
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    protected int getRotation() throws NullPointerException {
        int result = 0;
        try {
            result = viewBinding.previewView.getDisplay().getRotation();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
        return result;
    }

    @SuppressLint("UnsafeOptInUsageError")
    private void analyze(@NonNull ImageProxy image) {
        try {
            if (image.getImage() == null) return;

            InputImage inputImage = InputImage.fromMediaImage(
                    image.getImage(),
                    image.getImageInfo().getRotationDegrees()
            );

            BarcodeScanner barcodeScanner = BarcodeScanning.getClient();

            barcodeScanner.process(inputImage)
                    .addOnSuccessListener(this::onSuccessListener)
                    .addOnFailureListener(e -> Log.e(TAG, "Barcode process failure", e))
                    .addOnCompleteListener(task -> image.close());

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void onSuccessListener(List<Barcode> barcodes) {
        try {
            if (barcodes.size() > 0) {
//                Toast.makeText(this, barcodes.get(0).getDisplayValue(), Toast.LENGTH_SHORT)
//                        .show();
                returnData(barcodes.get(0).getDisplayValue());
            }

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }

    private void returnData(String result) {
        try {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(Activity.RESULT_OK, intent);
            finish();

        } catch (Exception e) {
            new CS_Utility(context).saveError(e, context.getClass().getSimpleName(), new Object() {
            }.getClass().getEnclosingMethod().getName(), String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()));
        }
    }
}