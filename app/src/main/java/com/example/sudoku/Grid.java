package com.example.sudoku;

import android.os.SystemClock;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

class Grid {
    private Cell[][] grid;
    private Difficulty difficulty;
    private static final String url = "https://sugoku.herokuapp.com/";
    private static final int size = 9;
    private Status status;
    private Solution solution;


    Grid(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.status = Status.unsolved;
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
        solve();
    }

    boolean validate(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    URL api = new URL(url+"validate");
                    Board board = toBoard();
                    String json = gson.toJson(board);
                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(json.getBytes("utf-8"), 0, json.getBytes("utf-8").length);
                    InputStream inputStream = conn.getInputStream();
                    byte[] b = new byte[1000];
                    int read = inputStream.read(b);
                    String json1 = new String(b, 0, read);
                    JsonParser parser = new JsonParser();
                    JsonObject o = parser.parse(json1).getAsJsonObject();
                    status = Status.valueOf(o.get("status").toString().replace("\"", ""));
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
        return status == Status.solved;
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

    void solve(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    URL api = new URL(url+"solve");
                    Board board = toBoard();
                    String json = gson.toJson(board);
                    HttpURLConnection conn = (HttpURLConnection) api.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(json.getBytes("utf-8"), 0, json.getBytes("utf-8").length);
                    solution = gson.fromJson(new BufferedReader(new InputStreamReader((conn.getInputStream()))), Solution.class);
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


    boolean isCorrect(){
        return solution.getSolution() == toBoard().getBoard();
    }

//    HashMap<Integer,Boolean> getCellHints(int row, int col){
//        if(row > 9 || row < 0){
//            throw new IndexOutOfBoundsException();
//        }
//        if (col > 9 || col < 0){
//            throw  new  IndexOutOfBoundsException();
//        }
//        HashMap<Integer,Boolean> newHints = new HashMap<>();
//        for (int i = 1; i <size+1 ; i++) {
//            newHints.put(i,true);
//        }
//        // linhas - Row
//        for (int i = 0; i < size; i++) {
//            int value = grid[row][i].getValue();
//            if (value > 0 && newHints.get(value)) {
//                newHints.put(value,false);
//            }
//        }
//        // colunas - cols
//        for (int i = 0; i < size; i++) {
//            int value = grid[i][col].getValue();
//            if (value > 0 && newHints.get(value)) {
//                newHints.put(value,false);
//            }
//        }
//        //Quadrado
//        int start_row = row/3;
//        int start_col = col/3;
//        for (int i = start_row*3; i < (start_row*3) + 3 ; i++) {
//            for (int j = start_col*3; j <(start_col*3) +3 ; j++) {
//                int value = grid[i][j].getValue();
//                if (value > 0 && newHints.get(value)) {
//                    newHints.put(value,false);
//                }
//            }
//        }
//        int trues;
//        if (getDifficulty() == Difficulty.easy){
//            trues = 7;
//        }else if (getDifficulty() == Difficulty.hard){
//            trues = 3;
//        }else if (getDifficulty() == Difficulty.medium){
//            trues = 5;
//        }else{
//            Random rnd = new Random(SystemClock.elapsedRealtime());
//            trues = rnd.nextInt(6)+1;
//        }
//        int counter = 0;
//        for (int i = 1; i <= size ; i++) {
//            if (newHints.get(i)){
//                counter++;
//            }
//            if (newHints.get(i) && counter > trues){
//                newHints.put(i,false);
//            }
//        }
//        return  newHints;
//    }

    boolean isPossible(Cell cell){
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

    Cell getCell(int row, int col) {
        return grid[row][col];
    }

    int getSize(){
        return size;
    }

    void setCell(Cell cell){
        grid[cell.getRow()][cell.getCol()] = cell;
    }
}

