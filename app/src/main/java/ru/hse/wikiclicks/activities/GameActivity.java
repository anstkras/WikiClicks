package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import ru.hse.wikiclicks.controllers.WikiController;

public class GameActivity extends AppCompatActivity {
    private int stepsCount = -1;
    private String finishId;
    private String startId;
    private String finishTitle;
    private TextView stepsTextView;
    private WebView webView;
    private Chronometer chronometer;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        readExtras();
        setUpWebView();
        setUpToolBar();
        initializeSharedPreferences();
        setUpStepsCounter(stepsModeEnabled());
        setUpChronometer(timeModeEnabled());
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("You win!");
                builder.setMessage("Do you want to start a new game?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent getStartIntent = new Intent(GameActivity.this, GetEndpointsActivity.class);
                        startActivity(getStartIntent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainMenuIntent = new Intent(GameActivity.this, MainMenuActivity.class);
                        startActivity(mainMenuIntent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName ('header-container header-chrome')[0].style.display='none';"
                    +"})()");
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
        finishId = extras.getString(GetEndpointsActivity.FINISH_ID_KEY);
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        finishId = WikiController.getRedirectedId(finishId);
        startId = extras.getString(GetEndpointsActivity.START_ID_KEY);
    }

    private void setUpToolBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView finishTitleTextView = findViewById(R.id.tv_finish);
        finishTitleTextView.setText(getString(R.string.target, finishTitle));
    }

    private void initializeSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    private boolean timeModeEnabled() {
        return sharedPreferences.getBoolean("pref_time_mode", true);
    }

    private boolean stepsModeEnabled() {
        return sharedPreferences.getBoolean("pref_steps_mode", true);
    }
}
