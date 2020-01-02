package com.example.sudoku.Core;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Stack;

@Entity(tableName = "player")
public class Player implements Serializable {
        @PrimaryKey
        private int id;
        @ColumnInfo(name = "name")
        private String name;
        @ColumnInfo(name = "photo")
        private String photoPath;

        @Ignore
        private int points;
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
        }

        @Ignore
        public Player(String name) {
                this.name = name;
                this.points = 0;
                this.timeStack = new Stack<>();
                this.playing = false;
                this.photoPath = "";
        }

        @Ignore
        public Player() {
                this.name = "";
                this.photoPath = "";
                this.points = 0;
                this.timeStack = new Stack<>();
                this.playing = false;
        }


        public String getPhotoPath() {
                return photoPath;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
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

        public void removePoints(int points) {
                this.points -= points;
        }

        private int showExtraTime() {
                return this.timeStack.peek();
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
}
