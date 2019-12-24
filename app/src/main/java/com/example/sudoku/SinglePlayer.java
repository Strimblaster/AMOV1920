package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.sql.Time;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SinglePlayer extends AppCompatActivity {
    SudokuView sudokuView;
    Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
    Difficulty difficulty;
    ImageButton btnNotes, btnDelete,newGame;
    TextView tvErros, tvPoints;

    private int seconds;
    private int minutes;
    private  int erros;
    private int points;


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Jogar.class));
        overridePendingTransition(R.anim.slide_left,R.anim.slide_out_right);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);
        FrameLayout flSudoku = findViewById(R.id.flSudoku);
        difficulty = (Difficulty) getIntent().getSerializableExtra("Difficulty");
        sudokuView = new SudokuView(this, difficulty);
        flSudoku.addView(sudokuView);
        this.erros = 0;
        this.points = 0;
        n1 = findViewById(R.id.n1);
        n2 =  findViewById(R.id.n2);
        n3 =  findViewById(R.id.n3);
        n4 =  findViewById(R.id.n4);
        n5 =  findViewById(R.id.n5);
        n6 =  findViewById(R.id.n6);
        n7 =  findViewById(R.id.n7);
        n8 =  findViewById(R.id.n8);
        n9 =  findViewById(R.id.n9);
        tvErros = findViewById(R.id.tvErrors);
        tvPoints = findViewById(R.id.tvPoints);
        newGame = findViewById(R.id.newGame);

        tvErros.setText(erros+"/"+sudokuView.grid.getDifficulty().getErros());
        tvPoints.setText(""+points);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuView.grid.resetGrid();
                resetValues();
                sudokuView.invalidate();
            }
        });
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sudokuView.selectedCell != null) {
                    sudokuView.selectedCell.clear();
                    sudokuView.grid.setCell(sudokuView.selectedCell);
                    sudokuView.invalidate();
                }
            }
        });
        btnNotes = findViewById(R.id.btnNotes);
        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sudokuView.selectedCell != null ) {
                    if (!sudokuView.isNotes()) {
                        sudokuView.enableNotes();
                        sudokuView.invalidate();
                    } else {
                        sudokuView.disableNotes();
                        sudokuView.invalidate();
                    }
                }
            }
        });

        setOnClick(n1,1);
        setOnClick(n2,2);
        setOnClick(n3,3);
        setOnClick(n4,4);
        setOnClick(n5,5);
        setOnClick(n6,6);
        setOnClick(n7,7);
        setOnClick(n8,8);
        setOnClick(n9,9);


        Thread thread = new Thread(){
            TextView tvTime = findViewById(R.id.tvTime);
            private static final int second = 1000;

            public void run(){
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
                                if(minutes > 0) {
                                    tvTime.setText(minutes + "m " + seconds+"s");
                                }else{
                                    tvTime.setText(seconds+"s");
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
        tvErros.setText(erros+"/"+sudokuView.grid.getDifficulty().getErros());
        tvPoints.setText(""+points);
    }


    private void setOnClick(final Button btn, final int value){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sudokuView.selectedCell != null) {
                    if(!sudokuView.isNotes()) {
                        sudokuView.selectedCell.setValue(value);
                        if (!sudokuView.grid.isPossible(sudokuView.selectedCell)){
                            erros++;
                            points -= sudokuView.grid.getDifficulty().getPoints() * 4;
                            tvErros.setText(erros+"/"+sudokuView.grid.getDifficulty().getErros());
                        }else{
                            points += sudokuView.grid.getDifficulty().getPoints();
                        }
                    }else if (sudokuView.grid.canNote(sudokuView.selectedCell,value)){
                        sudokuView.selectedCell.addNote(value);
                    }
                    sudokuView.grid.setCell(sudokuView.selectedCell);
                    validate();
                    tvPoints.setText(""+points);
                    sudokuView.invalidate();
                }
            }
        });
    }

    private void validate() {
        if (sudokuView.grid.isColCompleted(sudokuView.selectedCell)){
            points = sudokuView.grid.getDifficulty().getPoints() * 2;
        }
        if (sudokuView.grid.isRowCompleted(sudokuView.selectedCell)){
            points = sudokuView.grid.getDifficulty().getPoints() * 2;
        }
        if (sudokuView.grid.isSquareCompleted(sudokuView.selectedCell)){
            points = sudokuView.grid.getDifficulty().getPoints() * 3;
        }
        if(erros >= sudokuView.grid.getDifficulty().getErros()){
            Intent intent = new Intent(this,  Result.class);
            intent.putExtra("title","Ohh, que pena!");
            intent.putExtra("message","Esgotou o limite de erros ("+sudokuView.grid.getDifficulty().getErros()+")!");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
            finish();
        }
        if (sudokuView.grid.isCorrect()){
            Intent intent = new Intent(this,  Result.class);
            intent.putExtra("title","Parabéns!");
            intent.putExtra("message","Conseguiu ganhar o jogo, somando "+points+" pontos em "+minutes+"m "+seconds+"s !");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right,R.anim.slide_out_left);
            finish();
        }
    }


}
