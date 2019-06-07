package ru.hse.wikiclicks.controllers.modes;

/**
 * Interface for implementation of visitor pattern for game mode
 *
 * @param <T> type that visit return
 */
public interface GameModeVisitor<T> {
    T visit(TimeGameMode timeGameMode);

    T visit(StepsGameMode stepsGameMode);

    T visit(CustomGameMode customGameMode);

    T visit(LevelGameMode levelGameMode);
}
