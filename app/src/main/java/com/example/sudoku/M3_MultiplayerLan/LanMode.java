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

import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Player;
import com.example.sudoku.DifficultyView;
import com.example.sudoku.Jogar;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class LanMode extends AppCompatActivity {

    Button btnBack, btnServer, btnClient;
    Player player;

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
//        player = (Player) getIntent().getSerializableExtra("player");
        btnBack = findViewById(R.id.btnBack);
        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);

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
                Toast.makeText(getApplicationContext(), "IP: "+ getLocalIpAddress(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(new Intent(LanMode.this, DifficultyView.class));
                player = new Player("Server");
                intent.putExtra("mode", "M3");
                intent.putExtra("type", "server");
                intent.putExtra("player", player);
                intent.putExtra("ip", getLocalIpAddress());

                startActivity(intent);
                overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
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
                                            player = new Player("Cliente");
                                            intent.putExtra("mode", "M3");
                                            intent.putExtra("player", player);
                                            intent.putExtra("type", "client");
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

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
