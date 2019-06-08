package ru.hse.wikiclicks.database.GameStats;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Class that represents Game statistics entity in Games database's bookmarks table. */
@Entity(tableName = "game_stats_table")
public class GameStats {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "value")
    private long value;

    @NonNull
    @ColumnInfo(name = "from")
    private String from;

    @NonNull
    @ColumnInfo(name = "to")
    private String to;

    @ColumnInfo(name = "isTime")
    private boolean isTime;

    /** Creates a game statistics entry for the given game, either for time mode or steps. */
    public GameStats(long value, @NonNull String from, @NonNull String to, boolean isTime) {
        this.value = value;
        this.from = from;
        this.to = to;
        this.isTime = isTime;
    }

    /** Returns the game statistics' entry's value. */
    public long getValue() {
        return value;
    }

    /** Returns the game statistics' entry's id. */
    public int getId() {
        return id;
    }

    /** Sets the game statistics' entry's id to equal the given id. */
    public void setId(int id) {
        this.id = id;
    }

    /** Returns the game statistics' entry's start page. */
    @NonNull
    public String getFrom() {
        return from;
    }

    /** Returns the game statistics' entry's finish page. */
    @NonNull
    public String getTo() {
        return to;
    }

    /** Returns whether the entry is about a game in time mode. */
    public boolean isTime() {
        return isTime;
    }
}
