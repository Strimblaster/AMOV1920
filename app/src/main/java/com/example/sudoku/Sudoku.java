package com.example.sudoku;

import java.io.Serializable;

public class Sudoku extends Thread implements Serializable {
    private Grid grid;

    public Sudoku()  { }

    public Integer[] getList(){
        int size = grid.getSize();
        Integer list[] = new Integer[size * size] ;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size; j++) {
                list[(i*size)+j] = grid.getValue(i,j);
            }
        }
        return list;
    }

    @Override
    public void run() {
        this.grid = new Grid(Difficulty.random);
    }
}
