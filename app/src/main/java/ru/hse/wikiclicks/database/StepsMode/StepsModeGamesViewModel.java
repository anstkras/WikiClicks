package ru.hse.wikiclicks.database.StepsMode;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StepsModeGamesViewModel extends AndroidViewModel {

    private StepsModeGamesRepository repository;

    private LiveData<List<StepsModeGame>> timeGames;
    private LiveData<List<StepsModeGame>> stepsGames;


    public StepsModeGamesViewModel(@NonNull Application application) {
        super(application);
        repository = new StepsModeGamesRepository(application);
        timeGames = repository.getTimeGames();
        stepsGames = repository.getStepsGames();
    }

    public LiveData<List<StepsModeGame>> getTimeGames() {
        return timeGames;
    }
    public LiveData<List<StepsModeGame>> getStepsGames() {
        return stepsGames;
    }


    public void insert(StepsModeGame stepsModeGame) {
        repository.insert(stepsModeGame);
    }
}
