package com.example.sudoku.Core;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlayerDAO {
        @Query("SELECT * FROM player;")
        List<Player> getPlayer();

        @Insert
        void insertPlayer(Player player);

        @Query("DELETE  FROM player;")
        void deleteAll();
}
