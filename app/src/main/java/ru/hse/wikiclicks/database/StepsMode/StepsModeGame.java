package ru.hse.wikiclicks.database.StepsMode;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "steps_mode_games_table")
public class StepsModeGame {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "steps")
    private long steps;

    public StepsModeGame(long steps) {
        this.steps = steps;
    }

    public long getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
