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
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.MainController;

public class MainActivity extends AppCompatActivity {
    private int stepsCount = -1;
    private String finishId;
    private String finishTitle;
    private TextView stepsTextView;
    private WebView webView;
    private Chronometer chronometer;
    private SharedPreferences sharedPreferences;
    private String finishURL;
    private static final String FINISH_TITLE_KEY = "finish_title";

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
            finishTitle = extras.getString(FINISH_TITLE_KEY);
            finishURL = MainController.getURLForId(finishId);
            System.out.println(extras.getString("startid"));
            System.out.println(finishId);
            webView.loadUrl(MainController.getPageLinkById(extras.getString("startid")));
        }

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
            if (!MainController.isCorrectWikipediaLink(url)) {
                Toast toast = Toast.makeText(getApplicationContext(), "URL should lead to english wiki page", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            stepsCount++;
            stepsTextView.setText("Steps: " + stepsCount);
            if (url.equals(finishURL)) {
                chronometer.stop();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("You win!");
                builder.setMessage("Do you want to start a new game?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent getStartIntent = new Intent(MainActivity.this, GetStartActivity.class);
                        startActivity(getStartIntent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainMenuIntent = new Intent(MainActivity.this, MainMenuActivity.class);
                        startActivity(mainMenuIntent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void setUpToolBar() {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        TextView finishTitleTextView = findViewById(R.id.tv_finish);
        finishTitleTextView.setText("Target: " + finishTitle);
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
