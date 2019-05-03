package ru.hse.wikiclicks.controllers;

public class GameModeFactory {
    public static GameMode getGameMode(String mode) {
        if (mode.equals("time_mode")) {
            return new TimeGameMode();
        }
        if (mode.equals("steps_mode")) {
            return new StepsGameMode();
        }
        throw new IllegalArgumentException("No such mode exists: " + mode);
    }
}
