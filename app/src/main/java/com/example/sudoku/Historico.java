package com.example.sudoku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sudoku.Core.Player;
import com.example.sudoku.Core.Score;

import java.util.List;

public class Historico extends AppCompatActivity {

        private RecyclerView recyclerView;
        private RecyclerView.Adapter adapter;
        private RecyclerView.LayoutManager layoutManager;
        private List<Score> scoreList;
        private Player player;

        Button btnBack;

        @Override
        public void onBackPressed() {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                finish();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_historico);

                player = MainActivity.player;
                Thread getHistory = new Thread(){
                        @Override
                        public void run() {
                                super.run();
                                scoreList = MainActivity.appDatabase.conn().getScoresForPlayer(player.getId());
                        }
                };
                getHistory.start();
                try {
                        getHistory.join();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }

                btnBack = findViewById(R.id.btnVoltar);
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new Adapter(scoreList);
                recyclerView.setAdapter(adapter);

                if (scoreList.size() == 0){
                        Toast.makeText(this," Ainda não existem dados, jogue primeiro!", Toast.LENGTH_LONG).show();
                }

                btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(Historico.this, MainActivity.class));
                                overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
                                finish();
                        }
                });
        }

        private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
                private List<Score> scoreList;

                class ViewHolder extends RecyclerView.ViewHolder {
                        View view;

                        ViewHolder(View view) {
                                super(view);
                                this.view = view;
                        }
                }


                Adapter(List<Score> scoreList) {
                        this.scoreList = scoreList;
                }

                @Override
                public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_view, parent, false);
                        ViewHolder viewHolder = new ViewHolder(view);
                        return viewHolder;
                }

                @Override
                public void onBindViewHolder(ViewHolder holder, int position) {
                        TextView tvPlayer, tvMode, tvInfo;
                        int info;

                        if (scoreList.get(position).getTimeM1() > 0) {
                                info = scoreList.get(position).getTimeM1();
                        } else {
                                info = scoreList.get(position).getRightPlaysM2M3();
                        }
                        tvPlayer = holder.view.findViewById(R.id.tvPlayerName);
                        tvMode = holder.view.findViewById(R.id.tvMode);
                        tvInfo = holder.view.findViewById(R.id.tvInfo);

                        tvPlayer.setText(scoreList.get(position).getWinner());
                        tvMode.setText(scoreList.get(position).getMode());
                        tvInfo.setText(""+info);
                }

                @Override
                public int getItemCount() {
                        return scoreList.size();
                }
        }
}
