package com.example.sudoku;

import java.util.HashMap;

public class Cell {
    private int value;
    private HashMap<Integer,Boolean> hints;
    private boolean isOriginal;

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
}
