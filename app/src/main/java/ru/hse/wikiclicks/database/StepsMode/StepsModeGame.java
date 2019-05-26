package ru.hse.wikiclicks.database.StepsMode;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps_mode_games_table")
public class StepsModeGame {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "value")
    private long value;

    @ColumnInfo(name = "from")
    private String from;

    @ColumnInfo(name = "to")
    private String to;

    @ColumnInfo(name = "isTime")
    private boolean isTime;

    public StepsModeGame(long value, String from, String to, boolean isTime) {
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

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isTime() {
        return isTime;
    }
}
