package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.download.DownloadController;
import ru.hse.wikiclicks.controllers.modes.GameContext;
import ru.hse.wikiclicks.controllers.modes.GetWinMessageVisitor;
import ru.hse.wikiclicks.controllers.modes.SaveStatsVisitor;
import ru.hse.wikiclicks.controllers.wiki.WikiController;
import ru.hse.wikiclicks.controllers.modes.GameMode;
import ru.hse.wikiclicks.controllers.modes.TimeGameMode;
import ru.hse.wikiclicks.database.Bookmarks.BookmarkViewModel;

/** Activity for the offline game process. */
public class OfflineGameActivity extends AppCompatActivity {
    private static final String MIME_TYPE = "text/html";
    private static final String ENCODING = "UTF-8";

    private WebView webView;

    private BookmarkViewModel bookmarkViewModel;
    private String startTitle;
    private String finishTitle;
    private Chronometer chronometer;
    private final GameMode gameMode = TimeGameMode.getInstance();
    private long milliseconds;
    private String currentUrl = "";

    private List<String> titleTree = new ArrayList<>();
    private String directory;
    private boolean hasGameEnded;

    /** Creates the offline game, initializes the start and end points. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        readExtras();
        setUpToolBar();
        setUpChronometer();
        setUpExitButton();
        setUpBookmarkButton();
        setUpWebView();
        hasGameEnded = false;
    }

    /** Reloads the previous page, unless the current page was the first one loaded. */
    @Override
    public void onBackPressed() {
        if (titleTree.size() > 1) {
            titleTree.remove(titleTree.size() - 1);
            String lastTitle = titleTree.get(titleTree.size() - 1);
            String webPage = "";
            try {
                webPage = DownloadController.readPage(lastTitle, directory);
            } catch (IOException e) {
                Log.e("Reload old page error", e.getMessage());
            }
            webView.loadDataWithBaseURL("", webPage, MIME_TYPE, ENCODING, "");
        } else {
            super.onBackPressed();
        }
    }

    private void setUpWebView() {
        webView = findViewById(R.id.offline_webview);
        webView.setWebViewClient(new OfflineWebViewClient());
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        titleTree.add(startTitle);
        String webPage = "";
        try {
            webPage = DownloadController.readPage(startTitle, directory);
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "This offline game has not been downloaded.", Toast.LENGTH_SHORT);
            toast.show();
            this.finish();
        }
        webView.loadDataWithBaseURL("", webPage, MIME_TYPE, ENCODING, "");
    }

    /** WebViewClient for offline game that always overrides link loadings and loads external file instead. */
    private class OfflineWebViewClient extends WebViewClient {
        /** Always returns true and loads an external file manually. */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String title = WikiController.getPageTitleFromUrl(url);
                String webPage = DownloadController.readPage(title, directory);
                titleTree.add(title);
                currentUrl = WikiController.getUrlForTitle(title);
                webView.loadDataWithBaseURL("", webPage, MIME_TYPE, ENCODING, "");
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This page is too far away from destination and has not been downloaded.", Toast.LENGTH_SHORT);
                toast.show();
            }
            return true;
        }

        /** Method that checks whether the game has been won and processes the winning state. */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!hasGameEnded && WikiController.getPageTitleFromUrl(currentUrl).equals(finishTitle)) {
                hasGameEnded = true;
                chronometer.stop();
                milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
                addDatabaseEntry();
                AlertDialog dialog = getNewWinDialog();
                dialog.show();
            }
        }
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

    private AlertDialog getNewWinDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OfflineGameActivity.this);
        return builder.setTitle("You win!")
                .setMessage(getWinMessage() + System.lineSeparator() + "Do you want to start a new game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent getEndpointsIntent = new Intent(OfflineGameActivity.this, OfflineLevelsActivity.class);
                        getEndpointsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(getEndpointsIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainMenuIntent = new Intent(OfflineGameActivity.this, MainMenuActivity.class);
                        mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainMenuIntent);
                    }
                })
                .create();
    }

    private AlertDialog getNewExitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(OfflineGameActivity.this);
        return builder.setTitle("Do you want to finish this game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent mainMenuIntent = new Intent(OfflineGameActivity.this, MainMenuActivity.class);
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
        finishTitle = extras.getString(GetEndpointsActivity.FINISH_TITLE_KEY);
        startTitle = extras.getString(GetEndpointsActivity.START_TITLE_KEY);
        directory = extras.getString(OfflineLevelsActivity.OFFLINE_DIRECTORY_KEY);
        currentUrl = WikiController.getUrlForTitle(startTitle);
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
        SaveStatsVisitor saveStatsVisitor = new SaveStatsVisitor(new GameContext(0, milliseconds, this, startTitle, finishTitle));
        gameMode.accept(saveStatsVisitor);
    }
}
