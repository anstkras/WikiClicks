package ru.hse.wikiclicks.controllers;

public class CustomGameMode implements GameMode{
    private final boolean isTimeModeEnabled;
    private final boolean isStepsModeEnabled;
    private final boolean isBanYearsEnabled;
    private final boolean isBanCountriesEnabled;
    private final boolean isBanBackEnabled;

    public CustomGameMode(boolean isTimeModeEnabled, boolean isStepsModeEnabled, boolean isBanYearsEnabled,
                          boolean isBanCountriesEnabled, boolean isBanBackEnabled) {
        this.isTimeModeEnabled = isTimeModeEnabled;
        this.isStepsModeEnabled = isStepsModeEnabled;
        this.isBanYearsEnabled = isBanYearsEnabled;
        this.isBanCountriesEnabled = isBanCountriesEnabled;
        this.isBanBackEnabled = isBanBackEnabled;
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

    @Override
    public boolean banBackEnabled() {
        return isBanBackEnabled;
    }
}
