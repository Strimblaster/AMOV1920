package com.example.sudoku.Core;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;


@Entity(tableName = "player_score_join",
    primaryKeys = { "playerID", "scoreID" },
    foreignKeys = {
        @ForeignKey(entity = Player.class,
            parentColumns = "id",
            childColumns = "playerID"),
        @ForeignKey(entity = Score.class,
            parentColumns = "id",
            childColumns = "scoreID")
    },indices = {@Index("scoreID")})
public class PlayerScoreJoin {
        @ColumnInfo(name = "playerID")
        public int playerID;
        @ColumnInfo(name = "scoreID")
        public long scoreID;

        public PlayerScoreJoin() {
        }

        public int getPlayerID() {
                return playerID;
        }

        public void setPlayerID(int playerID) {
                this.playerID = playerID;
        }

        public long getScoreID() {
                return scoreID;
        }

        public void setScoreID(long scoreID) {
                this.scoreID = scoreID;
        }
}
