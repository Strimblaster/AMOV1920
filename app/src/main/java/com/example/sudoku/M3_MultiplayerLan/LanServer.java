package com.example.sudoku.M3_MultiplayerLan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Data;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;
import com.example.sudoku.Historico;
import com.example.sudoku.M1_SinglePlayer.SinglePlayer;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class LanServer extends AppCompatActivity {

        private ViewLanServer viewServer;
        private Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
        private Difficulty difficulty;
        private ImageButton btnNotes, btnDelete;
        private TextView tvErros, tvPoints, tvTime, tvPlayer;
        private ImageView ivPlayer;
        private LinearLayout btnsLayout, optionsLayout;
        private Player player;

        private ServerSocket serverSocket;
        private ServerSocket receiveSocket;
        private final int port = 2021;
        private final int portReceive = 1920;
        private ArrayList<Socket> clientSockets;
        private int totalPlayers;
        private boolean leave;

        public void onBackPressed() {
                startActivity(new Intent(this, LanChoose.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }



        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_lan_server);
                FrameLayout flSudoku = findViewById(R.id.flSudoku);
                difficulty = (Difficulty) getIntent().getSerializableExtra("Difficulty");
                totalPlayers = getIntent().getIntExtra("totalPlayers", 1);
                viewServer = new ViewLanServer(this, difficulty);
                clientSockets = new ArrayList<>();
                player = MainActivity.player;
                flSudoku.addView(viewServer);
                leave = false;

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




                btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        viewServer.clearSelectedCell();
                                        viewServer.setCell(viewServer.getSelectedCell());
                                        viewServer.invalidate();
                                }
                        }
                });
                btnNotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        if (!viewServer.isNotes()) {
                                                viewServer.enableNotes();
                                        } else {
                                                viewServer.disableNotes();
                                        }
                                        viewServer.invalidate();
                                }
                        }
                });

                viewServer.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                if (viewServer.amIPlaying()) {
                                        if (event.getAction() == MotionEvent.ACTION_DOWN)
                                                return true;
                                        if (event.getAction() == MotionEvent.ACTION_UP) {
                                                int px = (int) event.getX();
                                                int py = (int) event.getY();
                                                viewServer.disableNotes();
                                                int w = viewServer.getWidth();
                                                int cellWidth = w / viewServer.getGridSize();
                                                int h = viewServer.getHeight();
                                                int cellHeight = h / viewServer.getGridSize();

                                                final int col = (px / cellWidth);
                                                final int row = (py / cellHeight);

                                                Cell temp = viewServer.getCell(row, col);
                                                if (!temp.isOriginal()) {
                                                        viewServer.setSelectedCell(temp);
                                                        viewServer.getSelectedCell().setCol(col);
                                                        viewServer.getSelectedCell().setRow(row);
                                                }
                                                Thread UpdateAndSend = new UpdateAndSend();
                                                UpdateAndSend.start();
                                                return true;
                                        }
                                }
                                return false;
                        }
                });


                Thread mainThread = new Thread(){
                        Thread Timer = new Timer();
                        Thread SendData = new SendData();
                        Thread ReceiveData = new ReceiveData();


                        @Override
                        public void run() {
                                super.run();
                                try {
                                        serverSocket = new ServerSocket(port);
                                        ReceiveData.start();
                                        int total = 0;


                                        while (total != totalPlayers) {
                                                Socket clientSocket;
                                                clientSocket = serverSocket.accept();
                                                clientSockets.add(clientSocket);
                                                ObjectInputStream receivePlayer = new ObjectInputStream(clientSocket.getInputStream());
                                                Player player = (Player) receivePlayer.readObject();
                                                viewServer.addPlayer(player);
                                                total++;
                                        }
                                        viewServer.setRandomPlayingPlayer();
                                        Timer.start();
                                        SendData.start();
                                } catch (IOException | ClassNotFoundException ex) {
                                        ex.printStackTrace();
                                }
                        }
                };
                mainThread.start();

                setOnClick(n1, 1);
                setOnClick(n2, 2);
                setOnClick(n3, 3);
                setOnClick(n4, 4);
                setOnClick(n5, 5);
                setOnClick(n6, 6);
                setOnClick(n7, 7);
                setOnClick(n8, 8);
                setOnClick(n9, 9);
        }

        private void validatePlay(){
                if (viewServer.getSelectedCell() != null) {
                        if (!viewServer.isPossible(viewServer.getSelectedCell())) {
                                viewServer.getSelectedCell().setBadValue();
                                viewServer.getPlayingPlayer().addErrors();
                                viewServer.getPlayingPlayer().removePoints(viewServer.getDifficultyPoints() * 4);
                                viewServer.setCell(viewServer.getSelectedCell());
                                Thread removeBads = new RemoveBads();
                                removeBads.start();
                        } else {
                                if (!viewServer.getSelectedCell().isEarnedPoints()) {
                                        viewServer.getPlayingPlayer().addPoints(viewServer.getDifficultyPoints());
                                        viewServer.getPlayingPlayer().addRightPlays();
                                        if (viewServer.isColCompleted(viewServer.getSelectedCell())) {
                                                viewServer.getPlayingPlayer().addPoints(viewServer.getDifficultyPoints() * 2);
                                        }
                                        if (viewServer.isRowCompleted(viewServer.getSelectedCell())) {
                                                viewServer.getPlayingPlayer().addPoints(viewServer.getDifficultyPoints() * 2);
                                        }
                                        if (viewServer.isSquareCompleted(viewServer.getSelectedCell())) {
                                                viewServer.getPlayingPlayer().addPoints(viewServer.getDifficultyPoints() * 3);
                                        }
                                        viewServer.getSelectedCell().setEarnedPoints();
                                }
                                viewServer.setCell(viewServer.getSelectedCell());
                        }
                }
        }

        private void setOnClick(final Button btn, final int value) {
                btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewServer.getSelectedCell() != null) {
                                        if (!viewServer.isNotes()) {
                                                viewServer.setValueSelectedCell(value);
                                                validatePlay();
                                        } else if (viewServer.canNote(viewServer.getSelectedCell(), value)) {
                                                viewServer.addNoteSelectedCell(value);
                                        }
                                        validate();
                                        Thread updateALL = new UpdateAndSend();
                                        updateALL.start();
                                }
                        }
                });
        }



        private void validate() {
                if (viewServer.getErrors()>= viewServer.getTotalErrors()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Ohh, que pena!");
                        intent.putExtra("message", "Esgotou o limite de erros (" + viewServer.getTotalErrors() + ")!");
                        Thread  updateALL = new UpdateAndSend();
                        updateALL.start();
                        try {
                                updateALL.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
                if (viewServer.isCorrect()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Parabéns!");
                        intent.putExtra("message", "Conseguiram completar o puzzle, o jogador"+viewServer.getWinnerPlayer().getName()+" ganhou, com um total de " + viewServer.getWinnerPlayer().getPoints() + " pontos com " + viewServer.getWinnerPlayer().getRightPlays() + " jogadas certas !");
                        Thread saveScore = new Thread(){
                                @Override
                                public void run() {
                                        super.run();
                                        Score score = new Score();
                                        score.setMode("M3");
                                        score.setTimeM1(0);
                                        score.setWinner(viewServer.getWinnerPlayer().getName());
                                        score.setRightPlaysM2M3(viewServer.getWinnerPlayer().getRightPlays());
                                        long id = MainActivity.appDatabase.score().insertScore(score);
                                        PlayerScoreJoin playerScoreJoin = new PlayerScoreJoin();
                                        playerScoreJoin.setPlayerID(MainActivity.player.getId());
                                        playerScoreJoin.setScoreID(id);
                                        MainActivity.appDatabase.conn().insert(playerScoreJoin);
                                }
                        };
                        saveScore.start();


                        viewServer.weHaveAWinner();
                        Thread  updateALL = new UpdateAndSend();
                        updateALL.start();
                        try {
                                updateALL.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
        }


        private class RemoveBads extends Thread {
                final Cell temp = viewServer.getSelectedCell();
                final Cell gCell = viewServer.getCell(temp.getRow(), temp.getCol());
                private static final int time = 3000;

                @Override
                public void run() {
                        super.run();
                        try {
                                Thread.sleep(time);
                                if (gCell.isBadValue()) {
                                        gCell.clear();
                                }
                                Thread  updateALL = new UpdateAndSend();
                                updateALL.start();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }

        private class ViewUpdater extends Thread{
                public void run() {
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        if (!viewServer.amIPlaying()){
                                                btnsLayout.setVisibility(View.INVISIBLE);
                                                optionsLayout.setVisibility(View.INVISIBLE);
                                        }else{
                                                btnsLayout.setVisibility(View.VISIBLE);
                                                optionsLayout.setVisibility(View.VISIBLE);
                                        }
                                        tvPlayer.setText(viewServer.getPlayingPlayer().getName());
                                        tvTime.setText(viewServer.getTime() + "s");
                                        tvErros.setText(viewServer.getPlayingPlayer().getErrors()+"/"+viewServer.getTotalErrors());
                                        tvPoints.setText(""+viewServer.getPlayingPlayer().getPoints());
                                        viewServer.invalidate();
                                }
                        });
                }
        }


        private class Timer extends Thread {
                private static final int second = 1000;
                public void run() {
                        while (true) {
                                try {
                                        Thread.sleep(second);
                                        runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                        if (viewServer.getTime() > 0){
                                                                viewServer.decrementTime();
                                                        }else{
                                                                if (!viewServer.getPlayingPlayer().hasMoreTime()){
                                                                        viewServer.nextPlayer();
                                                                }
                                                                viewServer.setTime(viewServer.getPlayingPlayer().getExtraTime());
                                                        }
                                                        tvTime.setText(viewServer.getTime() + "s");
                                                        tvErros.setText(""+viewServer.getPlayingPlayer().getErrors());
                                                        tvPoints.setText(""+viewServer.getPlayingPlayer().getPoints());
                                                        tvPlayer.setText(viewServer.getPlayingPlayer().getName());
                                                        viewServer.invalidate();
                                                        Thread updateClients = new UpdateAndSend();
                                                        updateClients.start();
                                                }
                                        });
                                } catch (InterruptedException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }

        private class ReceiveData extends Thread {
                public void run() {
                        try {
                                receiveSocket = new ServerSocket(portReceive);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                        while (true){
                                try {
                                        Socket socket = receiveSocket.accept();
                                        Data data = (Data) (new ObjectInputStream(socket.getInputStream())).readObject();
                                        if (data != null) {
                                                viewServer.setData(data);
                                                validatePlay();
                                                Thread updateAndSend = new UpdateAndSend();
                                                updateAndSend.start();
                                        }
                                } catch (IOException | ClassNotFoundException e) {
                                        e.printStackTrace();
                                        if (!leave) {
                                                Intent intent = new Intent(new Intent(LanServer.this, SinglePlayer.class));
                                                intent.putExtra("Difficulty", viewServer.getData().getGrid().getDifficulty());
                                                intent.putExtra("grid", viewServer.getData().getGrid());
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                                finish();
                                                leave = true;
                                        }
                                }
                        }
                }
        }


        private class SendData extends Thread {
                public void run() {
                        try {
                                for (Socket client : clientSockets) {
                                        try {
                                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                                                objectOutputStream.writeObject(viewServer.getData());
                                                objectOutputStream.flush();
                                        }catch (SocketException e){
                                                e.fillInStackTrace();
                                                if (!leave) {
                                                        Intent intent = new Intent(new Intent(LanServer.this, SinglePlayer.class));
                                                        intent.putExtra("Difficulty", viewServer.getData().getGrid().getDifficulty());
                                                        intent.putExtra("grid", viewServer.getData().getGrid());
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                                        finish();
                                                        leave = true;
                                                }
                                        }
                                }
                        } catch (IOException  e) {
                                e.printStackTrace();
                        }
                }
        }

        private class UpdateAndSend extends Thread {
                public void run() {
                        Thread  updateMe = new ViewUpdater();
                        updateMe.start();
                        Thread updateClients = new SendData();
                        updateClients.start();
                }
        }
}

