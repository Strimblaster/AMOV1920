package com.example.sudoku.Core;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlayerScoreDAO {
        @Insert
        long insert(PlayerScoreJoin playerScoreJoin);

        @Query("DELETE  FROM player_score_join;")
        void deleteAll();

        @Query("SELECT * FROM score " +
            "INNER JOIN player_score_join " +
            "ON score.id=player_score_join.scoreID " +
            "WHERE player_score_join.playerID=:playerID")
        List<Score> getScoresForPlayer(final int playerID);
}
