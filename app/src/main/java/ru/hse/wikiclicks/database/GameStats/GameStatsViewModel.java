package ru.hse.wikiclicks.database.GameStats;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/** View model for a game statistics entity from the database */
public class GameStatsViewModel extends AndroidViewModel {

    private GameStatsRepository repository;

    private LiveData<List<GameStats>> timeGames;
    private LiveData<List<GameStats>> stepsGames;

    public GameStatsViewModel(@NonNull Application application) {
        super(application);
        repository = new GameStatsRepository(application);
        timeGames = repository.getTimeGames();
        stepsGames = repository.getStepsGames();
    }

    public LiveData<List<GameStats>> getTimeGames() {
        return timeGames;
    }
    public LiveData<List<GameStats>> getStepsGames() {
        return stepsGames;
    }

    public void insert(GameStats gameStats) {
        repository.insert(gameStats);
    }
}
