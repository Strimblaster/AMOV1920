package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudoku.Core.AppDatabase;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;

import java.util.List;


public class MainActivity extends AppCompatActivity {
        public static AppDatabase appDatabase;
        private Button btnJogar, btnProfile, btnHistorico;
        public static Player player;
        private boolean connected;
        private boolean cameraAndStorage;

        @Override
        public void onBackPressed() {
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                connected = false;
                cameraAndStorage = false;
                Thread connectionChecker = new connectionChecker();
                connectionChecker.start();
                Thread cameraAndStorageChecker = new cameraAndStorageChecker();
                cameraAndStorageChecker.start();
                appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main_menu);

                btnJogar = findViewById(R.id.btnJogar);
                btnProfile = findViewById(R.id.btnProfile);
                btnHistorico = findViewById(R.id.btnHistorico);

                Thread thread = new Thread() {
                        @Override
                        public void run() {
                                List<Player> players = MainActivity.appDatabase.player().getPlayer();
                                if (players.isEmpty()) {
                                        Intent intent = new Intent(MainActivity.this, Profile.class);
                                        startActivity(intent);
                                        finish();
                                } else {
                                        player = players.get(0);
                                }
                        }
                };
                thread.start();

                btnJogar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (connected) {

                                        Intent intent = new Intent(MainActivity.this, Jogar.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                        finish();
                                } else {
                                        Toast.makeText(MainActivity.this, "Por favor ligue-se a internet!", Toast.LENGTH_LONG).show();
                                }
                        }
                });

                btnProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (cameraAndStorage) {
                                        Intent intent = new Intent(MainActivity.this, Profile.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                        finish();
                                } else {
                                        Toast.makeText(MainActivity.this, "Conceda as permissões necessárias!", Toast.LENGTH_LONG).show();
                                }
                        }
                });
                btnHistorico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Historico.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                finish();
                        }
                });

        }

        private class cameraAndStorageChecker extends Thread{
                @Override
                public void run() {
                        super.run();
                        while (!cameraAndStorage){
                                cameraAndStorage = (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                                        || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                        }
                }
        }

        private class connectionChecker extends Thread {
                public void run() {
                        while (!connected) {
                                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
                        }
                }
        }

}
