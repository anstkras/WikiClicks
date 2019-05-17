package ru.hse.wikiclicks.database.TimeMode;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import ru.hse.wikiclicks.database.GamesDatabase;

public class TimeModeGamesRepository {

    private TimeModeGameDao timeModeGameDao;
    private LiveData<List<TimeModeGame>> allGames;

    TimeModeGamesRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        timeModeGameDao = db.timeModeGameDao();
        allGames = timeModeGameDao.getAllGames();
    }

    LiveData<List<TimeModeGame>> getAllGames() {
        return allGames;
    }


    public void insert (TimeModeGame timeModeGame) {
        new insertAsyncTask(timeModeGameDao).execute(timeModeGame);
    }

    private static class insertAsyncTask extends AsyncTask<TimeModeGame, Void, Void> {

        private TimeModeGameDao asyncTaskDao;

        insertAsyncTask(TimeModeGameDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TimeModeGame... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}