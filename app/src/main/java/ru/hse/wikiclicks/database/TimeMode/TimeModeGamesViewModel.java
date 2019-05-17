package ru.hse.wikiclicks.database.TimeMode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TimeModeGamesViewModel extends AndroidViewModel {
    private TimeModeGamesRepository repository;

    private LiveData<List<TimeModeGame>> allGames;

    public TimeModeGamesViewModel(@NonNull Application application) {
        super(application);
        repository = new TimeModeGamesRepository(application);
        allGames = repository.getAllGames();
    }

    public LiveData<List<TimeModeGame>> getAllGames() {
        return allGames;
    }

    public void insert(TimeModeGame timeModeGame) {
        repository.insert(timeModeGame);
    }
}
