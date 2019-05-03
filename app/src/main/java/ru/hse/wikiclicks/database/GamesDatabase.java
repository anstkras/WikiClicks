package ru.hse.wikiclicks.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TimeModeGame.class}, version = 1)
public abstract class GamesDatabase extends RoomDatabase {

    public abstract TimeModeGameDao timeModeGameDao();

    private static volatile GamesDatabase INSTANCE;

    static GamesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GamesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GamesDatabase.class, "games_table")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}