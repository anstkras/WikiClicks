package ru.hse.wikiclicks.controllers.modes;

public interface GameMode {
    boolean timeModeEnabled();

    boolean stepsModeEnabled();

    boolean banYearsEnabled();

    boolean banCountriesEnabled();

    boolean banBackEnabled();

    <T> T accept(GameModeVisitor<T> visitor);
}
