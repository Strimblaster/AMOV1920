package com.example.sudoku;

import java.io.Serializable;

class Board implements Serializable {
    private int[][] board;
    private static final int size = 9;

    Board() {
        this.board = new int[size][size];
    }

    int[][] getBoard() {
        return board;
    }


    private int getPos(int i, int j) {
        return board[i][j];
    }

    Cell[][] toGrid() {
        Cell[][] grid = new Cell[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                grid[i][j] = new Cell(getPos(i,j));
            }
        }
        return grid;
    }

    void setPos(int i, int j, int value) {
        this.board[i][j] = value;
    }
}
