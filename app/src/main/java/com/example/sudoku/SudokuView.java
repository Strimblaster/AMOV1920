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

public class SudokuView extends View  {

    int [][] board = {
            {0, 1, 0, 2, 0, 3, 0, 4, 0},
            {5, 0, 6, 0, 7, 0, 8, 0, 9},
            {0, 1, 0, 2, 0, 3, 0, 4, 0},
            {5, 0, 6, 0, 7, 0, 8, 0, 9},
            {0, 1, 0, 2, 0, 3, 0, 4, 0},
            {5, 0, 6, 0, 7, 0, 8, 0, 9},
            {0, 1, 0, 2, 0, 3, 0, 4, 0},
            {5, 0, 6, 0, 7, 0, 8, 0, 9},
            {0, 1, 0, 2, 0, 3, 0, 4, 0},
    };

    public static final int BOARD_SIZE = 9;
    Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers;
    Button n1, n2, n3, n4, n5, n6, n7, n8, n9;

    public SudokuView(Context context) {
        super(context);
        createPaints();
        n1 = (Button) findViewById(R.id.n1);
        n2 = (Button) findViewById(R.id.n2);
        n3 = (Button) findViewById(R.id.n3);
        n4 = (Button) findViewById(R.id.n4);
        n5 = (Button) findViewById(R.id.n5);
        n6 = (Button) findViewById(R.id.n6);
        n7 = (Button) findViewById(R.id.n7);
        n8 = (Button) findViewById(R.id.n8);
        n9 = (Button) findViewById(R.id.n9);
    }

    void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(Color.rgb(236,240,241));
        paintMainLines.setStrokeWidth(8);

        paintSubLines = new Paint(paintMainLines);
        paintSubLines.setStrokeWidth(3);

        paintMainNumbers = new Paint(paintSubLines);
        paintMainNumbers.setColor(Color.rgb(52,152,219));
        paintMainNumbers.setTextSize(32);
        paintMainNumbers.setTextAlign(Paint.Align.CENTER);

        paintSmallNumbers = new Paint(paintMainNumbers);
        paintSmallNumbers.setTextSize(12);
        paintSmallNumbers.setStrokeWidth(2);
        paintSmallNumbers.setColor(Color.rgb(189,195,199));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth(), cellW = w/BOARD_SIZE;
        int h = getHeight(), cellH = h/BOARD_SIZE;

        for(int i=0; i<=BOARD_SIZE; i++){
            canvas.drawLine(0, i*cellH, w, i*cellH, i%3==0 ? paintMainLines : paintSubLines);
            canvas.drawLine(i*cellW, 0, i*cellH, h, i%3==0 ? paintMainLines : paintSubLines);
        }

        if(board == null)
            return;

        paintMainNumbers.setTextSize(cellH / 2);
        paintSmallNumbers.setTextSize(cellH/ 4);

        for(int r=0; r < BOARD_SIZE; r++){
            for(int c=0; c < BOARD_SIZE; c++){
                int n = board[r][c];
                if(n != 0){
                    int x = cellW / 2 + cellW * c; //fica no meio e anda de 1 em 1 cell
                    int y = cellH / 2 + cellH * r + cellH / 6;

                    canvas.drawText(""+n, x, y, paintMainNumbers);
                }
                else{
                    List<Integer> possibilities = Arrays.asList(1,2,3,4,5,6,7,8,9);
                    Collections.shuffle(possibilities);
                    Random rnd = new Random(SystemClock.elapsedRealtime());
                    possibilities = possibilities.subList(0, rnd.nextInt(6)+1);

                    int x = cellW / 6 + cellW*c;
                    int y = cellH / 6 + cellH*r;
                    for(int p=1; p<=BOARD_SIZE; p++){
                        if(possibilities.contains(p)){
                            int xp = x+(p-1)%3 * cellW / 3;
                            int yp = y+(p-1)/3 * cellH / 3 + cellH / 9;
                            canvas.drawText(""+p, xp, yp, paintSmallNumbers);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
            return true;
        if(event.getAction() == MotionEvent.ACTION_UP){
            int px = (int) event.getX();
            int py = (int) event.getY();

            int w = getWidth(), cellW = w/BOARD_SIZE;
            int h = getHeight(), cellH = h/BOARD_SIZE;

            final int cellX = px / cellW;
            final int cellY = py / cellH;

            board[cellY][cellX] = 5;

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void setBoard(int [][] board){
        this.board = board;
        invalidate();
    }



}
