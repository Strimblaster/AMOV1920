package com.example.sudoku.M3_MultiplayerLan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import com.example.sudoku.Core.Cell;
import com.example.sudoku.Core.Data;
import com.example.sudoku.Core.Difficulty;
import com.example.sudoku.Core.Player;
import com.example.sudoku.MainActivity;
import com.example.sudoku.R;
import java.io.Serializable;


public class ViewLanServer extends View implements Serializable {
        Paint paintMainLines, paintMainNumbers, paintSmallNumbers, paintOriginalNumbers;
        Paint strokePaint, errorPaint, selectedPaint, notesPaint, backgroundPaint, relatedPaint;
        private Data data;
        private Player player;

        public ViewLanServer(Context context, Difficulty difficulty) {
                super(context);
                createPaints();
                this.data = new Data(difficulty);
                this.player = MainActivity.player;
                data.addPlayer(player);
        }


        @Override
        protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                int cellWidth = (getWidth() / data.getGridSize());
                int cellHeight = (getHeight() / data.getGridSize());

                for (int row = 0; row < data.getGridSize(); row++) {
                        for (int col = 0; col < data.getGridSize(); col++) {
                                Cell c = data.getCell(row, col);
                                Rect rectangle = new Rect(col * cellWidth, row * cellHeight, (col + 1) * cellWidth, (row + 1) * cellHeight);
                                canvas.drawRect(rectangle, strokePaint);
                                if (data.getSelectedCell() != null) {
                                        if ((data.getSelectedCell().getCol() == c.getCol() || data.getSelectedCell().getRow() == c.getRow() || c.inSquare(data.getSelectedCell()))) {
                                                canvas.drawRect(rectangle, relatedPaint);
                                        } else {
                                                canvas.drawRect(rectangle, backgroundPaint);
                                        }
                                } else {
                                        canvas.drawRect(rectangle, backgroundPaint);
                                }

                                if (data.getSelectedCell() != null) {
                                        if (c.getValue() != 0 && !c.isOriginal() && !data.isPossible(c)) {
                                                canvas.drawRect(rectangle, errorPaint);
                                        } else if (c == data.getSelectedCell() && data.isNotes()) {
                                                canvas.drawRect(rectangle, notesPaint);
                                        } else if (c == data.getSelectedCell()) {
                                                canvas.drawRect(rectangle, selectedPaint);
                                        }
                                }
                        }
                }
                for (int i = 1; i < data.getGridSize(); i++) {
                        if (i % 3 == 0) {
                                canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), paintMainLines);
                                canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, paintMainLines);
                        }
                }

                if (data.getGrid() == null)
                        return;

                paintMainNumbers.setTextSize(cellHeight / 2);
                paintOriginalNumbers.setTextSize(cellHeight / 2);
                paintSmallNumbers.setTextSize(cellHeight / 4);

                for (int row = 0; row < data.getGridSize(); row++) {
                        for (int col = 0; col < data.getGridSize(); col++) {
                                Cell cell = data.getCell(row, col);
                                int value = cell.getValue();
                                if (value != 0) {
                                        int x = cellWidth / 2 + cellWidth * col; //fica no meio e anda de 1 em 1 cell
                                        int y = cellHeight / 2 + cellHeight * row + cellHeight / 6;
                                        if (cell.isOriginal()) {
                                                canvas.drawText("" + value, x, y, paintOriginalNumbers);
                                        } else {
                                                canvas.drawText("" + value, x, y, paintMainNumbers);
                                        }
                                } else {
                                        int x = cellWidth / 6 + cellWidth * col;
                                        int y = cellHeight / 6 + cellHeight * row;
                                        for (int i = 1; i <= data.getGridSize(); i++) {
                                                if (cell.getNote(i)) {
                                                        int xp = x + (i - 1) % 3 * ((cellWidth - 30) / 3);
                                                        int yp = (y + 9) + (i - 1) / 3 * ((cellHeight - 30) / 3 + cellHeight / 9);
                                                        canvas.drawText("" + i, xp, yp, paintSmallNumbers);
                                                }
                                        }
                                }
                        }
                }
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

        public int getTime(){
                return data.getTime();
        }
        public void decrementTime(){
                data.removeTime();
        }
        public Player getPlayingPlayer(){
                return data.getPlayingPlayer();
        }

        public void setTime(int extraTime) {
                data.setTime(extraTime);
        }

        public void enableNotes() {
                data.enableNotes();
        }

        public void disableNotes() {
                data.disableNotes();
        }

        public boolean isNotes() {
                return data.isNotes();
        }

        public void setSelectedCell(Cell cell) {
                this.data.setSelectedCell(cell);
        }

        public Cell getSelectedCell() {
                return data.getSelectedCell();
        }
        public void clearSelectedCell(){
                data.getSelectedCell().clear();
        }

        public Data getData() {
                return data;
        }

        public void setData(Data data) {
                this.data = data;
        }

        public  int getTotalErrors(){
                return data.getGrid().getDifficulty().getErros();
        }
        public void nextPlayer(){
                 data.nextPlayer();
        }

        public void setCell(Cell cell) {
                data.getGrid().setCell(cell);
        }
        public void setValueSelectedCell(int value){
                this.data.getSelectedCell().setValue(value);
        }

        public boolean isPossible(Cell cell) {
                return data.getGrid().isPossible(cell);
        }

        public int getDifficultyPoints() {
                return data.getGrid().getDifficulty().getPoints();
        }

        public Cell getCell(int row, int col) {
                return data.getGrid().getCell(row,col);
        }

        public boolean isColCompleted(Cell cell) {
                return data.getGrid().isColCompleted(cell);
        }

        public boolean isRowCompleted(Cell cell) {
                return data.getGrid().isRowCompleted(cell);
        }

        public boolean isSquareCompleted(Cell cell) {
                return data.getGrid().isSquareCompleted(cell);
        }

        public boolean canNote(Cell cell, int value) {
                return data.getGrid().canNote(cell,value);
        }

        public void addNoteSelectedCell(int value) {
                this.data.getSelectedCell().addNote(value);
        }

        public boolean isCorrect() {
                return data.getGrid().isCorrect();
        }

        public Player getWinnerPlayer() {
                return data.getWinnerPlayer();
        }

        public void addPlayer(Player player) {
                data.addPlayer(player);
        }

        public void setRandomPlayingPlayer() {
                data.setRandomPlayingPlayer();
        }

        public void weHaveAWinner() {
                 data.weHaveAWinner();
        }

        public boolean amIPlaying() {
                return getPlayingPlayer().getName().equals(player.getName());  // se existir dois com o mesmo nome vai dar problema, mas não tinhamos outra maneira de comprar
                // pois as base de dados são locais, caso fossem online, comparava-se o id de cada player
        }

        public int getGridSize(){
                return data.getGridSize();
        }

}
