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
    Button n1, n2, n3, n4, n5, n6, n7, n8, n9, newGame;

    ImageButton btnNotes, btnDelete;


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
        sudokuView = new SudokuView(this);
        flSudoku.addView(sudokuView);
        n1 = findViewById(R.id.n1);
        n2 =  findViewById(R.id.n2);
        n3 =  findViewById(R.id.n3);
        n4 =  findViewById(R.id.n4);
        n5 =  findViewById(R.id.n5);
        n6 =  findViewById(R.id.n6);
        n7 =  findViewById(R.id.n7);
        n8 =  findViewById(R.id.n8);
        n9 =  findViewById(R.id.n9);
        newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuView.newGame();
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
            private int seconds;
            private int minutes;
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


    private void setOnClick(final Button btn, final int value){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sudokuView.selectedCell != null) {
                    if(!sudokuView.isNotes()) {
                        sudokuView.selectedCell.setValue(value);
                    }else if (sudokuView.grid.canNote(sudokuView.selectedCell,value)){
                        sudokuView.selectedCell.addNote(value);
                    }
                    sudokuView.grid.setCell(sudokuView.selectedCell);
                    sudokuView.invalidate();
                }
            }
        });
    }


}
