package com.example.sudoku;

import java.util.HashMap;

public class Cell {
    private int value;
    private HashMap<Integer,Boolean> hints;
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

    Cell(int value) {
        this.value = value;
        this.hints = generateHints();
        this.isOriginal = false;
        this.col = -1;
        this.row = -1;
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

    public boolean addHint(int value){
        if (value > 0 && value <= 9) {
            return hints.put(value, true);
        }
        return false;
    }
    public boolean getHint(int value){
        return hints.get(value);
    }

    public boolean removeHint (int value){
        if (value > 0 && value <= 9) {
            return hints.put(value, false);
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
}
