package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.CustomGameMode;
import ru.hse.wikiclicks.controllers.GameMode;
import ru.hse.wikiclicks.controllers.GameModeFactory;
import ru.hse.wikiclicks.controllers.LevelGameMode;
import ru.hse.wikiclicks.controllers.OfflineController;
import ru.hse.wikiclicks.controllers.StepsGameMode;
import ru.hse.wikiclicks.controllers.TimeGameMode;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class OfflineGameActivity extends AppCompatActivity {
    private WebView webView;

    private GameStatsViewModel gameStatsViewModel;
    private BookmarkViewModel bookmarkViewModel;
    private int stepsCount = -1;
    private String startTitle;
    private String finishTitle;
    private TextView stepsTextView;
    private Chronometer chronometer;
    private GameMode gameMode;
    private ImageButton exitButton;
    private ImageButton bookmarkButton;
    private long milliseconds;
    private String currentUrl = "";

    private GameStatsViewModel gamesViewModel;
    ArrayList<String> titleTree = new ArrayList<>();
    private String directory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);
        bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        readExtras();
        setUpToolBar();
        setUpStepsCounter(gameMode.stepsModeEnabled());
        setUpChronometer(gameMode.timeModeEnabled());
        setUpExitButton();
        setUpBookmarkButton();
        setUpWebView();
    }

    @Override
    public void onBackPressed() {
        if (titleTree.size() > 1) {
            titleTree.remove(titleTree.size() - 1);
            String lastTitle = titleTree.get(titleTree.size() - 1);
            String webPage = "";
            try {
                webPage = OfflineController.readPage(lastTitle);
            } catch (IOException e) {
                e.printStackTrace();
            }
            webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
        } else {
            super.onBackPressed();
        }
    }

    private void setUpWebView() {
        webView = findViewById(R.id.offline_webview);
        webView.setWebViewClient(new OfflineWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        directory = OfflineController.getOutputDirectory();

        titleTree.add(startTitle);
        String webPage = "";
        try {
            webPage = OfflineController.readPage(startTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
    }

    private class OfflineWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String title = url.replace("https://wiki/", "");
                String webPage = OfflineController.readPage(title);
                titleTree.add(title);
                currentUrl = WikiController.getUrlForTitle(title);
                webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This page is too far away from destination and has not been downloaded.", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText(getString(R.string.steps, stepsCount));
            if (OfflineController.normalize(WikiController.getPageTitleFromUrl(currentUrl)).
                    equals(OfflineController.normalize(finishTitle))) {
                chronometer.stop();
                milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
                addDatabaseEntry();
                AlertDialog dialog = getNewWinDialog();
                dialog.show();
            }
        }
    }
    private void setUpExitButton() {
        exitButton = findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = getNewExitDialog();
                dialog.show();
            }
        });
    }

    private void setUpBookmarkButton() {
        bookmarkButton = findViewById(R.id.button_bookmark);
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                if (stepsCount == 0) { // TODO ubrat' etot costyl'
                    title = startTitle;
                } else {
                    title = WikiController.getPageTitleFromUrl(currentUrl);
                }
                ru.hse.wikiclicks.database.Bookmarks.Bookmark bookmark = new ru.hse.wikiclicks.database.Bookmarks.Bookmark(currentUrl, title);
                bookmarkViewModel.insert(bookmark);
                Toast toast = Toast.makeText(getApplicationContext(), "Bookmark for " + title + " added", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private AlertDialog getNewWinDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OfflineGameActivity.this);
        builder.setTitle("You win!");
        builder.setMessage(getWinMessage() + System.lineSeparator() + "Do you want to start a new game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent getEndpointsIntent = new Intent(OfflineGameActivity.this, GetEndpointsActivity.class);
                getEndpointsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(getEndpointsIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent mainMenuIntent = new Intent(OfflineGameActivity.this, MainMenuActivity.class);
                mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainMenuIntent);
            }
        });
        return builder.create();
    }

    private AlertDialog getNewExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OfflineGameActivity.this);
        builder.setTitle("Do you want to finish this game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent mainMenuIntent = new Intent(OfflineGameActivity.this, MainMenuActivity.class);
                mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainMenuIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        return builder.create();
    }

    private void readExtras() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        startTitle = extras.getString(GetEndpointsActivity.START_TITLE_KEY);
        String gameModeString = extras.getString(SelectModeActivity.GAME_MODE_KEY);
        assert gameModeString != null;
        int level = extras.getInt(ChallengesActivity.LEVEL_KEY);
        gameMode = GameModeFactory.getGameMode(gameModeString, level, this);
    }

    private void setUpToolBar() {
        Toolbar myToolbar = findViewById(R.id.offline_toolbar);
        setSupportActionBar(myToolbar);
        TextView finishTitleTextView = findViewById(R.id.offline_tv_finish);
        finishTitleTextView.setText(getString(R.string.target, finishTitle));
    }


    private void setUpStepsCounter(boolean enabled) {
        stepsTextView = findViewById(R.id.offline_steps);
        if (!enabled) {
            stepsTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpChronometer(boolean enabled) {
        chronometer = findViewById(R.id.offline_chronometer);
        if (!enabled) {
            chronometer.setVisibility(View.INVISIBLE);
        }
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private String getWinMessage() { // TODO replace this with smth more adequate
        if (gameMode instanceof TimeGameMode) {
            return "Your time is " + getTimeFromChronometer();
        }

        if (gameMode instanceof StepsGameMode) {
            return "Your steps count is " + stepsCount;
        }

        if (gameMode instanceof CustomGameMode) {
            String result = "";
            if (gameMode.timeModeEnabled()) {
                result += "Your time is " + getTimeFromChronometer();
            }
            if (gameMode.stepsModeEnabled()) {
                if (!result.equals("")) {
                    result += "\n";
                }
                result += "Your steps count is " + stepsCount;
            }
            return result;
        }
        if (gameMode instanceof  LevelGameMode) {
            return "Your steps count is " + stepsCount;
        }

        throw new AssertionError("Wrong game mode");
    }

    private String getTimeFromChronometer() {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void addDatabaseEntry() { // TODO make it more adequate
        if (gameMode instanceof TimeGameMode) {
            GameStats gameStats = new GameStats(milliseconds, startTitle, finishTitle, true);
            gameStatsViewModel.insert(gameStats);
        } else if (gameMode instanceof StepsGameMode) {
            GameStats gameStats = new GameStats(stepsCount, startTitle, finishTitle, false);
            gameStatsViewModel.insert(gameStats);
        } else if (gameMode instanceof LevelGameMode) {
            LevelGameMode levelGameMode = (LevelGameMode) gameMode;
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if (account == null) {
                Toast toast = Toast.makeText(this, "account is null", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
            if (levelGameMode.getLevel() == 1) {
                Games.getLeaderboardsClient(this, account)
                        .submitScore(getString(R.string.leaderboard_level_1), stepsCount);
            }
            if (levelGameMode.getLevel() == 2) {
                Games.getLeaderboardsClient(this, account)
                        .submitScore(getString(R.string.leaderboard_level_2), stepsCount);
            }

            if (levelGameMode.getLevel() == 3) {
                Games.getLeaderboardsClient(this, account)
                        .submitScore(getString(R.string.leaderboard_level_3), stepsCount);
            }
        }
    }

    private boolean banCountriesEnabled() {
        return gameMode.banCountriesEnabled();
    }

    private boolean banYearsEnabled() {
        return gameMode.banYearsEnabled();
    }
}
