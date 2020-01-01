package com.example.sudoku.M3_MultiplayerLan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Data;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Grid;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Jogar;
import com.example.sudoku.M1_SinglePlayer.ViewSinglePlayer;
import com.example.sudoku.R;
import com.example.sudoku.Result;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LanMultiplayer extends AppCompatActivity {
        ViewMultiplayerLan viewServer , viewClient;
        Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
        Difficulty difficulty;
        ImageButton btnNotes, btnDelete;
        TextView tvErros, tvPoints, tvTime, tvPlayer;
        String type, ip;
        Server server;
        Client client;
        Player player;
        Data data;

        private int seconds;
        private int minutes;
        private int erros;
        private int points;

        public void onBackPressed() {
                startActivity(new Intent(this, LanMode.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);
                Gson gson = new Gson();
                erros = savedInstanceState.getInt("erros");
                seconds = savedInstanceState.getInt("seconds");
                minutes = savedInstanceState.getInt("minutes");
                points = savedInstanceState.getInt("points");
                viewServer.setGrid(gson.fromJson(savedInstanceState.getString("grid"), Grid.class));
                viewServer.setSelectedCell(gson.fromJson(savedInstanceState.getString("selectedCell"), Cell.class));

                if (minutes > 0) {
                        tvTime.setText(minutes + "m " + seconds + "s");
                } else {
                        tvTime.setText(seconds + "s");
                }
                tvErros.setText(erros + "/" + viewServer.getGrid().getDifficulty().getErros());
                tvPoints.setText("" + points);
                viewServer.invalidate();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                Gson gson = new Gson();
                outState.putInt("seconds", seconds);
                outState.putInt("minutes", minutes);
                outState.putInt("erros", erros);
                outState.putInt("points", points);
                outState.putString("grid", gson.toJson(viewServer.getGrid()));
                outState.putString("selectedCell", gson.toJson(viewServer.getSelectedCell()));
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_lan_multiplayer);
                difficulty = (Difficulty) getIntent().getSerializableExtra("Difficulty");
                type = getIntent().getStringExtra("type");
                tvPlayer = findViewById(R.id.tvPlayer);
                ip = getIntent().getStringExtra("ip");


                if (type.equals("server")) {
                        data = new Data(difficulty);
                        FrameLayout flSudoku = findViewById(R.id.flSudoku);
                        viewServer = new ViewMultiplayerLan(LanMultiplayer.this, data.getGrid());
                        flSudoku.addView(viewServer);
                        player = new Player("Servidor");
                        tvPlayer.setText(player.getName());
                        try {
                                server = new Server(1);
                                Thread threadServidor = new Thread(server);
                                threadServidor.start();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                } else {
                        try {
                                client = new Client(ip);
                                Thread threadClient = new Thread(client);
                                threadClient.start();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        //tvPlayer.setText(player.getName());
                        Toast.makeText(getApplicationContext(), "Liguei-me ao servidor: " + ip, Toast.LENGTH_LONG).show();
                }
                this.erros = 0;
                this.points = 0;

                n1 = findViewById(R.id.n1);
                n2 = findViewById(R.id.n2);
                n3 = findViewById(R.id.n3);
                n4 = findViewById(R.id.n4);
                n5 = findViewById(R.id.n5);
                n6 = findViewById(R.id.n6);
                n7 = findViewById(R.id.n7);
                n8 = findViewById(R.id.n8);
                n9 = findViewById(R.id.n9);
                tvTime = findViewById(R.id.tvTime);
                tvErros = findViewById(R.id.tvErrors);
                tvPoints = findViewById(R.id.tvPoints);


                btnDelete = findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        viewServer.clearSelectedCell();
                                        viewServer.getGrid().setCell(viewServer.getSelectedCell());
                                        viewServer.invalidate();
                                }
                        }
                });
                btnNotes = findViewById(R.id.btnNotes);
                btnNotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        if (!viewServer.isNotes()) {
                                                viewServer.enableNotes();
                                                viewServer.invalidate();
                                        } else {
                                                viewServer.disableNotes();
                                                viewServer.invalidate();
                                        }
                                }
                        }
                });

                setOnClick(n1, 1);
                setOnClick(n2, 2);
                setOnClick(n3, 3);
                setOnClick(n4, 4);
                setOnClick(n5, 5);
                setOnClick(n6, 6);
                setOnClick(n7, 7);
                setOnClick(n8, 8);
                setOnClick(n9, 9);


                Thread thread = new Thread() {
                        private static final int second = 1000;

                        public void run() {
                                while (true) {
                                        try {
                                                Thread.sleep(second);
                                                runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                                if (seconds < 59) {
                                                                        seconds++;
                                                                } else {
                                                                        seconds = 0;
                                                                        minutes++;
                                                                }
                                                                if (minutes > 60) {
                                                                        return;
                                                                }
                                                                if (minutes > 0) {
                                                                        tvTime.setText(minutes + "m " + seconds + "s");
                                                                } else {
                                                                        tvTime.setText(seconds + "s");
                                                                }
                                                        }
                                                });
                                        } catch (InterruptedException e) {
                                                e.printStackTrace();
                                        }
                                }
                        }
                };
                thread.start();
        }

        private void setOnClick(final Button btn, final int value) {
                btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        if (!viewServer.isNotes()) {
                                                viewServer.setValueSelectedCell(value);
                                                if (!viewServer.getGrid().isPossible(viewServer.getSelectedCell())) {
                                                        erros++;
                                                        points -= viewServer.getGrid().getDifficulty().getPoints() * 4;
                                                        tvErros.setText(erros + "/" + viewServer.getGrid().getDifficulty().getErros());
                                                } else {
                                                        points += viewServer.getGrid().getDifficulty().getPoints();
                                                }
                                        } else if (viewServer.getGrid().canNote(viewServer.getSelectedCell(), value)) {
                                                viewServer.addNoteSelectedCell(value);
                                        }
                                        viewServer.getGrid().setCell(viewServer.getSelectedCell());
                                        validate();
                                        tvPoints.setText("" + points);
                                        viewServer.invalidate();
                                }
                        }
                });
        }

        private void validate() {
                if (viewServer.getGrid().isColCompleted(viewServer.getSelectedCell())) {
                        points += viewServer.getGrid().getDifficulty().getPoints() * 2;
                }
                if (viewServer.getGrid().isRowCompleted(viewServer.getSelectedCell())) {
                        points += viewServer.getGrid().getDifficulty().getPoints() * 2;
                }
                if (viewServer.getGrid().isSquareCompleted(viewServer.getSelectedCell())) {
                        points += viewServer.getGrid().getDifficulty().getPoints() * 3;
                }
                if (erros >= viewServer.getGrid().getDifficulty().getErros()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Ohh, que pena!");
                        intent.putExtra("message", "Esgotou o limite de erros (" + viewServer.getGrid().getDifficulty().getErros() + ")!");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
                if (viewServer.getGrid().isCorrect()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Parabéns!");
                        intent.putExtra("message", "Conseguiu ganhar o jogo, somando " + points + " pontos em " + minutes + "m " + seconds + "s !");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
        }

        private void updateView(){
                runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                                viewServer.setGrid(data.getGrid());
                                tvErros.setText(erros + "/" + viewServer.getGrid().getDifficulty().getErros());
                                tvPoints.setText("" + points);

                        }
                });

        }


        private class Server implements Runnable {
                private ServerSocket serverSocket;
                private final int port = 2021;
                private final int portPlayers = 2022;

                private ArrayList<Socket> clientSockets;
                private int totalPlayers;
                private Gson gson;

                Server(int players) throws IOException {
                        this.totalPlayers = players;
                        this.clientSockets = new ArrayList<>();
                        this.serverSocket = new ServerSocket(port);
                        this.gson = new Gson();
                }

                @Override
                public void run() {
                        Thread threadSendData = new threadSendData();
                        try {
                                //serverSocket = new ServerSocket(portPlayers);
                                int total = 0;
                                while (total < totalPlayers) {
                                        System.out.println("----------------------------------------Estou a espera de todos os clientes: ("+total + "/"+totalPlayers+")");
                                        Socket clientSocket;
                                        clientSocket = serverSocket.accept();
                                        clientSockets.add(clientSocket);
                                        System.out.println("----------------------------------------Cliente Entrou!!");
                                        ObjectInputStream receivePlayer = new ObjectInputStream(clientSocket.getInputStream());
                                        Player player = (Player) receivePlayer.readObject();
                                        System.out.println("Recebi o objeto");
                                        data.addPlayer(player);
                                        total++;
                                }
                                System.out.println("Todos os clientes ligados! Começar a enviar a informação...");
                                updateView();
                                threadSendData.start();
                        } catch (IOException | ClassNotFoundException ex) {
                                ex.printStackTrace();
                        }
                }


                private class threadSendData extends Thread {
                        public void run() {
                                while (true) {
                                        try {
                                                for (Socket client : clientSockets) {
                                                        ObjectOutputStream objectOutputStream =  new ObjectOutputStream(client.getOutputStream());
                                                        objectOutputStream.writeObject(data);
                                                        objectOutputStream.flush();
                                                        System.out.println("Informação enviada ao: " + client.getLocalAddress());
                                                }
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                        }
                }
        }




        private class Client implements Runnable {
                private Socket socket;
                private final int serverPort = 2021;
                private final int serverPortWelcom = 2022;

                private Player player;
                private ObjectOutputStream objectOutputStream;
                private ObjectInputStream objectInputStream;
                private String serverAddress;
                private Gson gson;
                private Data data;


                public Client(String IP) throws IOException {
                        this.serverAddress = IP;
                        this.player = new Player("Andre");
                        this.data = null;
                        this.gson = new Gson();
                }


                @Override
                public void run() {
                        try {
                                socket = new Socket(serverAddress, serverPort);
                                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

//                        ObjectOutputStream sendPlayer = new ObjectOutputStream(socket.getOutputStream());
                                System.out.println("Vou enviar informaçao");
                                objectOutputStream.writeObject(player);
                                objectOutputStream.flush();
                                System.out.println("Informação enviada");
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        Thread receiveData = new  receiveData();
                        receiveData.start();

                }

                private class receiveData extends Thread {
                        public void run() {
                                while (true) {
                                        try {
                                                objectInputStream = new ObjectInputStream(socket.getInputStream());
                                                data = (Data) objectInputStream.readObject();
                                                System.out.println("Recebi a informação!");

                                                viewClient = new ViewMultiplayerLan(LanMultiplayer.this, data.getGrid());
                                                runOnUiThread(new Runnable() {

                                                        @Override
                                                        public void run() {
                                                                FrameLayout flSudoku = findViewById(R.id.flSudoku);
                                                                flSudoku.addView(viewClient);

                                                        }
                                                });

//                                                updateView();
                                        } catch (ClassNotFoundException | IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                        }
                }
        }


}