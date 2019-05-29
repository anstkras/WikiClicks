package ru.hse.wikiclicks.controllers;

public class LevelGameMode implements GameMode {
    private final int level;

    public LevelGameMode(int level) {
        this.level = level;
    }

    @Override
    public boolean timeModeEnabled() {
        return false;
    }

    @Override
    public boolean stepsModeEnabled() {
        return true;
    }

    @Override
    public boolean banYearsEnabled() {
        return false;
    }

    @Override
    public boolean banCountriesEnabled() {
        return false;
    }

    @Override
    public boolean banBackEnabled() {
        return false;
    }

    public int getLevel() {
        return level;
    }
}
