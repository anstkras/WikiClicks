package ru.hse.wikiclicks.controllers.modes;

import androidx.appcompat.app.AppCompatActivity;

/** Class that stores important information about game state to be used by visitors. */
public class GameContext {
    private final int stepsCount;
    private final long millisecondsElapsed;
    private final AppCompatActivity activity;
    private final String startTitle;
    private final String finishTitle;

    /** Basic constructor for the class. */
    public GameContext(int stepsCount, long millisecondsElapsed, AppCompatActivity activity, String startTitle, String finishTitle) {
        this.stepsCount = stepsCount;
        this.millisecondsElapsed = millisecondsElapsed;
        this.activity = activity;
        this.startTitle = startTitle;
        this.finishTitle = finishTitle;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public long getMillisecondsElapsed() {
        return millisecondsElapsed;
    }

    public AppCompatActivity getActivity() {
        return activity;
    }

    public String getFinishTitle() {
        return finishTitle;
    }

    public String getStartTitle() {
        return startTitle;
    }
}
