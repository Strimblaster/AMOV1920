package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button btnJogar, btnProfile, btnHistorico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnJogar = findViewById(R.id.btnJogar);
        btnProfile = findViewById(R.id.btnProfile);
        btnHistorico = findViewById(R.id.btnHistorico);

        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJogar();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHistorico();
            }
        });

    }

    private void goToJogar() {
        Intent intent = new Intent(this,  Jogar.class);
        System.out.println("ola");
        startActivity(intent);
    }

    private void goToProfile() {
        Intent intent = new Intent(this,  Profile.class);
        startActivity(intent);
    }

    private void goToHistorico() {
        Intent intent = new Intent(this,  Historico.class);
        startActivity(intent);
    }
}
