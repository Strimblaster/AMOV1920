package com.example.sudoku;

import java.util.HashMap;

public class Cell {
    private int value;
    private HashMap<Integer,Boolean> hints;

    Cell(int value) {
        this.value = value;
        this.hints = generateHints();
    }

    private HashMap<Integer, Boolean> generateHints() {
        HashMap<Integer,Boolean> hashMap = new HashMap<>();
        for (int i = 0; i < 9; i++) {
            hashMap.put((1 + i),false);
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
