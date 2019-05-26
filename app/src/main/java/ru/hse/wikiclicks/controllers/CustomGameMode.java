package ru.hse.wikiclicks.controllers;

public class CustomGameMode implements GameMode{
    private final boolean isTimeModeEnabled;
    private final boolean isStepsModeEnabled;
    private final boolean isBanYearsEnabled;
    private final boolean isBanCountriesEnabled;

    public CustomGameMode(boolean isTimeModeEnabled, boolean isStepsModeEnabled, boolean isBanYearsEnabled, boolean isBanCountriesEnabled) {
        this.isTimeModeEnabled = isTimeModeEnabled;
        this.isStepsModeEnabled = isStepsModeEnabled;
        this.isBanYearsEnabled = isBanYearsEnabled;
        this.isBanCountriesEnabled = isBanCountriesEnabled;
    }

    @Override
    public boolean timeModeEnabled() {
        return isTimeModeEnabled;
    }

    @Override
    public boolean stepsModeEnabled() {
        return isStepsModeEnabled;
    }

    @Override
    public boolean banYearsEnabled() {
        return isBanYearsEnabled;
    }

    @Override
    public boolean banCountriesEnabled() {
        return isBanCountriesEnabled;
    }
}
