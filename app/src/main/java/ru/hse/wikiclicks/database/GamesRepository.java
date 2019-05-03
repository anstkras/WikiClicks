package ru.hse.wikiclicks.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.sql.Time;
import java.util.List;

public class GamesRepository {

    private TimeModeGameDao timeModeGameDao;
    private LiveData<List<TimeModeGame>> mAllGames;

    GamesRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        timeModeGameDao = db.timeModeGameDao();
        mAllGames = timeModeGameDao.getAllWords();
    }

    LiveData<List<TimeModeGame>> getAllWords() {
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