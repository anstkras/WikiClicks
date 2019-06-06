package ru.hse.wikiclicks.controllers;

public class GameContext {
    private int stepsCount;
    private long millisecondsElapsed;

    public GameContext(int stepsCount, long millisecondsElapsed) {
        this.stepsCount = stepsCount;
        this.millisecondsElapsed = millisecondsElapsed;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public long getMillisecondsElapsed() {
        return millisecondsElapsed;
    }
}
