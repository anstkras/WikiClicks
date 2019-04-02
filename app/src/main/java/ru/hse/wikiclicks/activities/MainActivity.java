package ru.hse.wikiclicks.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.TextView;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.MainController;

public class MainActivity extends AppCompatActivity {
    private int stepsCount = -1; // magic
    private String finishId;
    private TextView stepsTextView;
    private WebView webView;
    private Chronometer chronometer;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WikiWebViewClient());
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            webView.loadUrl(MainController.getRandomPageLink());
        } else {
            finishId = extras.getString("finishid");
            System.out.println(extras.getString("startid"));
            System.out.println(finishId);
            webView.loadUrl(MainController.getPageLinkById(extras.getString("startid")));
        }
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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
            if (!MainController.isCorrectWikipediaLink(url)) {
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText("Steps: " + stepsCount);
        }
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
