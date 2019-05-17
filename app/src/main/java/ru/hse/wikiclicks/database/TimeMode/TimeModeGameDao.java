package ru.hse.wikiclicks.database.TimeMode;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TimeModeGameDao {
    @Insert
    void insert(TimeModeGame timeModeGame);

    @Query("DELETE FROM time_mode_games_table")
    void deleteAll();

    @Query("SELECT * from time_mode_games_table ORDER BY time ASC")
    LiveData<List<TimeModeGame>> getAllGames();
}
