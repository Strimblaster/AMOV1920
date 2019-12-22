package com.example.sudoku;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.lang.reflect.Method;

public class Foto extends AppCompatActivity {
//
//    EditText edTitle;
//    Button btnImage;
//    FrameLayout frPreview;
//    Drawable edSaveDrawable=null;
//
//    String imageFilePath = "/sdcard/tmp_photo.jpg";
//    Boolean isCamera = false;
//
//    static final int REQUEST_PERMISSION_RW = 1;
//    static final int ACTIVITY_PICK = 10;
//    static final int ACTIVITY_CAPTURE = 20;
//    static final String IS_CAMERA_FLAG = "isCamera";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_choose_background_image);
//
//        edTitle = findViewById(R.id.edTitle);
//        btnImage = findViewById(R.id.btImage);
//        frPreview = findViewById(R.id.frPreview);
//
//        if (getIntent() != null)
//            isCamera = getIntent().getBooleanExtra(IS_CAMERA_FLAG, false);
//
//        btnImage.setText(getString(isCamera ? R.string.btn_capture_from_camera:R.string.btn_choose_from_gallery));
//
//        Utils.setBackgroundFromAsset(frPreview,"images/isec.jpg");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
//                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                btnImage.setEnabled(false);
//                requestPermissions(new String[]{
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_RW);
//            }
//        } else
//            btnImage.setEnabled(true);
//        if (Build.VERSION.SDK_INT >= 24) {
//            try {
//                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                m.invoke(null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        edSaveDrawable = edTitle.getBackground();
//        edTitle.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                edTitle.setBackground(edSaveDrawable);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requestCode == REQUEST_PERMISSION_RW) {
//            btnImage.setEnabled(
//                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater mi = getMenuInflater();
//        mi.inflate(R.menu.menu_create, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (btnImage.isEnabled() && item.getItemId() == R.id.opCreate) {
//            String strTitle = edTitle.getText().toString();
//            if (strTitle.length() < 1) {
//                //Toast.makeText(this,getString(R.string.error_no_title),Toast.LENGTH_LONG).show();
//                Snackbar.make(edTitle, getString(R.string.error_no_title), Snackbar.LENGTH_LONG).show();
//                edTitle.setBackgroundColor(Color.RED);
//                return true;
//            }
//            if ( !(new File(imageFilePath).exists())) {
//                //Toast.makeText(this,getString(R.string.error_no_image),Toast.LENGTH_LONG).show();
//                Snackbar.make(btnImage, getString(R.string.error_no_image), Snackbar.LENGTH_LONG).show();
//                return true;
//            }
//
//            Intent intent = new Intent(this, SketchAreaActivity.class);
//
//            intent.putExtra("title", strTitle);
//            intent.putExtra("imageFile", imageFilePath);
//
//            startActivity(intent);
//            finish();
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    public void onImage(View view) {
//        if (isCamera) {
//            Uri fileUri;
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            fileUri = Uri.fromFile(new File(imageFilePath));
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            startActivityForResult(intent, ACTIVITY_CAPTURE);
//
//        } else {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent,ACTIVITY_PICK);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ACTIVITY_PICK && resultCode==RESULT_OK
//                && data != null && data.getData() != null) {
//            Uri _uri = data.getData();
//            if (_uri != null) {
//                Cursor cursor = getContentResolver().query(_uri,
//                        new String[]{MediaStore.Images.ImageColumns.DATA},
//                        null, null, null);
//
//                cursor.moveToFirst();
//
//                imageFilePath =  cursor.getString(0);
//
//                cursor.close();
//
//                Utils.setBackground(frPreview,imageFilePath);
//            }
//        } else if (requestCode == ACTIVITY_CAPTURE && resultCode==RESULT_OK) {
//            Utils.setBackground(frPreview,imageFilePath);
//        }
//    }
}
