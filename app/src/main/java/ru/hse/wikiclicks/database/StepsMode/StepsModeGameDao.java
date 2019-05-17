package ru.hse.wikiclicks.database.StepsMode;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface StepsModeGameDao {
    @Insert
    void insert(StepsModeGame stepsModeGame);

    @Query("DELETE FROM steps_mode_games_table")
    void deleteAll();

    @Query("SELECT * from steps_mode_games_table ORDER BY steps ASC")
    LiveData<List<StepsModeGame>> getAllGames();
}
