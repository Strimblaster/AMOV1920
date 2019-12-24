package com.example.sudoku.Core;

import java.io.Serializable;

public class Solution implements Serializable {
    private String difficulty;
    private int[][] solution;
    private String status;

    public Solution() {
        difficulty = "";
        solution = new int[9][9];
        status = "";
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int[][] getBoard() {
        return solution;
    }

    public String getStatus() {
        return status;
    }
}
