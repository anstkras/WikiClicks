package ru.hse.wikiclicks.activities;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
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
import ru.hse.wikiclicks.controllers.modes.GameContext;
import ru.hse.wikiclicks.controllers.modes.GetNewGameVisitor;
import ru.hse.wikiclicks.controllers.modes.GetWinMessageVisitor;
import ru.hse.wikiclicks.controllers.modes.SaveStatsVisitor;
import ru.hse.wikiclicks.controllers.modes.GameMode;
import ru.hse.wikiclicks.controllers.wiki.BanController;
import ru.hse.wikiclicks.controllers.wiki.WikiController;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;

/** Activity for the main game process. */
public class GameActivity extends AppCompatActivity {
    private BookmarkViewModel bookmarkViewModel;
    private int stepsCount = -1;
    private boolean hasGameEnded;
    private String startTitle;
    private String finishTitle;
    private String finishPageId;
    private TextView stepsTextView;
    private WebView webView;
    private Chronometer chronometer;
    private GameMode gameMode;
    private long milliseconds;
    private String currentUrl;

    /** Creates the game, initializes the start and end points. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        setContentView(R.layout.activity_game);
        readExtras();
        setUpWebView();
        setUpToolBar();
        setUpStepsCounter(gameMode.stepsModeEnabled());
        setUpChronometer(gameMode.timeModeEnabled());
        setUpPlaceholderText(gameMode.stepsModeEnabled() || gameMode.timeModeEnabled());
        setUpExitButton();
        setUpBookmarkButton();
        currentUrl = WikiController.getUrlForTitle(startTitle);
        hasGameEnded = false;
    }


    /** Reloads the previous page, unless the current page was the first one loaded or ban mode is enabled. */
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

    /** WebViewClient for game that formats page to remove search bar and checks game process state. */
    private class WikiWebViewClient extends WebViewClient {
        /** Blocks loading if url is not to Wikipedia page. */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!WikiController.isCorrectWikipediaLink(url)) {
                Toast toast = Toast.makeText(getApplicationContext(), "URL should lead to english wiki page", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            return catchEnforcedBans(url) || super.shouldOverrideUrlLoading(view, url);
        }

        /** Method that checks whether the game has been won and processes the winning state. */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText(getString(R.string.steps, stepsCount));
            if (!hasGameEnded && finishPageId.equals(WikiController.getPageFromUrl(url).getId())) {
                hasGameEnded = true;
                chronometer.stop();
                milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
                addDatabaseEntry();
                AlertDialog dialog = getNewWinDialog();
                dialog.show();
            }
            currentUrl = url;
        }

        /** Removes the search bar from the top of the screen. */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName ('header-container header-chrome')[0].style.display='none';"
                    + "})()");
        }

        private boolean catchEnforcedBans(String url) {
            if (!banCountriesEnabled() && !banYearsEnabled()) {
                return false;
            }
            if (finishPageId.equals(WikiController.getPageFromUrl(url).getId())) {
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
            return builder.setTitle("You win!")
                    .setMessage(getWinMessage() + System.lineSeparator() + "Do you want to start a new game?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(GameActivity.this, gameMode.accept(GetNewGameVisitor.getInstance()));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent mainMenuIntent = new Intent(GameActivity.this, MainMenuActivity.class);
                            mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainMenuIntent);
                        }
                    })
                    .create();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WikiWebViewClient());
        webView.loadUrl(WikiController.getUrlForTitle(startTitle));
    }

    private void setUpExitButton() {
        final ImageButton exitButton = findViewById(R.id.button_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = getNewExitDialog();
                dialog.show();
            }
        });
    }

    private void setUpBookmarkButton() {
        final ImageButton bookmarkButton = findViewById(R.id.button_bookmark);
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = WikiController.getPageTitleFromUrl(currentUrl);
                ru.hse.wikiclicks.database.Bookmarks.Bookmark bookmark = new ru.hse.wikiclicks.database.Bookmarks.Bookmark(currentUrl, title);
                bookmarkViewModel.insert(bookmark);
                Toast toast = Toast.makeText(getApplicationContext(), "Bookmark for " + title + " added", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private AlertDialog getNewExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        return builder.setTitle("Do you want to finish this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainMenuIntent = new Intent(GameActivity.this, MainMenuActivity.class);
                        mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainMenuIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
    }

    private void readExtras() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;

        finishPageId = extras.getString(GetEndpointsActivity.FINISH_ID_KEY);
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        finishPageId = WikiController.getRedirectedId(finishPageId);
        startTitle = extras.getString(GetEndpointsActivity.START_TITLE_KEY);

        gameMode = extras.getParcelable(SelectModeActivity.GAME_MODE_KEY);
        assert gameMode != null;
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
            stepsTextView.setVisibility(View.GONE);
        }
    }

    private void setUpChronometer(boolean enabled) {
        chronometer = findViewById(R.id.chronometer);
        if (!enabled) {
            chronometer.setVisibility(View.GONE);
        }
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void setUpPlaceholderText(boolean isInfo) {
        if (!isInfo) {
            TextView placeholder = findViewById(R.id.placeholder);
            placeholder.setVisibility(View.VISIBLE);
        }
    }

    private String getWinMessage() {
        GetWinMessageVisitor getWinMessageVisitor = new GetWinMessageVisitor(new GameContext(stepsCount, milliseconds, this, startTitle, finishTitle));
        return gameMode.accept(getWinMessageVisitor);
    }

    private void addDatabaseEntry() {
        SaveStatsVisitor saveStatsVisitor = new SaveStatsVisitor(new GameContext(stepsCount, milliseconds, this, startTitle, finishTitle));
        gameMode.accept(saveStatsVisitor);
    }

    private boolean banCountriesEnabled() {
        return gameMode.banCountriesEnabled();
    }

    private boolean banYearsEnabled() {
        return gameMode.banYearsEnabled();
    }
}
