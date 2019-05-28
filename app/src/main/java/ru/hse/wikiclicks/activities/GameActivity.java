package ru.hse.wikiclicks.activities;

import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.CustomGameMode;
import ru.hse.wikiclicks.controllers.GameMode;
import ru.hse.wikiclicks.controllers.GameModeFactory;
import ru.hse.wikiclicks.controllers.StepsGameMode;
import ru.hse.wikiclicks.controllers.TimeGameMode;
import ru.hse.wikiclicks.controllers.BanController;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class GameActivity extends AppCompatActivity {
    private GameStatsViewModel gameStatsViewModel;
    private BookmarkViewModel bookmarkViewModel;
    private int stepsCount = -1;
    private String finishId;
    protected String startId;
    private String startTitle;
    private String finishTitle;
    private TextView stepsTextView;
    protected WebView webView;
    private Chronometer chronometer;
    private GameMode gameMode;
    private ImageButton exitButton;
    private ImageButton bookmarkButton;
    private long milliseconds;
    private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameStatsViewModel = ViewModelProviders.of(this).get(GameStatsViewModel.class);
        bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        setContentView(R.layout.activity_game);
        readExtras();
        setUpWebView();
        setUpToolBar();
        setUpStepsCounter(gameMode.stepsModeEnabled());
        setUpChronometer(gameMode.timeModeEnabled());
        setUpExitButton();
        setUpBookmarkButton();
    }

    @Override
    public void onBackPressed() {
        if (gameMode.banBackEnabled()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Back press is banned in this mode", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }


    private class WikiWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!WikiController.isCorrectWikipediaLink(url)) {
                Toast toast = Toast.makeText(getApplicationContext(), "URL should lead to english wiki page", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            return catchEnforcedBans(url) || super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText(getString(R.string.steps, stepsCount));
            if (finishId.equals(WikiController.getPageFromUrl(url).getId())) {
                chronometer.stop();
                milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
                addDatabaseEntry();
                AlertDialog dialog = getNewWinDialog();
                dialog.show();
            }
            currentUrl = url;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName ('header-container header-chrome')[0].style.display='none';"
                    + "})()");
        }

        private boolean catchEnforcedBans(String url) {
            if (!banCountriesEnabled() && banYearsEnabled()) {
                return false;
            }
            if (finishId.equals(WikiController.getPageFromUrl(url).getId())) {
                return false; //finish is correct, no matter what
            }
            BanController urlController = new BanController(url);
            if (banCountriesEnabled() && urlController.isCountry()) {
                Toast toast = Toast.makeText(getApplicationContext(), "URL should not lead to country", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            if (banYearsEnabled() && urlController.isYear()) {
                Toast toast = Toast.makeText(getApplicationContext(), "URL should not lead to year", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            return false;
        }

        private AlertDialog getNewWinDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
            builder.setTitle("You win!");
            builder.setMessage(getWinMessage() + System.lineSeparator() + "Do you want to start a new game?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent getEndpointsIntent = new Intent(GameActivity.this, GetEndpointsActivity.class);
                    getEndpointsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(getEndpointsIntent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent mainMenuIntent = new Intent(GameActivity.this, MainMenuActivity.class);
                    mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainMenuIntent);
                }
            });
            return builder.create();
        }
    }

    protected void setUpWebView() {
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WikiWebViewClient());
        webView.loadUrl(WikiController.getPageLinkById(startId));
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
                ru.hse.wikiclicks.database.Bookmarks.Bookmark bookmark = new ru.hse.wikiclicks.database.Bookmarks.Bookmark(currentUrl, currentUrl);
                bookmarkViewModel.insert(bookmark);
            }
        });
    }

    private AlertDialog getNewExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Do you want to finish this game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent mainMenuIntent = new Intent(GameActivity.this, MainMenuActivity.class);
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
        finishId = extras.getString(GetEndpointsActivity.FINISH_ID_KEY);
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        finishId = WikiController.getRedirectedId(finishId);
        startId = extras.getString(GetEndpointsActivity.START_ID_KEY);
        startTitle = extras.getString(GetEndpointsActivity.START_TITLE_KEY);
        String gameModeString = extras.getString(SelectModeActivity.GAME_MODE_KEY);
        assert gameModeString != null;
        gameMode = GameModeFactory.getGameMode(gameModeString, this);
    }

    private void setUpToolBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView finishTitleTextView = findViewById(R.id.tv_finish);
        finishTitleTextView.setText(getString(R.string.target, finishTitle));
    }

    private void setUpStepsCounter(boolean enabled) {
        stepsTextView = findViewById(R.id.tv_steps);
        if (!enabled) {
            stepsTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpChronometer(boolean enabled) {
        chronometer = findViewById(R.id.chronometer);
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
        }
    }

    private boolean banCountriesEnabled() {
        return gameMode.banCountriesEnabled();
    }

    private boolean banYearsEnabled() {
        return gameMode.banYearsEnabled();
    }
}
