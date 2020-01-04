package com.example.sudoku.M3_MultiplayerLan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudoku.Core.Player;
import com.example.sudoku.DifficultyView;
import com.example.sudoku.Jogar;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class LanChoose extends AppCompatActivity {

        Button btnBack, btnServer, btnClient;
        Player player;

        @Override
        public void onBackPressed() {
                startActivity(new Intent(this, Jogar.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_lan_mode);
                player = MainActivity.player;
                btnBack = findViewById(R.id.btnBack);
                btnServer = findViewById(R.id.btnServer);
                btnClient = findViewById(R.id.btnClient);

                btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(LanChoose.this, Jogar.class));
                                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                finish();
                        }
                });

                btnServer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LanChoose.this);
                                View view = getLayoutInflater().inflate(R.layout.dialog_server, null);
                                final EditText editText = view.findViewById(R.id.tlTotalPlayers);
                                Button btnStart = view.findViewById(R.id.btnStart);
                                Button btnCancel = view.findViewById(R.id.btnCancel);
                                builder.setView(view);
                                final AlertDialog dialog = builder.create();
                                btnStart.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                String number = editText.getText().toString();
                                                if (!number.equals("")){
                                                        int totalPlayers = Integer.parseInt(number);
                                                        if (totalPlayers > 1 && totalPlayers < 4) {
                                                                Intent intent = new Intent(new Intent(LanChoose.this, DifficultyView.class));
                                                                intent.putExtra("mode", "M3");
                                                                intent.putExtra("type", "server");
                                                                intent.putExtra("totalPlayers", totalPlayers);
                                                                intent.putExtra("ip", getLocalIpAddress());
                                                                dialog.dismiss();
                                                                startActivity(intent);
                                                                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                                                finish();
                                                        }else{
                                                                Toast.makeText(getApplicationContext(), "Insira um valor entre 2 e 3!", Toast.LENGTH_LONG).show();
                                                        }
                                                }else{
                                                        Toast.makeText(getApplicationContext(), "Insira um valor entre 2 e 3!", Toast.LENGTH_LONG).show();
                                                }

                                        }
                                });
                                btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                dialog.dismiss();
                                        }
                                });
                                dialog.show();
                        }
                });

                btnClient.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LanChoose.this);
                                View view = getLayoutInflater().inflate(R.layout.dialog_client, null);
                                final EditText serverip = view.findViewById(R.id.serverIp);
                                Button btnConnect = view.findViewById(R.id.btnConnect);
                                Button btnBack = view.findViewById(R.id.btnBack);
                                builder.setView(view);
                                final AlertDialog dialog = builder.create();
                                btnConnect.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                final String ip;
                                                if (!serverip.getText().toString().isEmpty()) {
                                                        ip = serverip.getText().toString();
                                                        Thread validateIP = new Thread() {
                                                                public void run() {
                                                                        try {
                                                                                if (validarIP(ip)) {
                                                                                        Intent intent = new Intent(new Intent(LanChoose.this, LanClient.class));
                                                                                        intent.putExtra("type", "client");
                                                                                        intent.putExtra("ip", ip);
                                                                                        dialog.dismiss();
                                                                                        startActivity(intent);
                                                                                        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                                                                        finish();
                                                                                } else {
                                                                                        LanChoose.this.runOnUiThread(new Runnable() {
                                                                                                public void run() {
                                                                                                        Toast.makeText(getApplicationContext(), "Insira um endereço IP válido", Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                        });
                                                                                }
                                                                        } catch (IOException e) {
                                                                                LanChoose.this.runOnUiThread(new Runnable() {
                                                                                        public void run() {
                                                                                                Toast.makeText(getApplicationContext(), "Insira um endereço IP válido", Toast.LENGTH_LONG).show();
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
                                                } else {
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
                        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                                NetworkInterface intf = en.nextElement();
                                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
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
