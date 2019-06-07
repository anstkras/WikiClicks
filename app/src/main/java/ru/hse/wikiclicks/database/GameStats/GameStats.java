package ru.hse.wikiclicks.database.GameStats;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Class that represents Game statistics entity in Games database's bookmarks table */
@Entity(tableName = "game_stats_table")
public class GameStats {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
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

    public GameStats(long value, @NonNull String from, @NonNull String to, boolean isTime) {
        this.value = value;
        this.from = from;
        this.to = to;
        this.isTime = isTime;
    }

    public long getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFrom() {
        return from;
    }

    @NonNull
    public String getTo() {
        return to;
    }

    public boolean isTime() {
        return isTime;
    }
}
