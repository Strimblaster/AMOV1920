package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.sudoku.Core.Player;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Profile extends AppCompatActivity {
        Button btnPhoto, btnCancel, btnSave;
        ImageView photo;
        TextInputEditText tfName;
        Player player;
        String path;
        private static final int PHOTO_REQUEST_CODE = 0;
        private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
        private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.CAMERA};
        @Override
        public void onBackPressed() {
                if (player != null) {
                        startActivity(new Intent(this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                        finish();
                } else {
                        Toast.makeText(Profile.this, "Crie um prefil primeiro!", Toast.LENGTH_LONG).show();
                }
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                checkPermissions();
                setContentView(R.layout.activity_profile);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                btnPhoto = findViewById(R.id.btnPhoto);
                photo = findViewById(R.id.ivPhoto);
                btnCancel = findViewById(R.id.btnCancel);
                btnSave = findViewById(R.id.btnSave);
                tfName = findViewById(R.id.tfName);
                photo.setImageDrawable(getResources().getDrawable(R.mipmap.ic_placeholder));
                Thread thread = new Thread() {
                        @Override
                        public void run() {
                                List<Player> players = MainActivity.appDatabase.player().getPlayer();
                                if (!players.isEmpty()) {
                                        player = players.get(0);
                                        Profile.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                        tfName.setText(player.getName());
                                                        File photoFile = new File(player.getPhotoPath());

                                                        if (photoFile.exists()) {
                                                                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                                                float degrees = -90;
                                                                Matrix matrix = new Matrix();
                                                                matrix.setRotate(degrees);
                                                                Bitmap bOutput = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                                                                photo.setImageBitmap(bOutput);
                                                        }
                                                }
                                        });
                                }
                        }
                };
                thread.start();
                path = "";

                btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (path.isEmpty()) {
                                        Toast.makeText(Profile.this, "Coloque uma fotografia primeiro!", Toast.LENGTH_LONG).show();
                                } else {
                                        String name = Objects.requireNonNull(tfName.getText()).toString();
                                        if (name.isEmpty()) {
                                                Toast.makeText(Profile.this, "Coloque um nome primeiro!", Toast.LENGTH_LONG).show();
                                        } else {
                                                player = new Player(name, path);
                                                Thread thread = new Thread() {
                                                        @Override
                                                        public void run() {
                                                                MainActivity.appDatabase.player().deleteAll();
                                                                MainActivity.appDatabase.player().insertPlayer(player);
                                                                Profile.this.runOnUiThread(new Runnable() {
                                                                        public void run() {
                                                                                Toast.makeText(Profile.this, "Informação guardada!", Toast.LENGTH_LONG).show();
                                                                        }
                                                                });
                                                        }
                                                };
                                                thread.start();
                                        }
                                }
                        }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (player != null) {
                                        startActivity(new Intent(Profile.this, MainActivity.class));
                                        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                        finish();
                                } else {
                                        Toast.makeText(Profile.this, "Crie um prefil primeiro!", Toast.LENGTH_LONG).show();
                                }
                        }
                });

                btnPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(Profile.this, camera.class);
                                startActivityForResult(intent, PHOTO_REQUEST_CODE);
                                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        }
                });
        }


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == PHOTO_REQUEST_CODE) {
                        if (resultCode == Activity.RESULT_OK) {
                                path = data.getStringExtra("path");
                                File photoFile = new File(path);

                                if (photoFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                                        float degrees = -90;
                                        Matrix matrix = new Matrix();
                                        matrix.setRotate(degrees);
                                        Bitmap bOutput = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
                                        photo.setImageBitmap(bOutput);
                                }
                        }
                }
        }

        protected void checkPermissions() {
                final List<String> missingPermissions = new ArrayList<>();
                for (final String permission : REQUIRED_SDK_PERMISSIONS) {
                        final int result = ContextCompat.checkSelfPermission(this, permission);
                        if (result != PackageManager.PERMISSION_GRANTED) {
                                missingPermissions.add(permission);
                        }
                }
                if (!missingPermissions.isEmpty()) {
                        final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
                } else {
                        final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
                        Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
                        onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
                }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                               @NonNull int[] grantResults) {
                switch (requestCode) {
                        case REQUEST_CODE_ASK_PERMISSIONS:
                                for (int index = permissions.length - 1; index >= 0; --index) {
                                        if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                                                Toast.makeText(this, "Required permission '" + permissions[index]
                                                    + "' not granted, exiting", Toast.LENGTH_LONG).show();
                                                finish();
                                                return;
                                        }
                                }
                                break;
                }
        }
}
