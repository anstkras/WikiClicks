package ru.hse.wikiclicks.controllers.modes;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

/** Class that saves necessary statistics depending on the game mode. */
public class SaveStatsVisitor implements GameModeVisitor<Void> {
    private final GameContext gameContext;
    private final GameStatsViewModel gameStatsViewModel;

    /** Basic constructor that depends on the current game context. */
    public SaveStatsVisitor(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameStatsViewModel = ViewModelProviders.of(gameContext.getActivity()).get(GameStatsViewModel.class);
    }

    /** Submits statistics for time game mode. */
    @Override
    public Void visit(TimeGameMode timeGameMode) {
        GameStats gameStats = new GameStats(gameContext.getMillisecondsElapsed(), gameContext.getStartTitle(), gameContext.getFinishTitle(), true);
        gameStatsViewModel.insert(gameStats);
        return null;
    }

    /** Submits statistics for steps game mode. */
    @Override
    public Void visit(StepsGameMode stepsGameMode) {
        GameStats gameStats = new GameStats(gameContext.getStepsCount(), gameContext.getStartTitle(), gameContext.getFinishTitle(), false);
        gameStatsViewModel.insert(gameStats);
        return null;
    }

    /** Submits no statistics for custom game mode. */
    @Override
    public Void visit(CustomGameMode customGameMode) {
        return null;
    }

    /** Submits scores to the correct leaderboards for level game mode. */
    @Override
    public Void visit(LevelGameMode levelGameMode) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(gameContext.getActivity());
        if (account == null) {
            return null;
        }
        /*
         * Всю эту пачку if'ов можно заменить на один for
         */
        if (levelGameMode.getLevel() == 0) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_0), account);
        }
        if (levelGameMode.getLevel() == 1) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_1), account);
        }
        if (levelGameMode.getLevel() == 2) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_2), account);
        }
        if (levelGameMode.getLevel() == 3) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_3), account);
        }
        if (levelGameMode.getLevel() == 4) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_4), account);
        }
        if (levelGameMode.getLevel() == 5) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_5), account);
        }
        if (levelGameMode.getLevel() == 6) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_6), account);
        }
        if (levelGameMode.getLevel() == 7) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_7), account);
        }
        if (levelGameMode.getLevel() == 8) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_8), account);
        }
        if (levelGameMode.getLevel() == 9) {
            submitScore(gameContext.getActivity().getString(R.string.leaderboard_level_9), account);
        }

        Toast toast = Toast.makeText(gameContext.getActivity(), "Your score was submitted to the leaderboard", Toast.LENGTH_SHORT);
        toast.show();
        return null;
    }

    private void submitScore(String leaderBoardID, GoogleSignInAccount account) {
        Games.getLeaderboardsClient(gameContext.getActivity(), account)
                .submitScore(leaderBoardID, gameContext.getStepsCount());
    }
}