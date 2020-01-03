package com.example.sudoku.Core;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Stack;

@Entity(tableName = "player")
public class Player implements Serializable {
        @PrimaryKey(autoGenerate = true)
        private int id;
        @ColumnInfo(name = "name")
        private String name;
        @ColumnInfo(name = "photo")
        private String photoPath;

        public void setId(int id) {
                this.id = id;
        }
        @Ignore
        private int rightPlays;
        @Ignore
        private int points;
        @Ignore
        private int errors;
        @Ignore
        private Stack<Integer> timeStack;
        @Ignore
        private boolean playing;

        public Player(String name, String photoPath) {
                this.name = name;
                this.points = 0;
                this.timeStack = new Stack<>();
                this.playing = false;
                this.photoPath = photoPath;
                this.rightPlays = 0;
                this.errors = 0;
        }

        @Ignore
        public Player(String name) {
                this.name = name;
                this.points = 0;
                this.timeStack = new Stack<>();
                this.playing = false;
                this.photoPath = "";
                this.rightPlays = 0;
                this.errors = 0;
        }

        @Ignore
        public Player() {
                this.name = "";
                this.photoPath = "";
                this.points = 0;
                this.timeStack = new Stack<>();
                this.playing = false;
                this.rightPlays = 0;
                this.errors = 0;
        }

        public void addRightPlays() {
                this.rightPlays++;
        }

        public String getPhotoPath() {
                return photoPath;
        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public int getPoints() {
                return points;
        }

        public void addPoints(int points) {
                this.points += points;
        }

        public boolean hasMoreTime() {
                return !timeStack.empty();
        }

        public int getExtraTime() {
                return timeStack.pop();
        }

        public void addExtraTime() {
                timeStack.push(20);
        }

        void resetTime() {
                timeStack.clear();
                timeStack.push(30);
        }

        public void removePoints(int points) {
                this.points -= points;
        }


        public boolean isPlaying() {
                return playing;
        }

        public void setPlaying() {
                this.playing = true;
        }

        public void setNotPlaying() {
                this.playing = false;
        }

        public int getRightPlays() {
                return rightPlays;
        }

        public int getErrors() {
                return errors;
        }

        public void addErrors() {
                this.errors++;
        }
}
