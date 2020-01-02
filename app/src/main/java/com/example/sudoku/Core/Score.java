package com.example.sudoku.Core;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "score")
public class Score implements Serializable {
        @PrimaryKey(autoGenerate = true)
        private long id;
        @ColumnInfo(name = "mode")
        private String mode;
        @ColumnInfo(name = "time")
        private  int timeM1;
        @ColumnInfo(name = "plays")
        private int rightPlaysM2M3;
        @ColumnInfo(name = "winner")
        private String winner;

        public Score() {
                this.mode = "";
                this.timeM1 = 0;
                this.rightPlaysM2M3 = 0;
                this.winner = "";
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getMode() {
                return mode;
        }

        public void setMode(String mode) {
                this.mode = mode;
        }

        public int getTimeM1() {
                return timeM1;
        }

        public void setTimeM1(int timeM1) {
                this.timeM1 = timeM1;
        }

        public int getRightPlaysM2M3() {
                return rightPlaysM2M3;
        }

        public void setRightPlaysM2M3(int rightPlaysM2M3) {
                this.rightPlaysM2M3 = rightPlaysM2M3;
        }

        public String getWinner() {
                return winner;
        }

        public void setWinner(String winner) {
                this.winner = winner;
        }
}
