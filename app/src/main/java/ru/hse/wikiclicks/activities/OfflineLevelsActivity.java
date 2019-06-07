package ru.hse.wikiclicks.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.download.DownloadService;
import ru.hse.wikiclicks.controllers.download.OfflineController;

public class OfflineLevelsActivity extends AppCompatActivity {
    public final static int NOTIFICATION_ID = 179;
    public final static String CHANNEL_NOTIFICATION_ID = "wikiclicks_offline_download";

    public static final String OFFLINE_FINISH_TITLE_KEY = "offline_finish_title";
    public static final String OFFLINE_START_TITLE_KEY = "offline_start_title";
    public static final String OFFLINE_STEPS_TREE_SIZE_KEY = "offline_steps_tree_size";
    public static final String OFFLINE_DIRECTORY_KEY = "offline_directory";
    public static final String OFFLINE_LEVEL_NUMBER_KEY = "offline_level_number";

    final static String[] offlineLevelStartPages = {"Vanity Fair (novel)", "Lock picking", "Coffeemaker",
            "Moscow", "Invisible Pink Unicorn", "Sexuality of Adolf Hitler",
            "Lady Justice", "Butte",  "Ritchie Blackmore", "Singin' in the Rain"};
    final static String[] offlineLevelEndPages = {"Star Wars", "Harry Potter", "Hello, World",
            "Prada", "Game of Thrones", "Squatting",
            "Seafood", "Seven Wonders of the Ancient World",  "Hell Station",  "4'33\""};
    final static int[] offlineLevelTreeSizes = {2, 2, 2, 1, 2, 2, 2, 2, 3, 3};

    public final static int LEVEL0 = 0;
    public final static int LEVEL1 = 1;
    public final static int LEVEL2 = 2;
    public final static int LEVEL3 = 3;
    public final static int LEVEL4 = 4;
    public final static int LEVEL5 = 5;
    public final static int LEVEL6 = 6;
    public final static int LEVEL7 = 7;
    public final static int LEVEL8 = 8;
    public final static int LEVEL9 = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_levels);

        createButtonForLevel((Button)findViewById(R.id.level0), LEVEL0);
        createButtonForLevel((Button)findViewById(R.id.level1), LEVEL1);
        createButtonForLevel((Button)findViewById(R.id.level2), LEVEL2);
        createButtonForLevel((Button)findViewById(R.id.level3), LEVEL3);
        createButtonForLevel((Button)findViewById(R.id.level4), LEVEL4);
        createButtonForLevel((Button)findViewById(R.id.level5), LEVEL5);
        createButtonForLevel((Button)findViewById(R.id.level6), LEVEL6);
        createButtonForLevel((Button)findViewById(R.id.level7), LEVEL7);
        createButtonForLevel((Button)findViewById(R.id.level8), LEVEL8);
        createButtonForLevel((Button)findViewById(R.id.level9), LEVEL9);


        createDownloadButtonForLevel((Button)findViewById(R.id.download_level0), LEVEL0);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level1), LEVEL1);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level2), LEVEL2);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level3), LEVEL3);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level4), LEVEL4);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level5), LEVEL5);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level6), LEVEL6);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level7), LEVEL7);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level8), LEVEL8);
        createDownloadButtonForLevel((Button)findViewById(R.id.download_level9), LEVEL9);
    }

    private void createButtonForLevel(final Button button, final int level) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setTextColor(getResources().getColor(R.color.colorUsed));
                startGame(level);
            }
        });
    }

    private void createDownloadButtonForLevel(final Button button, final int level) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setTextColor(getResources().getColor(R.color.colorUsed));
                downloadLevel(level);
            }
        });
    }

    private void startGame(int levelNumber) {
        if (!doesLevelExist(levelNumber)) {
            Toast toast = Toast.makeText(getApplicationContext(), "This offline game has not been downloaded.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        Intent startGame = new Intent(OfflineLevelsActivity.this, OfflineGameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, offlineLevelStartPages[levelNumber]);
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, offlineLevelEndPages[levelNumber]);
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }

    private void downloadLevel(int levelNumber) {
        if (doesLevelExist(levelNumber)) {
            Toast.makeText(getApplicationContext(),"This level has already been downloaded.", Toast.LENGTH_SHORT).show();
            return;
        }

        //download level in new activity
        Intent downloadIntent = new Intent();
        Bundle offlineLevelInfo = new Bundle();
        String downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        offlineLevelInfo.putString(OFFLINE_START_TITLE_KEY, offlineLevelStartPages[levelNumber]);
        offlineLevelInfo.putString(OFFLINE_FINISH_TITLE_KEY, offlineLevelEndPages[levelNumber]);
        offlineLevelInfo.putString(OFFLINE_DIRECTORY_KEY, downloadDirectory);
        offlineLevelInfo.putInt(OFFLINE_STEPS_TREE_SIZE_KEY, offlineLevelTreeSizes[levelNumber]);
        offlineLevelInfo.putInt(OFFLINE_LEVEL_NUMBER_KEY, levelNumber);
        downloadIntent.putExtras(offlineLevelInfo);

        DownloadService downloadService = new DownloadService();
        downloadService.enqueueWork(OfflineLevelsActivity.this, downloadIntent);
        notifyOfStartedDownload(levelNumber);
    }

    private boolean doesLevelExist(int levelNumber) {
        String downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File hasDownloadHappened = new File(downloadDirectory, OfflineController.CONFIRMATION + levelNumber);
        return hasDownloadHappened.exists();
    }

    private void notifyOfStartedDownload(int levelNumber) {
        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder startedDownload = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_wikipedia)
                .setContentTitle("Downloading level " + levelNumber)
                .setContentText("Download is in progress.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(NOTIFICATION_ID + levelNumber, startedDownload.build());
    }
}