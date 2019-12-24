package com.example.sudoku.Core;

import java.io.Serializable;
import java.util.HashMap;

public class Cell implements Serializable {
    private int value;
    private HashMap<Integer,Boolean> notes;
    private boolean isOriginal;
    private int col;
    private int row;

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setValue(int value) {
        this.value = value;
    }

    void setOriginal() {
        isOriginal = true;
    }

    Cell(int value,int row, int col) {
        this.value = value;
        this.notes = generateHints();
        this.isOriginal = false;
        this.col = col;
        this.row = row;
    }

    private HashMap<Integer, Boolean> generateHints() {
        HashMap<Integer,Boolean> hashMap = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            hashMap.put((i),false);
        }
        return hashMap;
    }

    int getValue() {
        return value;
    }

    public boolean addNote(int value){
        if (value > 0 && value <= 9) {
            return notes.put(value, true);
        }
        return false;
    }
    public boolean getNote(int value){
        return notes.get(value);
    }

    public boolean removeNote (int value){
        if (value > 0 && value <= 9) {
            return notes.put(value, false);
        }
        return false;
    }

    public int getCol(){
        return col;
    }

    public int getRow(){
        return row;
    }

    public void setCol(int x){
        this.col = x;
    }

    public void setRow(int y){
        this.row = y;
    }

    public void clear() {
        if (!isOriginal()) {
            this.value = 0;
            for (int i = 1; i <= 9; i++) {
                removeNote(i);
            }
        }
    }

    public boolean hasNotes(){
        for (int i = 1; i <=9 ; i++) {
            if (getNote(i)){
                return true;
            }
        }
        return false;
    }

    public boolean inSquare(Cell selectedCell) {
        int start_row = this.row / 3;
        int start_col = this.col / 3 ;
        int selected_start_row = selectedCell.getRow() / 3;
        int selected_start_col = selectedCell.getCol() / 3;
        if (start_row == selected_start_row && start_col == selected_start_col ){
                return true;
        }
        return false;
    }
}
