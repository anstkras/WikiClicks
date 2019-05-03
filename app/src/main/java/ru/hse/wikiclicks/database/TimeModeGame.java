package ru.hse.wikiclicks.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "games_table")
public class TimeModeGame {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "time")
    private String time;

    public TimeModeGame(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

}

