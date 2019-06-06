package ru.hse.wikiclicks.controllers.modes;

public class StepsGameMode implements GameMode {
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

    @Override
    public <T> T accept(GameModeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
