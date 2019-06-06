package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.GameContext;
import ru.hse.wikiclicks.controllers.GetWinMessageVisitor;
import ru.hse.wikiclicks.controllers.OfflineController;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.controllers.modes.GameMode;
import ru.hse.wikiclicks.controllers.modes.StepsGameMode;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;
import ru.hse.wikiclicks.database.GameStats.GameStats;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class OfflineGameActivity extends AppCompatActivity {
    private WebView webView;

    private GameStatsViewModel gameStatsViewModel;
    private BookmarkViewModel bookmarkViewModel;
    private String startTitle;
    private String finishTitle;
    private Chronometer chronometer;
    private ImageButton exitButton;
    private ImageButton bookmarkButton;
    private final GameMode gameMode = new StepsGameMode();
    private long milliseconds;
    private String currentUrl = "";

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
        setUpChronometer();
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
                webPage = OfflineController.readPage(lastTitle, directory);
            } catch (IOException e) {
                Log.e("Reload old page error", e.getMessage());
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
        directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();

        titleTree.add(startTitle);
        currentUrl = WikiController.getUrlForTitle(startTitle);
        String webPage = "";
        try {
            webPage = OfflineController.readPage(startTitle, directory);
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "This offline game has not been downloaded.", Toast.LENGTH_LONG);
            toast.show();
            this.finish();
        }
        webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
    }

    private class OfflineWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String title = url.replace("https://wiki/", "");
                String webPage = OfflineController.readPage(title, directory);
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
                String title = WikiController.getPageTitleFromUrl(currentUrl);
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
                Intent getEndpointsIntent = new Intent(OfflineGameActivity.this, OfflineLevelsActivity.class);
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
    }

    private void setUpToolBar() {
        Toolbar myToolbar = findViewById(R.id.offline_toolbar);
        setSupportActionBar(myToolbar);
        TextView finishTitleTextView = findViewById(R.id.offline_tv_finish);
        finishTitleTextView.setText(getString(R.string.target, finishTitle));
    }

    private void setUpChronometer() {
        chronometer = findViewById(R.id.offline_chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private String getWinMessage() {
        GetWinMessageVisitor getWinMessageVisitor = new GetWinMessageVisitor(new GameContext(0, milliseconds, this, startTitle, finishTitle));
        return gameMode.accept(getWinMessageVisitor);
    }

    private void addDatabaseEntry() {
        GameStats gameStats = new GameStats(milliseconds, startTitle, finishTitle, true);
        gameStatsViewModel.insert(gameStats);
    }
}
