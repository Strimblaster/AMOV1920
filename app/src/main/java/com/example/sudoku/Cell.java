package com.example.sudoku;

import java.util.HashMap;

public class Cell {
    private int value;
    private HashMap<Integer,Boolean> hints;
    private boolean isOriginal;
    private int x;
    private int y;

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
        this.x = -1;
        this.y = -1;
    }

    private HashMap<Integer, Boolean> generateHints() {
        HashMap<Integer,Boolean> hashMap = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            hashMap.put((i),true);
        }
        return hashMap;
    }

    int getValue() {
        return value;
    }

    public HashMap<Integer, Boolean> getHints() {
        return hints;
    }

    public void setHints(HashMap<Integer, Boolean> newHints) {
        this.hints = newHints;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
}
