package ru.hse.wikiclicks.controllers;

public class StepsGameMode implements GameMode {
    @Override
    public boolean timeModeEnabled() {
        return false;
    }

    @Override
    public boolean stepsModeEnabled() {
        return true;
    }
}
