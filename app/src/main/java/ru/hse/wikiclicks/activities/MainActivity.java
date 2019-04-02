package ru.hse.wikiclicks.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.MainController;

public class MainActivity extends AppCompatActivity {
    private int stepsCount = -1; // magic
    private String finishId;
    private TextView stepsTextView;
    private WebView webView;

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
        stepsTextView = findViewById(R.id.stepsTextView);
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
        }
    }
}
