package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SinglePlayer extends AppCompatActivity {
    SudokuView sudokuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        FrameLayout flSudoku = findViewById(R.id.flSudoku);
        sudokuView = new SudokuView(this);
        flSudoku.addView(sudokuView);
    }

    public void n1(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(1);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }

    public void n2(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(2);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }

    public void n3(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(3);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n4(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(4);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n5(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(5);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n6(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(6);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n7(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(7);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n8(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(8);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }
    public void n9(View view) {
        if (sudokuView.selectedCell != null) {
            sudokuView.selectedCell.setValue(9);
            sudokuView.grid.setCell(sudokuView.selectedCell);
            sudokuView.selectedCell = null;
            sudokuView.invalidate();
        }
    }

}
