package ru.hse.wikiclicks.database;

import androidx.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TimeModeGameDao {
    @Insert
    void insert(TimeModeGame timeModeGame);

    @Query("DELETE FROM games_table")
    void deleteAll();

    @Query("SELECT * from games_table ORDER BY time ASC")
    LiveData<List<TimeModeGame>> getAllWords();
}
