package com.example.sudoku.Core;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class, Score.class, PlayerScoreJoin.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
        public abstract PlayerDAO player();
        public abstract ScoreDAO score();
        public abstract PlayerScoreDAO  conn();
}
