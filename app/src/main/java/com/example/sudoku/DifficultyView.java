package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.M1_SinglePlayer.SinglePlayer;
import com.example.sudoku.M2_MultiPlayerLocal.Multiplayer;
import com.example.sudoku.M3_MultiplayerLan.LanClient;
import com.example.sudoku.M3_MultiplayerLan.LanServer;
import com.example.sudoku.M3_MultiplayerLan.ViewLanClient;

public class DifficultyView extends AppCompatActivity {
        Button btnEasy, btnMedium, btnHard, btnRandom, btnBack, btnRules;
        String mode;
        ProgressBar progressBar;

        @Override
        public void onBackPressed() {
                startActivity(new Intent(this, Jogar.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_difficulty_view);

                btnEasy = findViewById(R.id.btnEasy);
                btnHard = findViewById(R.id.btnHard);
                btnMedium = findViewById(R.id.btnMedium);
                btnRandom = findViewById(R.id.btnRandom);
                btnBack = findViewById(R.id.btnBack);
                btnRules = findViewById(R.id.btnRules);
                progressBar = findViewById(R.id.pbLoader);

                mode = getIntent().getStringExtra("mode");

                btnRules.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(DifficultyView.this, Rules.class);
                                intent.putExtra("mode", mode);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                finish();
                        }
                });

                btnEasy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mode.equals("M3")) {
                                        goToLanMultiplayer(Difficulty.easy);
                                } else if (mode.equals("M2")) {
                                        goToMultiPlayer(Difficulty.easy);
                                } else {
                                        goToSinglePlayer(Difficulty.easy);
                                }

                        }
                });
                btnMedium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mode.equals("M3")) {
                                        goToLanMultiplayer(Difficulty.medium);
                                } else if (mode.equals("M2")) {
                                        goToMultiPlayer(Difficulty.medium);
                                } else {
                                        goToSinglePlayer(Difficulty.medium);
                                }
                        }
                });
                btnHard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mode.equals("M3")) {
                                        goToLanMultiplayer(Difficulty.hard);
                                } else if (mode.equals("M2")) {
                                        goToMultiPlayer(Difficulty.hard);
                                } else {
                                        goToSinglePlayer(Difficulty.hard);
                                }
                        }
                });
                btnRandom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (mode.equals("M3")) {
                                        goToLanMultiplayer(Difficulty.random);
                                } else if (mode.equals("M2")) {
                                        goToMultiPlayer(Difficulty.random);
                                } else {
                                        goToSinglePlayer(Difficulty.random);
                                }
                        }
                });

                btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(DifficultyView.this, Jogar.class));
                                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                finish();
                        }
                });

        }


        private void goToSinglePlayer(Difficulty difficulty) {
                Thread thread = new Thread() {
                        public void run() {
                                runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                progressBar.setVisibility(View.VISIBLE);
                                        }
                                });
                        }
                };
                thread.start();
                Intent intent = new Intent(this, SinglePlayer.class);
                intent.putExtra("Difficulty", difficulty);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                progressBar.setVisibility(View.GONE);
                finish();
        }

        private void goToMultiPlayer(Difficulty difficulty) {
                Thread thread = new Thread() {
                        public void run() {
                                runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                                progressBar.setVisibility(View.VISIBLE);
                                        }
                                });
                        }
                };
                thread.start();
                Intent intent = new Intent(this, Multiplayer.class);
                intent.putExtra("Difficulty", difficulty);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                progressBar.setVisibility(View.GONE);
                finish();
        }

        private void goToLanMultiplayer(Difficulty difficulty) {
                String type = getIntent().getStringExtra("type");
                Intent intent;
                assert type != null;
                if (type.equals("server")) {
                        intent = new Intent(this, LanServer.class);
                        intent.putExtra("totalPlayers", getIntent().getIntExtra("totalPlayers", 1));
                } else {
                        intent = new Intent(this, LanClient.class);
                }
                intent.putExtra("Difficulty", difficulty);
                intent.putExtra("type", type);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                progressBar.setVisibility(View.GONE);
                finish();
        }
}
