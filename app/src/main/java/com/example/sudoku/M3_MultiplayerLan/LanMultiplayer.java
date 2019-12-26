package com.example.sudoku.M3_MultiplayerLan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sudoku.Core.Client;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Server;
import com.example.sudoku.Jogar;
import com.example.sudoku.R;

import java.io.IOException;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LanMultiplayer extends AppCompatActivity {
    private Server server;
    private Client client;
    private  String mode;
    private String ip;
    Button btnBack;

    public void onBackPressed() {
        startActivity(new Intent(this, LanMode.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_multiplayer);

        btnBack = findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanMultiplayer.this, LanMode.class));
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
            }
        });
        mode =  getIntent().getStringExtra("mode");
        ip =  getIntent().getStringExtra("ip");
        if(mode.equals("server")) {
            try {
                server = new Server(2);
                Thread threadServidor = new Thread(server);
                threadServidor.start();
//                threadServidor.join();
            } catch (IOException  e) {
                e.printStackTrace();
            }
        }else{
            try {
                client = new Client(ip);
                Thread threadClient = new Thread(client);
                threadClient.start();
//                threadClient.join();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Liguei-me ao servidor: "+ip, Toast.LENGTH_LONG).show();

        }

    }

}
