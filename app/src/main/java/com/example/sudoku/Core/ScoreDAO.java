package com.example.sudoku.Core;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ScoreDAO {

        @Query("SELECT * FROM score")
        List<Score> getScores();

        @Insert
        public long insertScore(Score score);

        @Query("DELETE  FROM score;")
        void deleteAll();
}
