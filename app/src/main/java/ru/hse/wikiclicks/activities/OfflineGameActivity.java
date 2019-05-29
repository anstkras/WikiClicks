package ru.hse.wikiclicks.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.ChooseOfflineGame;
import ru.hse.wikiclicks.controllers.OfflineController;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.database.GameStats.GameStatsViewModel;

public class OfflineGameActivity extends AppCompatActivity {
    private WebView webView;

    private GameStatsViewModel gamesViewModel;
    private int stepsCount = -1;
    private String finishId;
    private String startId;
    ArrayList<String> titleTree = new ArrayList<>();
    private String directory;
    private String finishTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        try {
            setUpWebView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setUpToolBar();
    }

    @Override
    public void onBackPressed() {
        if (titleTree.size() > 1) {
            titleTree.remove(titleTree.size() - 1);
            String lastTitle = titleTree.get(titleTree.size() - 1);
            String webPage = "";
            try {
                webPage = FileUtils.readFileToString(new File(directory, lastTitle), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
        } else {
            super.onBackPressed();
        }
    }

    private void setUpWebView() throws IOException {

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new OfflineWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        directory = getFilesDir().getAbsolutePath() + "/wiki/";

        ChooseOfflineGame game = new ChooseOfflineGame();
        game.chooseRandomStartPage();

        OfflineController controller = new OfflineController(game, directory);

        finishTitle = WikiController.getPageFromUrl(game.endPageUrl).getTitle();
        finishId = WikiController.getPageFromUrl(game.endPageUrl).getId();

        String startTitle = OfflineController.normalize(game.startPageName);
        titleTree.add(startTitle);
        String webPage = FileUtils.readFileToString(new File(directory,  startTitle), "UTF-8");
        webView.loadDataWithBaseURL("", webPage, "text/html", "UTF-8", "");
    }

    private class OfflineWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                String title = OfflineController.normalize(url.replace("https://wiki/", ""));
                String webPage = FileUtils.readFileToString(new File(directory, title), "UTF-8");
                titleTree.add(title);
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
//            stepsTextView.setText(getString(R.string.steps, stepsCount));
        }
    }


    private void setUpToolBar() {
        TextView finishTitleTextView = findViewById(R.id.tv_finish);
        finishTitleTextView.setText(getString(R.string.target, finishTitle));
    }
}
