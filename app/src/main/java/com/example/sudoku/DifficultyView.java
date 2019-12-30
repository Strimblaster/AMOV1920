package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.M1_SinglePlayer.SinglePlayer;
import com.example.sudoku.M2_MultiPlayerLocal.Multiplayer;
import com.example.sudoku.M3_MultiplayerLan.LanMultiplayer;

import java.net.InetAddress;

public class DifficultyView extends AppCompatActivity {
    Button btnEasy, btnMedium, btnHard, btnRandom, btnBack;
    String mode, player;
    ProgressBar progressBar;
    String ip;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Jogar.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_view);

        btnEasy = findViewById(R.id.btnEasy);
        btnHard = findViewById(R.id.btnHard);
        btnMedium= findViewById(R.id.btnMedium);
        btnRandom = findViewById(R.id.btnRandom);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.pbLoader);

        mode =  getIntent().getStringExtra("mode");
        player =  getIntent().getStringExtra("player");
        ip = getIntent().getStringExtra("ip");


        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("M3")){
                    goToLanMultiplayer(Difficulty.easy, player);
                }else if (mode.equals("M2")){
                    goToMultiPlayer(Difficulty.easy);
                }else {
                    goToSinglePlayer(Difficulty.easy);
                }

            }
        });
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("M3")){
                    goToLanMultiplayer(Difficulty.medium, player);
                }else if (mode.equals("M2")){
                    goToMultiPlayer(Difficulty.medium);
                }else {
                    goToSinglePlayer(Difficulty.medium);
                }
            }
        });
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("M3")){
                    goToLanMultiplayer(Difficulty.hard, player);
                }else if (mode.equals("M2")){
                    goToMultiPlayer(Difficulty.hard);
                }else {
                    goToSinglePlayer(Difficulty.hard);
                }
            }
        });
        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("M3")){
                    goToLanMultiplayer(Difficulty.random, player);
                }else if (mode.equals("M2")){
                    goToMultiPlayer(Difficulty.random);
                }else {
                    goToSinglePlayer(Difficulty.random);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DifficultyView.this, Jogar.class));
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
            }
        });

    }


    private void goToSinglePlayer(Difficulty difficulty) {
        Thread thread = new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        thread.start();
        Intent intent = new Intent(this,  SinglePlayer.class);
        intent.putExtra("Difficulty",difficulty);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        progressBar.setVisibility(View.GONE);
        finish();
    }

    private void goToMultiPlayer(Difficulty difficulty) {
        Thread thread = new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        thread.start();
        Intent intent = new Intent(this,  Multiplayer.class);
        intent.putExtra("Difficulty",difficulty);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        progressBar.setVisibility(View.GONE);
        finish();
    }

    private void goToLanMultiplayer(Difficulty difficulty, String player) {
        Toast.makeText(getApplicationContext(), "IP: "+ ip, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,  LanMultiplayer.class);
        intent.putExtra("Difficulty",difficulty);
        intent.putExtra("player",player);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        progressBar.setVisibility(View.GONE);
        finish();
    }
}
