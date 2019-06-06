package ru.hse.wikiclicks.controllers.modes;

public interface GameModeVisitor <T> {
    T visit(TimeGameMode timeGameMode);

    T visit(StepsGameMode stepsGameMode);

    T visit(CustomGameMode customGameMode);

    T visit(LevelGameMode levelGameMode);
}
