package ru.hse.wikiclicks.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ru.hse.wikiclicks.database.Bookmarks.Bookmark;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkDao;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsDao;

@Database(entities = {GameStats.class, Bookmark.class}, version = 6, exportSchema = false)
public abstract class GamesDatabase extends RoomDatabase {

    public abstract GameStatsDao stepsModeGameDao();
    public abstract BookmarkDao bookmarkDao();

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