package com.example.sudoku.M1_SinglePlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Grid;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;
import com.example.sudoku.Jogar;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;
import com.google.gson.Gson;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SinglePlayer extends AppCompatActivity {
        ViewSinglePlayer viewSinglePlayer;
        Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
        Difficulty difficulty;
        ImageButton btnNotes, btnDelete, newGame;
        TextView tvErros, tvPoints, tvTime;

        private int seconds;
        private int minutes;
        private int erros;
        private int points;

        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                Gson gson = new Gson();
                outState.putInt("seconds", seconds);
                outState.putInt("minutes", minutes);
                outState.putInt("erros", erros);
                outState.putInt("points", points);
                outState.putString("grid", gson.toJson(viewSinglePlayer.getGrid()));
                outState.putString("selectedCell", gson.toJson(viewSinglePlayer.getSelectedCell()));
        }

        @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);
                Gson gson = new Gson();
                erros = savedInstanceState.getInt("erros");
                seconds = savedInstanceState.getInt("seconds");
                minutes = savedInstanceState.getInt("minutes");
                points = savedInstanceState.getInt("points");
                viewSinglePlayer.setGrid(gson.fromJson(savedInstanceState.getString("grid"), Grid.class));
                viewSinglePlayer.setSelectedCell(gson.fromJson(savedInstanceState.getString("selectedCell"), Cell.class));

                if (minutes > 0) {
                        tvTime.setText(minutes + "m " + seconds + "s");
                } else {
                        tvTime.setText(seconds + "s");
                }
                tvErros.setText(erros + "/" + viewSinglePlayer.getGrid().getDifficulty().getErros());
                tvPoints.setText("" + points);
                viewSinglePlayer.invalidate();
        }

        @Override
        public void onBackPressed() {
                startActivity(new Intent(this, Jogar.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_single_player);
                FrameLayout flSudoku = findViewById(R.id.flSudoku);
                difficulty = (Difficulty) getIntent().getSerializableExtra("Difficulty");
                viewSinglePlayer = new ViewSinglePlayer(this, difficulty);
                flSudoku.addView(viewSinglePlayer);

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
                newGame = findViewById(R.id.newGame);

                tvErros.setText(erros + "/" + viewSinglePlayer.getGrid().getDifficulty().getErros());
                tvPoints.setText("" + points);

                newGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                viewSinglePlayer.getGrid().resetGrid();
                                resetValues();
                                viewSinglePlayer.invalidate();
                        }
                });
                btnDelete = findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewSinglePlayer.getSelectedCell() != null) {
                                        viewSinglePlayer.clearSelectedCell();
                                        viewSinglePlayer.getGrid().setCell(viewSinglePlayer.getSelectedCell());
                                        viewSinglePlayer.invalidate();
                                }
                        }
                });
                btnNotes = findViewById(R.id.btnNotes);
                btnNotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewSinglePlayer.getSelectedCell() != null) {
                                        if (!viewSinglePlayer.isNotes()) {
                                                viewSinglePlayer.enableNotes();
                                        } else {
                                                viewSinglePlayer.disableNotes();
                                        }
                                        viewSinglePlayer.invalidate();
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

        private void resetValues() {
                this.erros = 0;
                this.minutes = 0;
                this.seconds = 0;
                this.points = 0;
                viewSinglePlayer.getGrid().clearEarnedPoints();
                tvErros.setText(erros + "/" + viewSinglePlayer.getGrid().getDifficulty().getErros());
                tvPoints.setText("" + points);
        }


        private void setOnClick(final Button btn, final int value) {
                btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewSinglePlayer.getSelectedCell() != null) {
                                        if (!viewSinglePlayer.isNotes()) {
                                                viewSinglePlayer.setValueSelectedCell(value);
                                                viewSinglePlayer.getSelectedCell().removeBadValue();
                                                if (!viewSinglePlayer.getGrid().isPossible(viewSinglePlayer.getSelectedCell())) {
                                                        viewSinglePlayer.getSelectedCell().setBadValue();
                                                        erros++;
                                                        points -= viewSinglePlayer.getGrid().getDifficulty().getPoints() * 4;
                                                        tvErros.setText(erros + "/" + viewSinglePlayer.getGrid().getDifficulty().getErros());
                                                        viewSinglePlayer.getGrid().setCell(viewSinglePlayer.getSelectedCell());
                                                        Thread removeBads = new removeBads();
                                                        removeBads.start();
                                                } else {
                                                        if (!viewSinglePlayer.getSelectedCell().isEarnedPoints()) {
                                                                points += viewSinglePlayer.getGrid().getDifficulty().getPoints();
                                                                if (viewSinglePlayer.getGrid().isColCompleted(viewSinglePlayer.getSelectedCell())) {
                                                                        points += viewSinglePlayer.getGrid().getDifficulty().getPoints() * 2;
                                                                }
                                                                if (viewSinglePlayer.getGrid().isRowCompleted(viewSinglePlayer.getSelectedCell())) {
                                                                        points += viewSinglePlayer.getGrid().getDifficulty().getPoints() * 2;
                                                                }
                                                                if (viewSinglePlayer.getGrid().isSquareCompleted(viewSinglePlayer.getSelectedCell())) {
                                                                        points += viewSinglePlayer.getGrid().getDifficulty().getPoints() * 3;
                                                                }
                                                                viewSinglePlayer.selectedCell.setEarnedPoints();
                                                        }
                                                        viewSinglePlayer.getGrid().setCell(viewSinglePlayer.getSelectedCell());
                                                }
                                        } else if (viewSinglePlayer.getGrid().canNote(viewSinglePlayer.getSelectedCell(), value)) {
                                                viewSinglePlayer.addNoteSelectedCell(value);
                                        }
                                        validate();
                                        tvPoints.setText("" + points);
                                        viewSinglePlayer.invalidate();
                                }
                        }
                });
        }

        private void validate() {
                if (erros >= viewSinglePlayer.getGrid().getDifficulty().getErros()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Ohh, que pena!");
                        intent.putExtra("message", "Esgotou o limite de erros (" + viewSinglePlayer.getGrid().getDifficulty().getErros() + ")!");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
                if (viewSinglePlayer.getGrid().isCorrect()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Parabéns!");
                        intent.putExtra("message", "Conseguiu ganhar o jogo, somando " + points + " pontos em " + minutes + "m " + seconds + "s !");
                        Thread saveScore = new Thread(){
                                @Override
                                public void run() {
                                        super.run();
                                        Score score = new Score();
                                        score.setMode("M1");
                                        score.setTimeM1((minutes * 60)+ seconds);
                                        score.setWinner(MainActivity.player.getName());
                                        score.setRightPlaysM2M3(0);
                                        long id = MainActivity.appDatabase.score().insertScore(score);
                                        PlayerScoreJoin playerScoreJoin = new PlayerScoreJoin();
                                        playerScoreJoin.setPlayerID(MainActivity.player.getId());
                                        playerScoreJoin.setScoreID(id);
                                }
                        };
                        saveScore.start();
                        try {
                                saveScore.join();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
        }

        private  class removeBads extends Thread{
                final Cell temp = viewSinglePlayer.getSelectedCell();
                final Cell gCell = viewSinglePlayer.getGrid().getCell(temp.getRow(),temp.getCol());
                private static final int time = 3000;
                @Override
                public void run() {
                        super.run();
                        try {
                                Thread.sleep(time);
                                if(gCell.isBadValue()) {
                                        gCell.clear();
                                }
                                viewSinglePlayer.invalidate();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }

}
