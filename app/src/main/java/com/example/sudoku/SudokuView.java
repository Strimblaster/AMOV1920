package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuView extends View{

    Grid grid = new Grid(Difficulty.hard);

    public static final int BOARD_SIZE = 9;
    Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;
    Button n1, n2, n3, n4, n5, n6, n7, n8, n9;
    Cell selectedCell;

    public SudokuView(Context context) {
        super(context);
        createPaints();
        selectedCell = null;
        n1 = (Button) findViewById(R.id.n1);
        n2 = (Button)  findViewById(R.id.n2);
        n3 = (Button)  findViewById(R.id.n3);
        n4 = (Button)  findViewById(R.id.n4);
        n5 = (Button)  findViewById(R.id.n5);
        n6 = (Button)  findViewById(R.id.n6);
        n7 = (Button)  findViewById(R.id.n7);
        n8 = (Button)  findViewById(R.id.n8);
        n9 = (Button)  findViewById(R.id.n9);
    }

    void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(Color.rgb(236, 240, 241));
        paintMainLines.setStrokeWidth(8);

        paintSubLines = new Paint(paintMainLines);
        paintSubLines.setStrokeWidth(3);

        paintMainNumbers = new Paint(paintSubLines);
        paintMainNumbers.setColor(Color.rgb(52, 152, 219));
        paintMainNumbers.setTextSize(32);
        paintMainNumbers.setTextAlign(Paint.Align.CENTER);

        paintSmallNumbers = new Paint(paintMainNumbers);
        paintSmallNumbers.setTextSize(12);
        paintSmallNumbers.setStrokeWidth(2);
        paintSmallNumbers.setColor(Color.rgb(189, 195, 199));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth(), cellW = w / BOARD_SIZE;
        int h = getHeight(), cellH = h / BOARD_SIZE;

        for (int i = 0; i <= BOARD_SIZE; i++) {
            canvas.drawLine(0, i * cellH, w, i * cellH, i % 3 == 0 ? paintMainLines : paintSubLines);
            canvas.drawLine(i * cellW, 0, i * cellH, h, i % 3 == 0 ? paintMainLines : paintSubLines);
        }

        if (grid == null)
            return;

        paintMainNumbers.setTextSize(cellH / 2);
        paintSmallNumbers.setTextSize(cellH / 4);

        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                int n = grid.getValue(r, c);
                if (n != 0) {
                    int x = cellW / 2 + cellW * c; //fica no meio e anda de 1 em 1 cell
                    int y = cellH / 2 + cellH * r + cellH / 6;

                    canvas.drawText("" + n, x, y, paintMainNumbers);
                } else {
                    int x = cellW / 6 + cellW * c;
                    int y = cellH / 6 + cellH * r;
                    for (int i = 1; i <= grid.getSize(); i++) {
                        try {
                            if (grid.getCellHints(r, c).get(i)) {
                                int xp = x + (i - 1) % 3 * cellW / 3;
                                int yp = y + (i - 1) / 3 * cellH / 3 + cellH / 9;
                                canvas.drawText("" + i, xp, yp, paintSmallNumbers);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int px = (int) event.getX();
            int py = (int) event.getY();

            int w = getWidth(), cellW = w / BOARD_SIZE;
            int h = getHeight(), cellH = h / BOARD_SIZE;

            final int col = (px / cellW);
            final int row = (py / cellH);
            System.out.println("Row: " + row + " COL: " + col);
            if (grid.getCell(row, col).getValue() == 0) {
                selectedCell = grid.getCell(row, col);
                selectedCell.setX(col);
                selectedCell.setY(row);
            }

            return true;
        }

        return super.onTouchEvent(event);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.n1:
//                if (selectedCell != null) {
//                    selectedCell.setValue(1);
//                    grid.setCell(selectedCell);
//                    selectedCell = null;
//                    invalidate();
//                }
//                break;
//            default:
//                break;
//        }
//    }




}