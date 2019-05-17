package ru.hse.wikiclicks.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ru.hse.wikiclicks.database.StepsMode.StepsModeGame;
import ru.hse.wikiclicks.database.StepsMode.StepsModeGameDao;
import ru.hse.wikiclicks.database.TimeMode.TimeModeGame;
import ru.hse.wikiclicks.database.TimeMode.TimeModeGameDao;

@Database(entities = {TimeModeGame.class, StepsModeGame.class}, version = 2, exportSchema = false)
public abstract class GamesDatabase extends RoomDatabase {

    public abstract TimeModeGameDao timeModeGameDao();

    public abstract StepsModeGameDao stepsModeGameDao();

    private static volatile GamesDatabase INSTANCE;

    public static GamesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesDatabase.class, "games_table")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}