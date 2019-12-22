package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Profile extends AppCompatActivity {
    private Button btnFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnFoto = findViewById(R.id.btnFoto);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCarregarFoto();
            }
        });
    }

    private void goToCarregarFoto() {
        Intent intent = new Intent(this, Foto.class);
        //intent.putExtra(Foto.IS_CAMERA_FLAG,true);
        startActivity(intent);
    }

}
