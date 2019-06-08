package ru.hse.wikiclicks.database.GameStats;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/** View model for a game statistics entity from the database. */
public class GameStatsViewModel extends AndroidViewModel {

    private GameStatsRepository repository;

    private LiveData<List<GameStats>> timeGames;
    private LiveData<List<GameStats>> stepsGames;

    /** Creates the GameStatsViewModel for an Application. */
    public GameStatsViewModel(@NonNull Application application) {
        super(application);
        repository = new GameStatsRepository(application);
        timeGames = repository.getTimeGames();
        stepsGames = repository.getStepsGames();
    }

    /** Returns a list of the existing statistics for timed games. */
    public LiveData<List<GameStats>> getTimeGames() {
        return timeGames;
    }

    /** Returns a list of the existing statistics for step-counting games. */
    public LiveData<List<GameStats>> getStepsGames() {
        return stepsGames;
    }

    /** Adds a new game statistics to the DB. */
    public void insert(GameStats gameStats) {
        repository.insert(gameStats);
    }
}
