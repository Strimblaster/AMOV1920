package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    TextView tvTitle, tvMessage;
    Button btnBack;
    private  String message, title;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Jogar.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        title = getIntent().getStringExtra("title");
        message = getIntent().getStringExtra("message");
        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        btnBack = findViewById(R.id.btnBack);

        tvTitle.setText(title);
        tvMessage.setText(message);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this,  Jogar.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
                finish();
            }
        });
    }
}
