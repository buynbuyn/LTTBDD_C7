package com.example.chuong_7.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chuong_7.R;

import java.io.IOException;
import java.util.Collections;

public class CameraAPIActivity extends AppCompatActivity {
    private static final boolean USE_CAMERA2 = false;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    // Camera1
    private Camera camera1;

    // Camera2
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_apiactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        surfaceView = findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();

        if (USE_CAMERA2) {
            surfaceHolder.addCallback(camera2Callback);
        } else {
            surfaceHolder.addCallback(camera1Callback);
        }

        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    // 🔹 Camera1 callback
    private final SurfaceHolder.Callback camera1Callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            camera1 = Camera.open();
            try {
                camera1.setPreviewDisplay(holder);
                camera1.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
        @Override public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            if (camera1 != null) {
                camera1.stopPreview();
                camera1.release();
                camera1 = null;
            }
        }
    };

    // 🔹 Camera2 callback
    private final SurfaceHolder.Callback camera2Callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                if (ActivityCompat.checkSelfPermission(CameraAPIActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraManager.openCamera(cameraId, camera2StateCallback, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
        @Override public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            if (cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };

    private final CameraDevice.StateCallback camera2StateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            try {
                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                builder.addTarget(surfaceHolder.getSurface());

                cameraDevice.createCaptureSession(Collections.singletonList(surfaceHolder.getSurface()),
                        new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                cameraCaptureSession = session;
                                try {
                                    session.setRepeatingRequest(builder.build(), null, null);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
                        }, null);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
        }

        @Override public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
        }
    };
}