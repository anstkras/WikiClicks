package ru.hse.wikiclicks.controllers.modes;

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

    @Override
    public <T> T accept(GameModeVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
