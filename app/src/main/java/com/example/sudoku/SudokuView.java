package com.example.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
    Paint paintMainLines, paintSubLines, paintMainNumbers, paintSmallNumbers, paintOriginalNumbers, paintPossibles;
    Cell selectedCell;
    private boolean hint;

    public SudokuView(Context context) {
        super(context);
        createPaints();
        selectedCell = null;
        hint = false;
    }

    void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(Color.rgb(236, 240, 241));
        paintMainLines.setStrokeWidth(8);

        paintSubLines = new Paint(paintMainLines);
        paintSubLines.setStrokeWidth(3);

        paintOriginalNumbers = new Paint(paintSubLines);
        paintOriginalNumbers.setColor(Color.rgb(52, 152, 219));
        paintOriginalNumbers.setTextSize(32);
        paintOriginalNumbers.setTextAlign(Paint.Align.CENTER);

        paintMainNumbers = new Paint(paintSubLines);
        paintMainNumbers.setColor(Color.rgb(236, 240, 241));
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
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint errorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int cellWidth = getWidth() / grid.getSize();
        int cellHeight = getHeight() / grid.getSize();

        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize() ; col++) {
                Cell c = grid.getCell(row,col);
                Rect rectangle = new Rect(col*cellWidth, row*cellHeight, (col+1)*cellWidth,  (row+1)*cellHeight);
                strokePaint.setStyle(Paint.Style.STROKE);
                strokePaint.setStrokeWidth(1f);
                strokePaint.setColor(Color.WHITE);
                canvas.drawRect(rectangle,strokePaint);
                if(c.getValue() != 0 && !c.isOriginal() && !grid.isPossible(c)){
                    errorPaint.setStyle(Paint.Style.FILL);
                    errorPaint.setColor(Color.rgb(231, 76, 60));
                    canvas.drawRect(rectangle, errorPaint);
                }else{
                    normalPaint.setStyle(Paint.Style.FILL);
                    normalPaint.setColor(Color.rgb(51, 51, 51));
                    canvas.drawRect(rectangle, normalPaint);
                }
                if(selectedCell != null) {
                    if (c == selectedCell) {
                        fillPaint.setStyle(Paint.Style.FILL);
                        fillPaint.setColor(Color.rgb(91, 91, 91));
                        canvas.drawRect(rectangle, fillPaint);
                    }
                }
            }
        }

        if (grid == null)
            return;

        paintMainNumbers.setTextSize(cellHeight / 2);
        paintOriginalNumbers.setTextSize(cellHeight / 2);
        paintSmallNumbers.setTextSize(cellHeight / 4);

        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize(); col++) {
                Cell cell = grid.getCell(row,col);
                int value = cell.getValue();
                if (value != 0) {
                    int x = cellWidth / 2 + cellWidth * col; //fica no meio e anda de 1 em 1 cell
                    int y = cellHeight / 2 + cellHeight * row + cellHeight / 6;
                    if (cell.isOriginal()){
                        canvas.drawText("" + value, x, y, paintOriginalNumbers);
                    }else {
                        canvas.drawText("" + value, x, y, paintMainNumbers);
                    }
                } else {
                    int x = cellWidth / 6 + cellWidth * col;
                    int y = cellHeight / 6 + cellHeight * row;
                    for (int i = 1; i <= grid.getSize(); i++) {
                        if (cell.getHint(i)) {
                            int xp = x + (i - 1) % 3 * cellWidth / 3;
                            int yp = y + (i - 1) / 3 * cellHeight / 3 + cellHeight / 9;
                            canvas.drawText("" + i, xp, yp, paintSmallNumbers);
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

            int w = getWidth();
            int cellWidth = w / grid.getSize();
            int h = getHeight();
            int cellHeight = h / grid.getSize();

            final int col = (px / cellWidth);
            final int row = (py / cellHeight);

            Cell temp = grid.getCell(row, col);
            if (!temp.isOriginal()) {
                selectedCell = temp;
                selectedCell.setCol(col);
                selectedCell.setRow(row);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    void newGame(){
        this.grid = new Grid(Difficulty.random);
        invalidate();
    }
}