package ru.hse.wikiclicks.database;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GamesViewModel extends AndroidViewModel {
    private GamesRepository mRepository;

    private LiveData<List<TimeModeGame>> mAllWords;

    public GamesViewModel (Application application) {
        super(application);
        mRepository = new GamesRepository(application);
        mAllWords = mRepository.getAllWords();
    }

    LiveData<List<TimeModeGame>> getAllWords() {
        return mAllWords;
    }

    public void insert(TimeModeGame timeModeGame) {
        mRepository.insert(timeModeGame);
    }
}
