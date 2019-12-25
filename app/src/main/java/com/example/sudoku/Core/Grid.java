package com.example.sudoku.Core;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Grid implements Serializable {
    private Cell[][] grid;
    private Difficulty difficulty;
    private static final String url = "https://sugoku.herokuapp.com/";
    private static final int size = 9;
    private Solution solution;


    public Grid(Difficulty difficulty) {
        this.difficulty = difficulty;
        buildGrid();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    private void buildGrid() {
        grid = new Cell[size][size];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                        Gson gson = new Gson();
                        URL api = new URL(url+"board?difficulty="+ difficulty.name());
                        HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Accept", "application/json");

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                        }


                        Board board = gson.fromJson(new BufferedReader(new InputStreamReader((conn.getInputStream()))), Board.class);
                        grid = board.toGrid();
                        solve();
                        conn.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    Board toBoard() {
        Board board = new Board();
        for (int i = 0; i < size ; i++) {
            for (int j = 0; j < size; j++) {
                board.setPos(i,j,grid[i][j].getValue());
            }
        }
        return board;
    }

    private void solve(){
        solution = new Solution();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    URL api = new URL(url+"solve");
                    Board board = toBoard();
                    StringBuilder stringBuilder = new StringBuilder();

                    String json = gson.toJson(board.getBoard());

                    stringBuilder.append(URLEncoder.encode("board", "UTF-8"));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(String.valueOf(json), "UTF-8"));
                    byte[] bytes = stringBuilder.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(bytes);
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    solution = gson.fromJson(in, Solution.class);
                    for (int i = 0; i < getSize(); i++) {
                        for (int j = 0; j < getSize(); j++) {
                            System.out.print(solution.getBoard()[i][j]+" ");
                        }
                        System.out.println();
                    }
                    conn.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean isCorrect(){
        int[][] sol = solution.getBoard();
        int dif = 0;
        for (int row = 0; row < getSize(); row++) {
            for (int col = 0; col <getSize() ; col++) {
                if(sol[row][col] != getValue(row,col)){
                    dif++;
                }
            }
        }
        return dif == 0;
    }

    public boolean canNote(Cell cell, int note){
            int row = cell.getRow();
            int col = cell.getCol();
            int value =  note;
            // linhas - Row
            for (int r = 0; r < getSize(); r++) {
                if (value == grid[r][col].getValue() && r != row) {
                    return false;
                }
            }
            // colunas - cols
            for (int c = 0; c < getSize(); c++) {
                if (value == grid[row][c].getValue() && c != col) {
                    return false;
                }
            }
            //Quadrado
            int start_row = row/3;
            int start_col = col/3;
            for (int i = start_row*3; i < (start_row*3) + 3 ; i++) {
                for (int j = start_col*3; j <(start_col*3) +3 ; j++) {
                    if (value == grid[i][j].getValue() && (i != row && j != col)) {
                        return false;
                    }
                }
            }
            return true;
        }

    public boolean isPossible(Cell cell){
        int row = cell.getRow();
        int col = cell.getCol();
        int value =  cell.getValue();
        if (value == 0){
            return true;
        }
        // linhas - Row
        for (int r = 0; r < getSize(); r++) {
            if (value == grid[r][col].getValue() && r != row) {
                return false;
            }
        }
        // colunas - cols
        for (int c = 0; c < getSize(); c++) {
            if (value == grid[row][c].getValue() && c != col) {
                return false;
            }
        }
        //Quadrado
        int start_row = row/3;
        int start_col = col/3;
        for (int i = start_row*3; i < (start_row*3) + 3 ; i++) {
            for (int j = start_col*3; j <(start_col*3) +3 ; j++) {
                if (value == grid[i][j].getValue() && (i != row && j != col)) {
                    return false;
                }
            }
        }
        return true;
    }


    int getValue(int row, int col){
        return grid[row][col].getValue();
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public int getSize(){
        return size;
    }

    public void setCell(Cell cell){
        grid[cell.getRow()][cell.getCol()] = cell;
    }

    public void resetGrid(){
        for (int row = 0; row < getSize() ; row++) {
            for (int col = 0; col < getSize(); col++) {
                grid[row][col].clear();
            }
        }
    }

    public boolean isRowCompleted(Cell cell){
        int[][] sol = solution.getBoard();
        int dif = 0;
        for (int col = 0; col < getSize() ; col++) {
            if (!getCell(cell.getRow(),col).isOriginal()){
                if(sol[cell.getRow()][col] != getCell(cell.getRow(),col).getValue()){
                    dif++;
                }
            }
        }
        return dif == 0;
    }

    public boolean isColCompleted(Cell cell){
        int[][] sol = solution.getBoard();
        int dif = 0;
        for (int row = 0; row < getSize() ; row++) {
            if (!getCell(row,cell.getCol()).isOriginal()){
                if(sol[row][cell.getCol()] != getCell(row,cell.getCol()).getValue()){
                    dif++;
                }
            }
        }
        return dif == 0;
    }

    public boolean isSquareCompleted(Cell cell){
        int[][] sol = solution.getBoard();
        int dif = 0;
        int start_col = cell.getCol()/3;
        int start_row = cell.getRow()/3;
        for (int row = start_row*3; row <(start_row *3)+ 3; row++) {
            for (int col = start_col*3; col < (start_col*3)+3; col++) {
                if(sol[row][col] != getCell(row,col).getValue()){
                    dif++;
                }
            }
        }
        return dif == 0;
    }
}

