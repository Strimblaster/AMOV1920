package com.example.sudoku.Core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Data implements Serializable {
        private Grid grid;
        private ArrayList<Player> players;
        private Cell selectedCell;
        private boolean notes;
        private int  time;
        private boolean gameStatus;
        private int errors;

        public Data(Difficulty difficulty) {
                this.grid = new Grid(difficulty);
                this.players = new ArrayList<>();
                this.selectedCell = null;
                this.notes = false;
                this.time = 0;
                this.gameStatus = false;
                this.errors = 0;
        }

        public int getErrors() {
                return errors;
        }

        public void addErrors() {
                this.errors++;
        }

        public boolean doWeHaveAWinner() {
                return gameStatus;
        }

        public void weHaveAWinner() {
                this.gameStatus = true;
        }

        public int getTime() {
                return time;
        }

        public void addTime() {
                this.time++;
        }

        public void removeTime() {
                this.time--;
        }

        public boolean isNotes() {
                return notes;
        }

        public void enableNotes() {
                this.notes = true;
        }

        public void disableNotes() {
                this.notes = false;
        }

        public Cell getSelectedCell() {
                return selectedCell;
        }

        public void setSelectedCell(Cell cell) {
                this.selectedCell = cell;
        }

        public Grid getGrid() {
                return grid;
        }

        public int getGridSize() {
                return grid.getSize();
        }

        void setGrid(Grid grid) {
                this.grid = grid;
        }

        public ArrayList<Player> getPlayers() {
                return players;
        }

        void setPlayers(ArrayList<Player> players) {
                this.players = players;
        }

        public void addPlayer(Player player) {
                this.players.add(player);
        }

        public Cell getCell(int row, int col) {
                return grid.getCell(row,col);
        }

        public boolean isPossible(Cell cell) {
                return grid.isPossible(cell);
        }

        public Player getPlayingPlayer() {
                for (Player player : players){
                        if (player.isPlaying()){
                                return player;
                        }
                }
                return null;
        }
        public void nextPlayer() {
                int jogar = -1;
                for (int i = 0; i < players.size() ; i++) {
                        if (players.get(i).isPlaying()){
                                jogar = i;
                        }
                }
                players.get(jogar).setNotPlaying();
                if (jogar == (players.size() -1)){
                        jogar = 0;
                }else{
                        jogar++;
                }
                players.get(jogar).setPlaying();
                players.get(jogar).resetTime();
        }

        public Player getWinnerPlayer() {
                Player ret = null;
                for (Player player: players){
                        if (ret == null){
                                ret = player;
                        }else if (player.getPoints() > ret.getPoints()){
                                ret = player;
                        }
                }
                return ret;
        }

        public void setRandomPlayingPlayer() {
                int pos = new Random().nextInt(players.size());
                players.get(pos).setPlaying();
        }

        public void setTime(int time) {
                this.time = time;
        }
}
