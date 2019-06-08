package ru.hse.wikiclicks.controllers.modes;

import java.util.Locale;

/** Class that constructs a proper win message depending on the game mode. */
public class GetWinMessageVisitor implements GameModeVisitor<String> {
    private final GameContext gameContext;

    /** Basic constructor that depends on the current game context. */
    public GetWinMessageVisitor(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    /** Returns the win message for a game in time mode. */
    @Override
    public String visit(TimeGameMode timeGameMode) {
        return "Your time is " + getTimeFromMilliseconds(gameContext.getMillisecondsElapsed());
    }

    /** Returns the win message for a game in steps mode. */
    @Override
    public String visit(StepsGameMode stepsGameMode) {
        return "Your steps count is " + gameContext.getStepsCount();
    }

    /** Returns the win message for a game in custom mode. */
    @Override
    public String visit(CustomGameMode customGameMode) {
        String result = "";
        if (customGameMode.timeModeEnabled()) {
            result += "Your time is " + getTimeFromMilliseconds(gameContext.getMillisecondsElapsed());
        }
        if (customGameMode.stepsModeEnabled()) {
            if (!result.equals("")) {
                result += System.lineSeparator();
            }
            result += "Your steps count is " + gameContext.getStepsCount();
        }
        return result;
    }

    /** Returns the start game activity for a game in level mode. */
    @Override
    public String visit(LevelGameMode levelGameMode) {
        return "Your steps count is " + gameContext.getStepsCount();
    }

    private static String getTimeFromMilliseconds(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        return String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
    }
}