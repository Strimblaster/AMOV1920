package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sudoku.Core.AppDatabase;
import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.PlayerScoreJoin;
import com.example.sudoku.Core.Score;

import java.util.List;


public class MainActivity extends AppCompatActivity {
        public static AppDatabase appDatabase;
        private Button btnJogar, btnProfile, btnHistorico;
        public static Player player;

        @Override
        public void onBackPressed() {
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database").build();
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main_menu);

                btnJogar = findViewById(R.id.btnJogar);
                btnProfile = findViewById(R.id.btnProfile);
                btnHistorico = findViewById(R.id.btnHistorico);

                Thread thread = new Thread() {
                        @Override
                        public void run() {
                                List<Player> players = MainActivity.appDatabase.player().getPlayer();
                                if (players.isEmpty()) {
                                        Intent intent = new Intent(MainActivity.this, Profile.class);
                                        startActivity(intent);
                                        finish();
                                }else {
                                        player = players.get(0);
                                        Score score = new Score();
                                        score.setMode("M1");
                                        score.setTimeM1(130);
                                        score.setWinner(player.getName());
                                        score.setRightPlaysM2M3(0);
                                        appDatabase.conn().deleteAll();
                                        appDatabase.score().deleteAll();
                                        long id = appDatabase.score().insertScore(score);
                                        PlayerScoreJoin playerScoreJoin = new PlayerScoreJoin();
                                        playerScoreJoin.setPlayerID(player.getId());
                                        playerScoreJoin.setScoreID(id);
                                        appDatabase.conn().insert(playerScoreJoin);
                                        List<Score> scores = appDatabase.conn().getScoresForPlayer(player.getId());

                                        for (Score s : scores){
                                                System.out.println(s.getMode()+" "+s.getWinner()+" "+s.getTimeM1());
                                        }
                                }
                        }
                };
                thread.start();

                btnJogar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Jogar.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                                finish();
                        }
                });

                btnProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Profile.class);
                                startActivity(intent);
                                finish();
                        }
                });
                btnHistorico.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, Historico.class);
                                startActivity(intent);
                                finish();
                        }
                });

        }
}
