package ru.hse.wikiclicks.controllers.modes;

public class GetWinMessageVisitor implements GameModeVisitor<String> {
    private final GameContext gameContext;

    public GetWinMessageVisitor(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public String visit(TimeGameMode timeGameMode) {
        return "Your time is " + getTimeFromMilliseconds(gameContext.getMillisecondsElapsed());
    }

    @Override
    public String visit(StepsGameMode stepsGameMode) {
        return "Your steps count is " + gameContext.getStepsCount();
    }

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

    @Override
    public String visit(LevelGameMode levelGameMode) {
        return "Your steps count is " + gameContext.getStepsCount();
    }

    private static String getTimeFromMilliseconds(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}