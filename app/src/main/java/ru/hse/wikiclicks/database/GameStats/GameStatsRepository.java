package ru.hse.wikiclicks.database.GameStats;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import ru.hse.wikiclicks.database.GamesDatabase;

// Class that provides methods to manage game stats table
public class GameStatsRepository {

    private GameStatsDao gameStatsDao;
    private LiveData<List<GameStats>> timeGames;
    private LiveData<List<GameStats>> stepsGames;


    GameStatsRepository(Application application) {
        GamesDatabase db = GamesDatabase.getDatabase(application);
        gameStatsDao = db.gameStatsDao();
        timeGames = gameStatsDao.getTimeGames();
        stepsGames = gameStatsDao.getStepsGames();
    }

    LiveData<List<GameStats>> getTimeGames() {
        return timeGames;
    }
    LiveData<List<GameStats>> getStepsGames() {
        return stepsGames;
    }


    public void insert(GameStats gameStats) {
        new GameStatsRepository.insertAsyncTask(gameStatsDao).execute(gameStats);
    }

    private static class insertAsyncTask extends AsyncTask<GameStats, Void, Void> {

        private GameStatsDao asyncTaskDao;

        insertAsyncTask(GameStatsDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GameStats... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
