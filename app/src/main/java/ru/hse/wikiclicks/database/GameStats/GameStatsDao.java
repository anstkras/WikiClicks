package ru.hse.wikiclicks.database.GameStats;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface GameStatsDao {
    @Insert
    void insert(GameStats gameStats);

    @Query("DELETE FROM game_stats_table")
    void deleteAll();

    @Query("SELECT * from game_stats_table WHERE isTime ORDER BY id DESC")
    LiveData<List<GameStats>> getTimeGames();

    @Query("SELECT * from game_stats_table WHERE NOT isTime ORDER BY id DESC")
    LiveData<List<GameStats>> getStepsGames();
}
