package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SinglePlayer extends AppCompatActivity {
    SudokuView sudokuView;
    Button n1, n2, n3, n4, n5, n6, n7, n8, n9, newGame;
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

        setOnClick(n1,1);
        setOnClick(n2,2);
        setOnClick(n3,3);
        setOnClick(n4,4);
        setOnClick(n5,5);
        setOnClick(n6,6);
        setOnClick(n7,7);
        setOnClick(n8,8);
        setOnClick(n9,9);
    }


    private void setOnClick(final Button btn, final int value){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sudokuView.selectedCell != null) {
                    sudokuView.selectedCell.setValue(value);
                    sudokuView.grid.setCell(sudokuView.selectedCell);
                    sudokuView.invalidate();
                }
            }
        });
    }

//    public void n1(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(1);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//
//    public void n2(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(2);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//
//    public void n3(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(3);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n4(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(4);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n5(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(5);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n6(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(6);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n7(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(7);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n8(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(8);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }
//    public void n9(View view) {
//        if (sudokuView.selectedCell != null) {
//            sudokuView.selectedCell.setValue(9);
//            sudokuView.grid.setCell(sudokuView.selectedCell);
//            sudokuView.invalidate();
//        }
//    }

}
