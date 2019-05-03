package ru.hse.wikiclicks.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.GameMode;
import ru.hse.wikiclicks.controllers.GameModeFactory;
import ru.hse.wikiclicks.controllers.StepsGameMode;
import ru.hse.wikiclicks.controllers.TimeGameMode;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.database.GamesRepository;
import ru.hse.wikiclicks.database.GamesViewModel;
import ru.hse.wikiclicks.database.TimeModeGame;

public class GameActivity extends AppCompatActivity {
    private final GamesViewModel gamesViewModel = ViewModelProviders.of(this).get(GamesViewModel.class);
    private int stepsCount = -1;
    private String finishId;
    private String startId;
    private String finishTitle;
    private TextView stepsTextView;
    private WebView webView;
    private Chronometer chronometer;
    private GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        readExtras();
        setUpWebView();
        setUpToolBar();
        setUpStepsCounter(gameMode.stepsModeEnabled());
        setUpChronometer(gameMode.timeModeEnabled());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
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
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText(getString(R.string.steps, stepsCount));
            if (finishId.equals(WikiController.getPageFromUrl(url).getId())) {
                chronometer.stop();
                addDatabaseEntry();
                AlertDialog dialog = getNewWinDialog();
                dialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName ('header-container header-chrome')[0].style.display='none';"
                    + "})()");
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

    private void setUpWebView() {
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WikiWebViewClient());
        webView.loadUrl(WikiController.getPageLinkById(startId));
    }

    private void readExtras() {
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        finishId = extras.getString(GetEndpointsActivity.FINISH_ID_KEY);
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        finishId = WikiController.getRedirectedId(finishId);
        startId = extras.getString(GetEndpointsActivity.START_ID_KEY);
        String gameModeString = extras.getString(SelectModeActivity.GAME_MODE_KEY);
        assert gameModeString != null;
        gameMode = GameModeFactory.getGameMode(gameModeString);
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
            return "Your time is " + chronometer.getText();
        }

        if (gameMode instanceof StepsGameMode) {
            return "Your steps count is" + stepsCount;
        }
        throw new AssertionError("Wrong game mode");
    }

    private void addDatabaseEntry() {
        if (gameMode instanceof TimeGameMode) {
            TimeModeGame timeModeGame = new TimeModeGame(chronometer.getText().toString());
            gamesViewModel.insert(timeModeGame);
        }
    }
}
