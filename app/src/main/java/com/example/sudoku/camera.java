package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class camera extends AppCompatActivity {

        private static final String TAG = "AndroidCameraApi";
        private Button takePictureButton;
        private ImageButton btnRotate;
        private TextureView textureView;

        private static final String CAMERA_FRONT = "1";
        private static final String CAMERA_BACK = "0";
        private String cameraId = CAMERA_FRONT;

        private static final SparseIntArray ORIENTATIONS = new SparseIntArray(4);

        static {
                ORIENTATIONS.append(Surface.ROTATION_0, 90);
                ORIENTATIONS.append(Surface.ROTATION_90, 0);
                ORIENTATIONS.append(Surface.ROTATION_180, 270);
                ORIENTATIONS.append(Surface.ROTATION_270, 180);
        }

        protected CameraDevice cameraDevice;
        protected CameraCaptureSession cameraCaptureSessions;
        protected CaptureRequest.Builder captureRequestBuilder;
        private Size imageDimension;
        private ImageReader imageReader;
        private static final int REQUEST_CAMERA_PERMISSION = 200;
        private Handler mBackgroundHandler;
        private HandlerThread mBackgroundThread;
        private File file;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_camera);
                textureView = findViewById(R.id.texture);
                assert textureView != null;
                textureView.setSurfaceTextureListener(textureListener);
                takePictureButton = findViewById(R.id.btnCapture);
                btnRotate =  findViewById(R.id.btnRotate);
                assert takePictureButton != null;
                takePictureButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                takePicture();
                        }
                });
                btnRotate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                switchCamera();
                        }
                });
        }

        public void switchCamera() {
                if (cameraId.equals(CAMERA_FRONT)) {
                        cameraId = CAMERA_BACK;
                        closeCamera();
                        reopenCamera();


                } else if (cameraId.equals(CAMERA_BACK)) {
                        cameraId = CAMERA_FRONT;
                        closeCamera();
                        reopenCamera();

                }
        }

        public void reopenCamera() {
                if (textureView.isAvailable()) {
                        openCamera(textureView.getWidth(), textureView.getHeight());
                } else {
                        textureView.setSurfaceTextureListener(textureView.getSurfaceTextureListener());
                }
        }

        TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                        openCamera(textureView.getWidth(), textureView.getHeight());
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                        return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                }
        };
        private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                        Log.e(TAG, "onOpened");
                        cameraDevice = camera;
                        createCameraPreview();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                        cameraDevice.close();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                        cameraDevice.close();
                        cameraDevice = null;
                }
        };

        protected void startBackgroundThread() {
                mBackgroundThread = new HandlerThread("Camera Background");
                mBackgroundThread.start();
                mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        }

        protected void stopBackgroundThread() {
                mBackgroundThread.quitSafely();
                try {
                        mBackgroundThread.join();
                        mBackgroundThread = null;
                        mBackgroundHandler = null;
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
        }

        protected void takePicture() {
                if (null == cameraDevice) {
                        Log.e(TAG, "cameraDevice is null");
                        return;
                }
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
                        Size[] jpegSizes = null;
                        if (characteristics != null) {
                                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                        }
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        int height = size.y;
                        if (jpegSizes != null && 0 < jpegSizes.length) {
                                width = jpegSizes[0].getWidth();
                                height = jpegSizes[0].getHeight();
                        }
                        ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                        List<Surface> outputSurfaces = new ArrayList<Surface>(2);
                        outputSurfaces.add(reader.getSurface());
                        outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
                        final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                        captureBuilder.addTarget(reader.getSurface());
                        captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation(characteristics));
                        file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");
                        ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                                @Override
                                public void onImageAvailable(ImageReader reader) {
                                        Image image = null;
                                        try {
                                                image = reader.acquireLatestImage();
                                                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                                                byte[] bytes = new byte[buffer.capacity()];

                                                buffer.get(bytes);
                                                save(bytes);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        } finally {
                                                if (image != null) {
                                                        image.close();
                                                        Intent resultIntent = new Intent();
                                                        resultIntent.putExtra("path", file.getAbsolutePath());
                                                        setResult(Activity.RESULT_OK, resultIntent);
                                                        finish();
                                                }
                                        }
                                }

                                private void save(byte[] bytes) throws IOException {
                                        OutputStream output = null;
                                        try {
                                                output = new FileOutputStream(file);
                                                output.write(bytes);
                                        } finally {
                                                if (null != output) {
                                                        output.close();
                                                }
                                        }
                                }
                        };

                        reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
                        final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                                @Override
                                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                        super.onCaptureCompleted(session, request, result);
                                        createCameraPreview();
                                }
                        };
                        cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(CameraCaptureSession session) {
                                        try {
                                                session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                                        } catch (CameraAccessException e) {
                                                e.printStackTrace();
                                        }
                                }

                                @Override
                                public void onConfigureFailed(CameraCaptureSession session) {
                                }
                        }, mBackgroundHandler);
                } catch (CameraAccessException e) {
                        e.printStackTrace();
                }
        }

        protected void createCameraPreview() {
                try {
                        SurfaceTexture texture = textureView.getSurfaceTexture();
                        assert texture != null;
                        texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
                        Surface surface = new Surface(texture);
                        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequestBuilder.addTarget(surface);
                        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                                        if (null == cameraDevice) {
                                                return;
                                        }
                                        cameraCaptureSessions = cameraCaptureSession;
                                        updatePreview();
                                }

                                @Override
                                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                                        Toast.makeText(camera.this, "Configuration change", Toast.LENGTH_SHORT).show();
                                }
                        }, null);
                } catch (CameraAccessException e) {
                        e.printStackTrace();
                }
        }

        private void openCamera(int width, int height ) {
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                Log.e(TAG, "is camera open");
                try {
                        assert manager != null;
                        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                        assert map != null;
                        imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions(camera.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                                return;
                        }
                        manager.openCamera(cameraId, stateCallback, null);
                } catch (CameraAccessException e) {
                        e.printStackTrace();
                }
                Log.e(TAG, "openCamera X");
        }

        protected void updatePreview() {
                if (null == cameraDevice) {
                        Log.e(TAG, "updatePreview error, return");
                }
                captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                try {
                        cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
                } catch (CameraAccessException e) {
                        e.printStackTrace();
                }
        }

        private void closeCamera() {
                if (null != cameraDevice) {
                        cameraDevice.close();
                        cameraDevice = null;
                }
                if (null != imageReader) {
                        imageReader.close();
                        imageReader = null;
                }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == REQUEST_CAMERA_PERMISSION) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                                Toast.makeText(camera.this, "Estão em falta algumas permissões!", Toast.LENGTH_LONG).show();
                                finish();
                        }
                }
        }

        @Override
        protected void onResume() {
                super.onResume();
                Log.e(TAG, "onResume");
                startBackgroundThread();
                if (textureView.isAvailable()) {
                        openCamera(textureView.getWidth(), textureView.getHeight());
                } else {
                        textureView.setSurfaceTextureListener(textureListener);
                }
        }

        @Override
        protected void onPause() {
                Log.e(TAG, "onPause");
                stopBackgroundThread();
                super.onPause();
        }

        private int getJpegOrientation(CameraCharacteristics c) {
                int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);
                int deviceRotation = camera.this.getWindowManager().getDefaultDisplay().getRotation();
                return (ORIENTATIONS.get(deviceRotation) + sensorOrientation + 270) % 360;
        }
}
