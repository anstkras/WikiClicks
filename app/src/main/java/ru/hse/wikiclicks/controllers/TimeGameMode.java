package ru.hse.wikiclicks.controllers;

public class TimeGameMode implements GameMode { // TODO make singleton?
    @Override
    public boolean timeModeEnabled() {
        return true;
    }

    @Override
    public boolean stepsModeEnabled() {
        return false;
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
}
