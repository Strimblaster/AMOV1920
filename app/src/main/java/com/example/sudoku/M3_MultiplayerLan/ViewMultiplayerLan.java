package com.example.sudoku.M3_MultiplayerLan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Grid;
import com.example.sudoku.Core.Player;
import com.example.sudoku.R;
import java.io.Serializable;

public class ViewMultiplayerLan extends View implements Serializable {
    private boolean notes;
    Grid grid;
    Paint paintMainLines, paintMainNumbers, paintSmallNumbers, paintOriginalNumbers;
    Paint strokePaint, errorPaint, selectedPaint, notesPaint,backgroundPaint, relatedPaint;
    Cell selectedCell;
    Player player1, player2, activePlayer;

    public ViewMultiplayerLan(Context context, Difficulty difficulty) {
        super(context);
        createPaints();
        selectedCell = null;
        notes = false;
        grid = new Grid(difficulty);
        player1 = new Player("Jogador 1");
        player2 = new Player("Jogador 2");
        setActivePlayer(player1);
    }

    public Grid getGrid() {
        return grid;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }
    public void clearSelectedCell(){
        this.selectedCell.clear();
    }
    public void setValueSelectedCell(int value){
        this.selectedCell.setValue(value);
    }
    public void addNoteSelectedCell(int note){
        this.selectedCell.addNote(note);
    }

    void createPaints() {
        paintMainLines = new Paint(Paint.DITHER_FLAG);
        paintMainLines.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMainLines.setColor(getResources().getColor(R.color.colorBlue));
        paintMainLines.setStrokeWidth(4);


        paintOriginalNumbers = new Paint();
        paintOriginalNumbers.setColor(getResources().getColor(R.color.colorBlue));
        paintOriginalNumbers.setTextSize(32f);
        paintOriginalNumbers.setTextAlign(Paint.Align.CENTER);

        paintMainNumbers = new Paint();
        paintMainNumbers.setColor(getResources().getColor(R.color.colorWhite));
        paintMainNumbers.setTextSize(32f);
        paintMainNumbers.setTextAlign(Paint.Align.CENTER);

        paintSmallNumbers = new Paint();
        paintSmallNumbers.setColor(getResources().getColor(R.color.colorWhite));
        paintSmallNumbers.setTextSize(10f);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(1f);
        strokePaint.setColor(getResources().getColor(R.color.colorWhite));

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setStyle(Paint.Style.FILL);
        selectedPaint.setColor(getResources().getColor(R.color.colorBackgroundLight));

        relatedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        relatedPaint.setStyle(Paint.Style.FILL);
        relatedPaint.setColor(getResources().getColor(R.color.colorBackgroundRelated));

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(getResources().getColor(R.color.colorBackground));

        notesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        notesPaint.setStyle(Paint.Style.FILL);
        notesPaint.setColor(getResources().getColor(R.color.colorOrange));



        errorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        errorPaint.setStyle(Paint.Style.FILL);
        errorPaint.setColor(getResources().getColor(R.color.colorRed));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cellWidth = (getWidth() / grid.getSize()) ;
        int cellHeight = (getHeight() / grid.getSize());

        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize() ; col++) {
                Cell c = grid.getCell(row, col);
                Rect rectangle = new Rect(col * cellWidth, row * cellHeight, (col + 1) * cellWidth, (row + 1) * cellHeight);
                canvas.drawRect(rectangle, strokePaint);
                if(selectedCell != null){
                    if((selectedCell.getCol() == c.getCol() || selectedCell.getRow() == c.getRow() || c.inSquare(selectedCell))){
                        canvas.drawRect(rectangle, relatedPaint);
                    } else {
                        canvas.drawRect(rectangle, backgroundPaint);
                    }
                }else {
                    canvas.drawRect(rectangle, backgroundPaint);
                }

                if (selectedCell != null) {
                    if(c.getValue() != 0 && !c.isOriginal() && !grid.isPossible(c)){
                        canvas.drawRect(rectangle, errorPaint);
                        final Cell temp = selectedCell;
                        Thread thread = new Thread(){
                            private static final int time = 3000;
                            public void run(){
                                try {
                                    Thread.sleep(time);
                                    if(!grid.isPossible(selectedCell)) {
                                        temp.clear();
                                    }
                                    invalidate();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    }else if (c == selectedCell && isNotes()) {
                        canvas.drawRect(rectangle, notesPaint);
                    } else if (c == selectedCell){
                        canvas.drawRect(rectangle, selectedPaint);
                    }
                }
            }
        }
        for (int i = 1; i < grid.getSize() ; i++) {
            if(i % 3 == 0){
                canvas.drawLine(i * cellWidth, 0, i*cellWidth, getHeight(), paintMainLines );
                canvas.drawLine(0, i * cellHeight, getWidth(), i* cellHeight,  paintMainLines);
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
                if (value != 0 ) {
                    int x = cellWidth / 2 + cellWidth * col; //fica no meio e anda de 1 em 1 cell
                    int y = cellHeight / 2 + cellHeight * row + cellHeight / 6;
                    if (cell.isOriginal()){
                        canvas.drawText("" + value, x, y, paintOriginalNumbers);
                    }else {
                        canvas.drawText("" + value, x, y, paintMainNumbers);
                    }
                } else {
                    int x = cellWidth / 6 + cellWidth* col;
                    int y = cellHeight / 6 + cellHeight* row;
                    for (int i = 1; i <= grid.getSize(); i++) {
                        if (cell.getNote(i)) {
                            int xp = x + (i - 1) % 3 * ((cellWidth -30) / 3);
                            int yp = (y + 9) + (i - 1) / 3 * ((cellHeight -30) / 3 + cellHeight / 9);
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
            disableNotes();
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

    public void  enableNotes(){
        this.notes = true;
    }
    public void  disableNotes(){
        this.notes = false;
    }
    public boolean isNotes(){
        return  this.notes;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public void setSelectedCell(Cell cell) {
        this.selectedCell = cell;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player player) {
        this.activePlayer = player;
    }
}