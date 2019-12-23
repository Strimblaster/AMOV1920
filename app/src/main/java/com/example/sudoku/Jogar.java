package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class Jogar extends AppCompatActivity {

    private Button btnSinglePlayer, btnMultiPlayer, btnConnect;
    private ProgressBar progressBar;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        btnSinglePlayer = findViewById(R.id.btnSinglePlayer);
        btnMultiPlayer = findViewById(R.id.btnMultiPlayer);
        btnConnect = findViewById(R.id.btnConnect);
        progressBar = findViewById(R.id.pbLoader);
        progressBar.setVisibility(View.GONE);

        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                goToSinglePlayer();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

    private void goToSinglePlayer() {
        Intent intent = new Intent(this,  SinglePlayer.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
        finish();
    }

    private void goToMultiPlayer() {
        Intent intent = new Intent(this,  Multiplayer.class);
        startActivity(intent);
        finish();
    }

    private void goToConnect() {
        Intent intent = new Intent(this,  Connect.class);
        startActivity(intent);
        finish();
    }
}
