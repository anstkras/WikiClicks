package ru.hse.wikiclicks.database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class GamesRepository {

    private TimeModeGameDao timeModeGameDao;
    private LiveData<List<TimeModeGame>> mAllGames;

    GamesRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        timeModeGameDao = db.timeModeGameDao();
        mAllGames = timeModeGameDao.getAllGames();
    }

    LiveData<List<TimeModeGame>> getAllGames() {
        return mAllGames;
    }


    public void insert (TimeModeGame timeModeGame) {
        new insertAsyncTask(timeModeGameDao).execute(timeModeGame);
    }

    private static class insertAsyncTask extends AsyncTask<TimeModeGame, Void, Void> {

        private TimeModeGameDao mAsyncTaskDao;

        insertAsyncTask(TimeModeGameDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimeModeGame... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}