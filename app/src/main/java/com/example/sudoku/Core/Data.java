package com.example.sudoku.Core;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private Grid grid;
    private ArrayList<Player> players;

    public Data(Difficulty difficulty) {
        this.grid = new Grid(difficulty);
        this.players = new ArrayList<>();
    }

    public Grid getGrid() {
        return grid;
    }

    void setGrid(Grid grid) {
        this.grid = grid;
    }

    ArrayList<Player> getPlayers() {
        return players;
    }

    void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }
}
