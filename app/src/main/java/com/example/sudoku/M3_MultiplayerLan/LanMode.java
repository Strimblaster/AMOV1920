package com.example.sudoku.M3_MultiplayerLan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Trace;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.example.sudoku.Jogar;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LanMode extends AppCompatActivity {

    Button btnBack, btnServer, btnClient;
    InetAddress localhost;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Jogar.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lan_mode);

        btnBack = findViewById(R.id.btnBack);
        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);
        Thread thread = new Thread(){
            public void run() {
                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress("google.com", 80));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
                localhost = socket.getLocalAddress();
            }
        };
        thread.start();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanMode.this, Jogar.class));
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
            }
        });

        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),  localhost.getHostAddress(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(new Intent(LanMode.this, LanMultiplayer.class));
                intent.putExtra("mode", "server");
                intent.putExtra("ip", "nao preciso");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
            }
        });

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LanMode.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_client,null);
                final EditText serverip = view.findViewById(R.id.serverIp);
                Button btnConnect = view.findViewById(R.id.btnConnect);
                Button btnBack = view.findViewById(R.id.btnBack);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                btnConnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String ip;
                        if (!serverip.getText().toString().isEmpty()){
                            ip = serverip.getText().toString();
                            Thread validateIP = new Thread(){
                                public void run() {
                                    try {
                                        if (validarIP(ip)) {
                                            Intent intent = new Intent(new Intent(LanMode.this, LanMultiplayer.class));
                                            intent.putExtra("mode", "client");
                                            intent.putExtra("ip", ip);
                                            dialog.dismiss();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                            finish();
                                        }else{
                                            LanMode.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    Toast.makeText(getApplicationContext(), "Insira um endereço IP válido", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    } catch (IOException e) {
                                        LanMode.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Ocorreu um erro inesperado!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        e.printStackTrace();
                                    }
                                }
                            };
                            validateIP.start();
                            try {
                                validateIP.join();
                            } catch (InterruptedException e) {
                                Toast.makeText(getApplicationContext(), "Ocorreu um erro inesperado!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Insira um IP do servidor!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

    }

    private boolean validarIP(String ip) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(ip);
        return inetAddress.isReachable(5000);
    }
}
