package ru.hse.wikiclicks.controllers.modes;

import ru.hse.wikiclicks.activities.ChallengesActivity;
import ru.hse.wikiclicks.activities.SelectModeActivity;

/**
 * Class that specifies which activity is start game activity depending on mode.
 * This class is a singleton.
 */
public class GetNewGameVisitor implements GameModeVisitor<Class<?>> {

    /** Returns the singleton instance. */
    public static GetNewGameVisitor getInstance() {
        return INSTANCE;
    }

    /** Returns the start game activity for a game in time mode. */
    @Override
    public Class<?> visit(TimeGameMode timeGameMode) {
        return SelectModeActivity.class;
    }

    /** Returns the start game activity for a game in steps mode. */
    @Override
    public Class<?> visit(StepsGameMode stepsGameMode) {
        return SelectModeActivity.class;
    }

    /** Returns the start game activity for a game in custom mode. */
    @Override
    public Class<?> visit(CustomGameMode customGameMode) {
        return SelectModeActivity.class;
    }

    /** Returns the start game activity for a game in level mode. */
    @Override
    public Class<?> visit(LevelGameMode levelGameMode) {
        return ChallengesActivity.class;
    }

    private static final GetNewGameVisitor INSTANCE = new GetNewGameVisitor();

    private GetNewGameVisitor() {
    }
}
