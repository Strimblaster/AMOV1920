package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jogar(View view) {
        System.out.println("ola");
    }

    public void jogarMultiplayer(View view) {
        System.out.println("ola");
    }

    public void entrar(View view) {
        System.out.println("ola");
    }
}
