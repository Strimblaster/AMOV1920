package com.example.sudoku.M2_MultiPlayerLocal;

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
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Grid;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;
import com.example.sudoku.Jogar;
import com.example.sudoku.M1_SinglePlayer.SinglePlayer;
import com.example.sudoku.M3_MultiplayerLan.LanClient;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import com.example.sudoku.Result;
import com.google.gson.Gson;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Multiplayer extends AppCompatActivity {
        ViewMultiplayerLocal viewMultiplayerLocal;
        Button n1, n2, n3, n4, n5, n6, n7, n8, n9, btnM1;
        Difficulty difficulty;
        ImageButton btnNotes, btnDelete;
        TextView tvPoints, tvTime, tvPlayer1, tvPlayer2, tvErrors;

        private int seconds;
        private int errors;

        @Override
        public void onBackPressed() {
                startActivity(new Intent(this, Jogar.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                Gson gson = new Gson();
                outState.putInt("seconds", seconds);
                outState.putInt("errors", errors);
                outState.putString("grid", gson.toJson(viewMultiplayerLocal.getGrid()));
                outState.putString("selectedCell", gson.toJson(viewMultiplayerLocal.getSelectedCell()));
                outState.putString("player1", gson.toJson(viewMultiplayerLocal.getPlayer1()));
                outState.putString("player2", gson.toJson(viewMultiplayerLocal.getPlayer2()));
                outState.putString("activePlayer", gson.toJson(viewMultiplayerLocal.getActivePlayer()));
        }

        @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
                super.onRestoreInstanceState(savedInstanceState);
                Gson gson = new Gson();
                seconds = savedInstanceState.getInt("seconds");
                errors = savedInstanceState.getInt("errors");
                viewMultiplayerLocal.setGrid(gson.fromJson(savedInstanceState.getString("grid"), Grid.class));
                viewMultiplayerLocal.setSelectedCell(gson.fromJson(savedInstanceState.getString("selectedCell"), Cell.class));
                viewMultiplayerLocal.setPlayer1(gson.fromJson(savedInstanceState.getString("player1"), Player.class));
                viewMultiplayerLocal.setPlayer2(gson.fromJson(savedInstanceState.getString("player2"), Player.class));
                viewMultiplayerLocal.setActivePlayer(gson.fromJson(savedInstanceState.getString("activePlayer"), Player.class));

                tvTime.setText(seconds + "s");
                tvPoints.setText("" + viewMultiplayerLocal.getActivePlayer().getPoints());
                int maxErros = (int) (viewMultiplayerLocal.getGrid().getDifficulty().getErros() * 1.5);
                tvErrors.setText(errors + "/" + maxErros);
                if (viewMultiplayerLocal.getActivePlayer() != null) {
                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                        } else if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer2().getName())) {
                                tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                        } else {
                                Toast.makeText(this, "Erro na rotaçao!", Toast.LENGTH_LONG).show();
                        }
                } else {
                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                        viewMultiplayerLocal.setActivePlayer(viewMultiplayerLocal.getPlayer1());
                }
                viewMultiplayerLocal.invalidate();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_multiplayer);

                FrameLayout flSudoku = findViewById(R.id.flSudoku);
                difficulty = (Difficulty) getIntent().getSerializableExtra("Difficulty");
                viewMultiplayerLocal = new ViewMultiplayerLocal(this, difficulty);
                flSudoku.addView(viewMultiplayerLocal);
                seconds = 30;

                n1 = findViewById(R.id.n1);
                n2 = findViewById(R.id.n2);
                n3 = findViewById(R.id.n3);
                n4 = findViewById(R.id.n4);
                n5 = findViewById(R.id.n5);
                n6 = findViewById(R.id.n6);
                n7 = findViewById(R.id.n7);
                n8 = findViewById(R.id.n8);
                n9 = findViewById(R.id.n9);
                btnM1 = findViewById(R.id.btnM1);
                tvTime = findViewById(R.id.tvTime);
                tvPoints = findViewById(R.id.tvPoints);
                tvPlayer1 = findViewById(R.id.tvPlayer);
                tvPlayer2 = findViewById(R.id.tvPlayer2);
                tvErrors = findViewById(R.id.tvErrors);
                int maxErros = (int) (viewMultiplayerLocal.getGrid().getDifficulty().getErros() * 1.5);
                tvErrors.setText(errors + "/" + maxErros);
                tvPoints.setText("" + viewMultiplayerLocal.getActivePlayer().getPoints());

                if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                } else if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer2().getName())) {
                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                } else {
                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                        viewMultiplayerLocal.setActivePlayer(viewMultiplayerLocal.getPlayer1());
                }

                btnM1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(new Intent(Multiplayer.this, SinglePlayer.class));
                                intent.putExtra("Difficulty", viewMultiplayerLocal.getGrid().getDifficulty());
                                intent.putExtra("grid", viewMultiplayerLocal.getGrid());
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_right);
                                finish();
                        }
                });
                btnDelete = findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewMultiplayerLocal.getSelectedCell() != null) {
                                        viewMultiplayerLocal.clearSelectedCell();
                                        viewMultiplayerLocal.getGrid().setCell(viewMultiplayerLocal.getSelectedCell());
                                        viewMultiplayerLocal.invalidate();
                                }
                        }
                });
                btnNotes = findViewById(R.id.btnNotes);
                btnNotes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (viewMultiplayerLocal.getSelectedCell() != null) {
                                        if (!viewMultiplayerLocal.isNotes()) {
                                                viewMultiplayerLocal.enableNotes();
                                                viewMultiplayerLocal.invalidate();
                                        } else {
                                                viewMultiplayerLocal.disableNotes();
                                                viewMultiplayerLocal.invalidate();
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
                                                                if (seconds > 0) {
                                                                        seconds--;
                                                                } else {
                                                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                                if (!viewMultiplayerLocal.getPlayer1().hasMoreTime()) {
                                                                                        viewMultiplayerLocal.setActivePlayer(viewMultiplayerLocal.getPlayer2());
                                                                                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                                                                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                                                                                        seconds = 30;
                                                                                } else {
                                                                                        seconds = viewMultiplayerLocal.getPlayer1().getExtraTime();
                                                                                }
                                                                        } else {
                                                                                if (!viewMultiplayerLocal.getPlayer2().hasMoreTime()) {
                                                                                        viewMultiplayerLocal.setActivePlayer(viewMultiplayerLocal.getPlayer1());
                                                                                        tvPlayer1.setBackgroundColor(getResources().getColor(R.color.colorBlue));
                                                                                        tvPlayer2.setBackgroundColor(getResources().getColor(R.color.colorBackgroundLight));
                                                                                        seconds = 30;
                                                                                } else {
                                                                                        seconds = viewMultiplayerLocal.getPlayer2().getExtraTime();
                                                                                }
                                                                        }
                                                                }
                                                                tvPoints.setText("" + viewMultiplayerLocal.getActivePlayer().getPoints());
                                                                tvTime.setText(seconds + "s");
                                                                viewMultiplayerLocal.invalidate();
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
                                if (viewMultiplayerLocal.getSelectedCell() != null) {
                                        if (!viewMultiplayerLocal.isNotes()) {
                                                viewMultiplayerLocal.setValueSelectedCell(value);
                                                if (!viewMultiplayerLocal.getGrid().isPossible(viewMultiplayerLocal.getSelectedCell())) {
                                                        viewMultiplayerLocal.getSelectedCell().setBadValue();
                                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                viewMultiplayerLocal.getPlayer1().removePoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 4);
                                                        } else {
                                                                viewMultiplayerLocal.getPlayer2().removePoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 4);
                                                        }
                                                        errors++;
                                                        viewMultiplayerLocal.getGrid().setCell(viewMultiplayerLocal.getSelectedCell());
                                                        Thread removeBads = new removeBads();
                                                        removeBads.start();
                                                } else {
                                                        if (!viewMultiplayerLocal.getSelectedCell().isEarnedPoints()) {
                                                                if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                        viewMultiplayerLocal.getPlayer1().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints());
                                                                        viewMultiplayerLocal.getPlayer1().addExtraTime();
                                                                        viewMultiplayerLocal.getPlayer1().addRightPlays();
                                                                } else {
                                                                        viewMultiplayerLocal.getPlayer2().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints());
                                                                        viewMultiplayerLocal.getPlayer2().addExtraTime();
                                                                        viewMultiplayerLocal.getPlayer2().addRightPlays();
                                                                }
                                                                if (viewMultiplayerLocal.getGrid().isColCompleted(viewMultiplayerLocal.getSelectedCell())) {
                                                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                                viewMultiplayerLocal.getPlayer1().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 2);
                                                                        } else {
                                                                                viewMultiplayerLocal.getPlayer2().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 2);
                                                                        }
                                                                }
                                                                if (viewMultiplayerLocal.getGrid().isRowCompleted(viewMultiplayerLocal.getSelectedCell())) {
                                                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                                viewMultiplayerLocal.getPlayer1().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 2);
                                                                        } else {
                                                                                viewMultiplayerLocal.getPlayer2().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 2);
                                                                        }
                                                                }
                                                                if (viewMultiplayerLocal.getGrid().isSquareCompleted(viewMultiplayerLocal.getSelectedCell())) {
                                                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                                                viewMultiplayerLocal.getPlayer1().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 3);
                                                                        } else {
                                                                                viewMultiplayerLocal.getPlayer2().addPoints(viewMultiplayerLocal.getGrid().getDifficulty().getPoints() * 3);
                                                                        }
                                                                }
                                                                viewMultiplayerLocal.getSelectedCell().setEarnedPoints();
                                                        }
                                                        viewMultiplayerLocal.getGrid().setCell(viewMultiplayerLocal.getSelectedCell());
                                                }
                                        } else if (viewMultiplayerLocal.getGrid().canNote(viewMultiplayerLocal.getSelectedCell(), value)) {
                                                viewMultiplayerLocal.addNoteSelectedCell(value);
                                        }
                                        validate();
                                        if (viewMultiplayerLocal.getActivePlayer().getName().equals(viewMultiplayerLocal.getPlayer1().getName())) {
                                                tvPoints.setText("" + viewMultiplayerLocal.getPlayer1().getPoints());
                                        } else {
                                                tvPoints.setText("" + viewMultiplayerLocal.getPlayer2().getPoints());
                                        }
                                        int maxErros = (int) (viewMultiplayerLocal.getGrid().getDifficulty().getErros() * 1.5);
                                        tvErrors.setText(errors + "/" + maxErros);
                                        viewMultiplayerLocal.invalidate();
                                }
                        }
                });
        }


        private void validate() {
                int maxErros = (int) (viewMultiplayerLocal.getGrid().getDifficulty().getErros() * 1.5);
                if (errors >= maxErros) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Ohh, que pena!");
                        intent.putExtra("message", "A quantidade de erros chegou ao seu  limite(" + viewMultiplayerLocal.getGrid().getDifficulty().getErros() + ")!");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
                if (viewMultiplayerLocal.getGrid().isCorrect()) {
                        Intent intent = new Intent(this, Result.class);
                        intent.putExtra("title", "Parabéns!");
                        final Score score = new Score();
                        if (viewMultiplayerLocal.getPlayer1().getPoints() > viewMultiplayerLocal.getPlayer2().getPoints()) {
                                intent.putExtra("message", "Conseguiram completar o puzzle!  O" + viewMultiplayerLocal.getPlayer1().getName() + "foi o vencedor com " + viewMultiplayerLocal.getPlayer1().getPoints() + ", com mais " + (viewMultiplayerLocal.getPlayer1().getPoints() - viewMultiplayerLocal.getPlayer2().getPoints()) + " que o " + viewMultiplayerLocal.getPlayer2().getPoints());
                                score.setWinner(viewMultiplayerLocal.getPlayer1().getName());
                                score.setRightPlaysM2M3(viewMultiplayerLocal.getPlayer1().getRightPlays());
                        } else {
                                intent.putExtra("message", "Conseguiram completar o puzzle!  O" + viewMultiplayerLocal.getPlayer2().getName() + "foi o vencedor com " + viewMultiplayerLocal.getPlayer2().getPoints() + ", com mais " + (viewMultiplayerLocal.getPlayer2().getPoints() - viewMultiplayerLocal.getPlayer1().getPoints()) + " que o " + viewMultiplayerLocal.getPlayer1().getPoints());
                                score.setWinner(viewMultiplayerLocal.getPlayer2().getName());
                                score.setRightPlaysM2M3(viewMultiplayerLocal.getPlayer2().getRightPlays());
                        }
                        Thread saveScore = new Thread(){
                                @Override
                                public void run() {
                                        super.run();
                                        score.setMode("M2");
                                        score.setTimeM1(0);
                                        score.setWinner(MainActivity.player.getName());
                                        long id = MainActivity.appDatabase.score().insertScore(score);
                                        PlayerScoreJoin playerScoreJoin = new PlayerScoreJoin();
                                        playerScoreJoin.setPlayerID(MainActivity.player.getId());
                                        playerScoreJoin.setScoreID(id);
                                }
                        };
                        saveScore.start();


                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                        finish();
                }
        }

        private  class removeBads extends Thread{
                final Cell temp = viewMultiplayerLocal.getSelectedCell();
                final Cell gCell = viewMultiplayerLocal.getGrid().getCell(temp.getRow(),temp.getCol());
                private static final int time = 3000;
                @Override
                public void run() {
                        super.run();
                        try {
                                Thread.sleep(time);
                                if(gCell.isBadValue()) {
                                        gCell.clear();
                                }
                                viewMultiplayerLocal.invalidate();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                }
        }
}
