package ru.hse.wikiclicks.database.StepsMode;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StepsModeGamesViewModel extends AndroidViewModel {

    private StepsModeGamesRepository repository;

    private LiveData<List<StepsModeGame>> allGames;

    public StepsModeGamesViewModel(@NonNull Application application) {
        super(application);
        repository = new StepsModeGamesRepository(application);
        allGames = repository.getAllGames();
    }

    public LiveData<List<StepsModeGame>> getAllGames() {
        return allGames;
    }

    public void insert(StepsModeGame stepsModeGame) {
        repository.insert(stepsModeGame);
    }
}
