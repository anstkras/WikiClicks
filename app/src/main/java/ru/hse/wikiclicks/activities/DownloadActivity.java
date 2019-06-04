package ru.hse.wikiclicks.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.DownloadService;
import ru.hse.wikiclicks.controllers.OfflineController;

public class DownloadActivity extends AppCompatActivity {
    private int levelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        levelNumber = extras.getInt(DownloadLevelsActivity.OFFLINE_LEVEL_NUMBER_KEY);

        DownloadService downloadService = new DownloadService();
        ResultReceiver receiver = new DownloadReceiver(new Handler());
        downloadService.enqueueWork(DownloadActivity.this, getIntent(), receiver);
    }

    private void handleDownloadFinished(String result) {
        TextView text = findViewById(R.id.download_text);
        text.setText(getString(R.string.download_finished));


        String downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File hasDownloadHappened = new File(downloadDirectory, OfflineController.CONFIRMATION + levelNumber);
        if (!hasDownloadHappened.exists()) {
            handleDownloadFailed();
        } else {
            handleDownloadSucceeded(result);
        }
    }

    private void handleDownloadSucceeded(final String result) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
        builder.setTitle("Download successful");
        builder.setMessage(result + " Do you want to download another level?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent downloadIntent = new Intent(DownloadActivity.this, DownloadLevelsActivity.class);
                downloadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(downloadIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent returnIntent = new Intent(DownloadActivity.this, MainMenuActivity.class);
                returnIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(returnIntent);
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
        } catch (Exception e) {
            Log.e("Dialog showing failed", e.getMessage());
        }
    }

    private void handleDownloadFailed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
        builder.setTitle("Download failed");
        builder.setMessage("Downloading the level failed. Please try again.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent downloadIntent = new Intent(DownloadActivity.this, DownloadLevelsActivity.class);
                downloadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(downloadIntent);
            }
        });
        AlertDialog dialog = builder.create();
        try {
            dialog.show();
        } catch (Exception e) {
            Log.e("Dialog showing failed", e.getMessage());
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == DownloadService.JOB_ID) {
                String result = resultData.getString("data");
                handleDownloadFinished(result);
            }
        }
    }
}
