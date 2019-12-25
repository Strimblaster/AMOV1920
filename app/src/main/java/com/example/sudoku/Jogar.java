package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.M3_MultiplayerLan.LanMode;


public class Jogar extends AppCompatActivity {

    private Button btnSinglePlayer, btnMultiPlayer, btnConnect, btnVoltar;

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

        btnSinglePlayer = findViewById(R.id.btnSinglePlayer);
        btnMultiPlayer = findViewById(R.id.btnMultiPlayer);
        btnConnect = findViewById(R.id.btnConnect);
        btnVoltar = findViewById(R.id.btnBack);

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Jogar.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
            }
        });



        btnSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Jogar.this,  DifficultyView.class);
                intent.putExtra("mode","M1");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
                finish();
            }
        });
        btnMultiPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Jogar.this,  DifficultyView.class);
                intent.putExtra("mode","M2");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
                finish();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Jogar.this,  LanMode.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
                finish();
            }
        });
    }

}
