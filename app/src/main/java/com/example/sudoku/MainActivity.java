package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.Core.AppDatabase;


public class MainActivity extends AppCompatActivity {
    public static AppDatabase appDatabase;
    private Button btnJogar, btnProfile, btnHistorico;

    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appDatabase = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, "database").build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnJogar = findViewById(R.id.btnJogar);
        btnProfile = findViewById(R.id.btnProfile);
        btnHistorico = findViewById(R.id.btnHistorico);

        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Jogar.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  Profile.class);
                startActivity(intent);
            }
        });
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,  Historico.class);
                startActivity(intent);
            }
        });

    }
}
