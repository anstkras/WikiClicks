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
    private final String[] leaderBoards;
    private static final int START_LEVEL = 0;
    private static final int END_LEVEL = 9;

    /** Basic constructor that depends on the current game context. */
    public SaveStatsVisitor(GameContext gameContext) {
        this.gameContext = gameContext;
        this.gameStatsViewModel = ViewModelProviders.of(gameContext.getActivity()).get(GameStatsViewModel.class);
        leaderBoards = gameContext.getActivity().getResources().getStringArray(R.array.leaderBoards);
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
        for (int i = START_LEVEL; i < END_LEVEL; i++) {
            if (levelGameMode.getLevel() == i) {
                submitScore(leaderBoards[i], account);
            }
        }

        Toast toast = Toast.makeText(gameContext.getActivity(),
                gameContext.getActivity().getResources().getString(R.string.score_submitted_message), Toast.LENGTH_SHORT);
        toast.show();
        return null;
    }

    private void submitScore(String leaderBoardID, GoogleSignInAccount account) {
        Games.getLeaderboardsClient(gameContext.getActivity(), account)
                .submitScore(leaderBoardID, gameContext.getStepsCount());
    }
}