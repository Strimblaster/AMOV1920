package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Jogar extends AppCompatActivity {

    private Button btnSinglePlayer, btnMultiPlayer, btnConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnSinglePlayer = findViewById(R.id.btnSinglePlayer);
        btnMultiPlayer = findViewById(R.id.btnMultiPlayer);
        btnConnect = findViewById(R.id.btnConnect);

        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSinglePlayer();
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
    }

    private void goToMultiPlayer() {
        Intent intent = new Intent(this,  Multiplayer.class);
        startActivity(intent);
    }

    private void goToConnect() {
        Intent intent = new Intent(this,  Connect.class);
        startActivity(intent);
    }
}
