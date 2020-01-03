package com.example.sudoku.M3_MultiplayerLan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Data;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LanClient extends AppCompatActivity {

        ViewLanClient viewClient;
        Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
        ImageButton btnNotes, btnDelete;
        TextView tvErros, tvPoints, tvTime, tvPlayer;
        ImageView ivPlayer;
        LinearLayout btnsLayout, optionsLayout;
        String ServerIP;

        private Socket socket;
        private final int port = 2021;
        private final int portReceive = 1920;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;

        public void onBackPressed() {
                startActivity(new Intent(this, LanChoose.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_lan_client);
                FrameLayout flSudoku = findViewById(R.id.flSudoku);
                ServerIP = getIntent().getStringExtra("ip");
                viewClient = new ViewLanClient(this);
                flSudoku.addView(viewClient);
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
                tvPlayer = findViewById(R.id.tvPlayer);
                ivPlayer = findViewById(R.id.ivPlayer);
                btnNotes = findViewById(R.id.btnNotes);
                btnDelete = findViewById(R.id.btnDelete);
                btnsLayout = findViewById(R.id.Numbers);
                optionsLayout = findViewById(R.id.optionsLayout);

                viewClient.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                if (viewClient.amIPlaying()) {
                                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                                                return true;
                                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                                int px = (int) event.getX();
                                                int py = (int) event.getY();
                                                viewClient.disableNotes();
                                                int w = viewClient.getWidth();
                                                int cellWidth = w / viewClient.getGridSize();
                                                int h = viewClient.getHeight();
                                                int cellHeight = h / viewClient.getGridSize();

                                                final int col = (px / cellWidth);
                                                final int row = (py / cellHeight);

                                                Cell temp = viewClient.getCell(row, col);
                                                if (!temp.isOriginal()) {
                                                        viewClient.setSelectedCell(temp);
                                                        viewClient.getSelectedCell().setCol(col);
                                                        viewClient.getSelectedCell().setRow(row);
                                                }
                                                Thread UpdateAndSend = new UpdateAndSend();
                                                UpdateAndSend.start();
                                                return true;
                                        }
                                }
                                return false;
                        }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewClient.getSelectedCell() != null) {
                                        viewClient.clearSelectedCell();
                                        viewClient.setCell(viewClient.getSelectedCell());
                                        Thread UpdateAndSend = new UpdateAndSend();
                                        UpdateAndSend.start();
                                }
                        }
                });
                btnNotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewClient.getSelectedCell() != null) {
                                        if (!viewClient.isNotes()) {
                                                viewClient.enableNotes();
                                        } else {
                                                viewClient.disableNotes();
                                        }
                                        Thread UpdateAndSend = new UpdateAndSend();
                                        UpdateAndSend.start();
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


                Thread mainThread = new Thread(){
                        Thread ReceiveData = new ReceiveData();
                        @Override
                        public void run() {
                                super.run();
                                try {
                                        socket = new Socket(ServerIP, port);
                                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                        objectOutputStream.writeObject(viewClient.getPlayer());
                                        objectOutputStream.flush();
                                        ReceiveData.start();
                                } catch (IOException ex) {
                                        ex.printStackTrace();
                                }
                        }
                };
                mainThread.start();
        }



        private void setOnClick(final Button btn, final int value) {
                btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewClient.getSelectedCell() != null) {
                                        if (!viewClient.isNotes()) {
                                                viewClient.setValueSelectedCell(value);
                                        } else if (viewClient.canNote(viewClient.getSelectedCell(), value)) {
                                                viewClient.addNoteSelectedCell(value);
                                        }
                                        Thread UpdateAndSend = new UpdateAndSend();
                                        UpdateAndSend.start();
                                }
                        }
                });
        }

        private void validate() {
                if (viewClient.getErrors() >= viewClient.getTotalErrors()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Ohh, que pena!");
                        intent.putExtra("message", "Esgotaram o limite de erros (" + viewClient.getTotalErrors() + ")!");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
                if (viewClient.doWeHaveAWinner()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Parabéns!");
                        intent.putExtra("message", "Conseguiram completar o puzzle, o jogador"+viewClient.getWinnerPlayer().getName()+" ganhou, com um total de " + viewClient.getWinnerPlayer().getPoints() + " pontos com " + viewClient.getWinnerPlayer().getRightPlays() + " jogadas certas !");
                        Thread saveScore = new Thread(){
                                @Override
                                public void run() {
                                        super.run();
                                        Score score = new Score();
                                        score.setMode("M3");
                                        score.setTimeM1(0);
                                        score.setWinner(viewClient.getWinnerPlayer().getName());
                                        score.setRightPlaysM2M3(viewClient.getWinnerPlayer().getRightPlays());
                                        long id = MainActivity.appDatabase.score().insertScore(score);
                                        PlayerScoreJoin playerScoreJoin = new PlayerScoreJoin();
                                        playerScoreJoin.setPlayerID(MainActivity.player.getId());
                                        playerScoreJoin.setScoreID(id);
                                        MainActivity.appDatabase.conn().insert(playerScoreJoin);
                                }
                        };
                        saveScore.start();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
        }

        private class UpdateAndSend extends Thread {
                public void run() {
                        Thread  updateMe = new ViewUpdater();
                        updateMe.start();
                        Thread sendToServer = new SendData();
                        sendToServer.start();
                }
        }

        private class ViewUpdater extends Thread{
                public void run() {
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        if (!viewClient.amIPlaying()){
                                                btnsLayout.setVisibility(View.INVISIBLE);
                                                optionsLayout.setVisibility(View.INVISIBLE);
                                        }else{
                                                btnsLayout.setVisibility(View.VISIBLE);
                                                optionsLayout.setVisibility(View.VISIBLE);
                                        }
                                        tvPlayer.setText(viewClient.getPlayingPlayer().getName());
                                        tvTime.setText(viewClient.getTime() + "s");
                                        tvErros.setText(viewClient.getPlayingPlayer().getErrors()+"/"+viewClient.getTotalErrors());
                                        tvPoints.setText(""+viewClient.getPlayingPlayer().getPoints());
                                        viewClient.invalidate();
                                }
                        });
                }
        }

        private class ReceiveData extends Thread {
                public void run() {
                        while (true) {
                                try {
                                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                                        Data data = (Data) objectInputStream.readObject();
                                        viewClient.setData(data);
                                        Thread updateMe = new ViewUpdater();
                                        updateMe.start();
                                        validate();
                                } catch (ClassNotFoundException | IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }
        private class SendData extends Thread {
                public void run() {
                        try {
                                Socket socket = new Socket(ServerIP, portReceive);
                                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                                objectOutputStream.writeObject(viewClient.getData());
                                objectOutputStream.flush();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }
}

