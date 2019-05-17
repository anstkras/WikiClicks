package ru.hse.wikiclicks.database.StepsMode;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import ru.hse.wikiclicks.database.GamesDatabase;

public class StepsModeGamesRepository {

    private StepsModeGameDao stepsModeGameDao;
    private LiveData<List<StepsModeGame>> allGames;

    StepsModeGamesRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        stepsModeGameDao = db.stepsModeGameDao();
        allGames = stepsModeGameDao.getAllGames();
    }

    LiveData<List<StepsModeGame>> getAllGames() {
        return allGames;
    }


    public void insert(StepsModeGame stepsModeGame) {
        new StepsModeGamesRepository.insertAsyncTask(stepsModeGameDao).execute(stepsModeGame);
    }

    private static class insertAsyncTask extends AsyncTask<StepsModeGame, Void, Void> {

        private StepsModeGameDao asyncTaskDao;

        insertAsyncTask(StepsModeGameDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final StepsModeGame... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
