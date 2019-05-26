package ru.hse.wikiclicks.controllers;

public interface GameMode {
    boolean timeModeEnabled();

    boolean stepsModeEnabled();

    boolean banYearsEnabled();

    boolean banCountriesEnabled();
}
