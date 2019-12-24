package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class Jogar extends AppCompatActivity {

    private Button btnSinglePlayer, btnMultiPlayer, btnConnect, btnSpEasy,btnSpHard,btnSpMedium, btnSpRandom;
    private ProgressBar progressBar;
    private LinearLayout spDifficulty;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_jogar);
        spDifficulty = findViewById(R.id.layoutDificuldade);
        btnSinglePlayer = findViewById(R.id.btnSinglePlayer);
        btnMultiPlayer = findViewById(R.id.btnMultiPlayer);
        btnConnect = findViewById(R.id.btnConnect);
        progressBar = findViewById(R.id.pbLoader);
        btnSpEasy = findViewById(R.id.btnEasy);
        btnSpHard = findViewById(R.id.btnHard);
        btnSpMedium = findViewById(R.id.btnMedium);
        btnSpRandom = findViewById(R.id.btnRandom);

        btnSpEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSinglePlayer(Difficulty.easy);
            }
        });
        btnSpHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSinglePlayer(Difficulty.hard);
            }
        });
        btnSpMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSinglePlayer(Difficulty.medium);
            }
        });
        btnSpRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSinglePlayer(Difficulty.random);
            }
        });


        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spDifficulty.getVisibility() == View.VISIBLE){
                    spDifficulty.setVisibility(View.GONE);
                }else{
                    spDifficulty.setVisibility(View.VISIBLE);
                }
            }
        });
        btnMultiPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMultiPlayer();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToConnect();
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
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        Intent intent = new Intent(this,  SinglePlayer.class);
        intent.putExtra("Difficulty",difficulty);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        progressBar.setVisibility(View.GONE);
        spDifficulty.setVisibility(View.GONE);
        finish();
    }

    private void goToMultiPlayer() {
        Intent intent = new Intent(this,  Multiplayer.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();
    }

    private void goToConnect() {
        Intent intent = new Intent(this,  Connect.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();
    }

}
