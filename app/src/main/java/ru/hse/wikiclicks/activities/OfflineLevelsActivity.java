package ru.hse.wikiclicks.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.download.DownloadService;
import ru.hse.wikiclicks.controllers.download.DownloadController;

/** Activity for choosing offline levels to download and play. */
public class OfflineLevelsActivity extends AppCompatActivity {
    /** The id for notifications about the downloads' status. */
    public final static int NOTIFICATION_ID = 179;

    /** Key for passing the offline game's finish title to the download service. */
    public static final String OFFLINE_FINISH_TITLE_KEY = "offline_finish_title";
    /** Key for passing the offline game's start title to the download service. */
    public static final String OFFLINE_START_TITLE_KEY = "offline_start_title";
    /** Key for passing the offline game's tree size to the download service. */
    public static final String OFFLINE_STEPS_TREE_SIZE_KEY = "offline_steps_tree_size";
    /** Key for passing the directory the game should be downloaded to to the download service. */
    public static final String OFFLINE_DIRECTORY_KEY = "offline_directory";
    /** Key for passing the number of the level to download to the download service. */
    public static final String OFFLINE_LEVEL_NUMBER_KEY = "offline_level_number";

    private List<String> offlineLevelStartPages = new ArrayList<>();
    private List<String> offlineLevelEndPages = new ArrayList<>();
    private List<Integer> offlineLevelTreeSizes = new ArrayList<>();

    private final static int LEVEL0 = 0;
    private final static int LEVEL1 = 1;
    private final static int LEVEL2 = 2;
    private final static int LEVEL3 = 3;
    private final static int LEVEL4 = 4;
    private final static int LEVEL5 = 5;
    private final static int LEVEL6 = 6;
    private final static int LEVEL7 = 7;
    private final static int LEVEL8 = 8;
    private final static int LEVEL9 = 9;

    /** Creates the activity, initializes the buttons for playing and downloading offline levels. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_levels);
        initializePages();

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

    /** Starts the given game level if it has been previously downloaded. */
    private void startGame(int levelNumber) {
        String gameDirectory = getDownloadDirectory();
        if (gameDirectory == null) {
            return;
        } else if (!DownloadController.checkConfirmation(gameDirectory, levelNumber)) {
            Toast toast = Toast.makeText(getApplicationContext(), "This offline game has not been downloaded.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent startGame = new Intent(OfflineLevelsActivity.this, OfflineGameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, offlineLevelStartPages.get(levelNumber));
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, offlineLevelEndPages.get(levelNumber));
        pagesInfo.putString(OfflineLevelsActivity.OFFLINE_DIRECTORY_KEY, gameDirectory);
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }

    /** Starts downloading the given level in a JobIntentService if it has not been previously downloaded. */
    private void downloadLevel(int levelNumber) {
        String downloadDirectory = getDownloadDirectory();
        if (downloadDirectory == null) {
            return;
        } else if (DownloadController.checkConfirmation(downloadDirectory, levelNumber)) {
            Toast toast = Toast.makeText(getApplicationContext(),"This level has already been downloaded.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Intent downloadIntent = new Intent();
        Bundle offlineLevelInfo = new Bundle();
        offlineLevelInfo.putString(OFFLINE_START_TITLE_KEY, offlineLevelStartPages.get(levelNumber));
        offlineLevelInfo.putString(OFFLINE_FINISH_TITLE_KEY, offlineLevelEndPages.get(levelNumber));
        offlineLevelInfo.putString(OFFLINE_DIRECTORY_KEY, downloadDirectory);
        offlineLevelInfo.putInt(OFFLINE_STEPS_TREE_SIZE_KEY, offlineLevelTreeSizes.get(levelNumber));
        offlineLevelInfo.putInt(OFFLINE_LEVEL_NUMBER_KEY, levelNumber);
        downloadIntent.putExtras(offlineLevelInfo);

        DownloadService downloadService = new DownloadService();
        downloadService.enqueueWork(OfflineLevelsActivity.this, downloadIntent);
        notifyOfStartedDownload(levelNumber);
    }

    /** Returns the current download directory for external files, displays a toast on failure. */
    private String getDownloadDirectory() {
        File downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (downloadDirectory == null) {
            Toast toast = Toast.makeText(getApplicationContext(),"Error: WikiClicks can't reach external storage", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }
        return downloadDirectory.getAbsolutePath();
    }

    /** Creates a notification that alerts the user about the download start and destructs on click. */
    private void notifyOfStartedDownload(int levelNumber) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getString(R.string.channel_id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Notification channels appear from SDK 26
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        Intent newIntent = new Intent();
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, newIntent, 0);

        NotificationCompat.Builder startedDownload = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_wikipedia)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle("Downloading level " + levelNumber)
                .setContentText("Download is in progress.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(NOTIFICATION_ID + levelNumber, startedDownload.build());
    }

    private void initializePages() {
        TypedArray offlineLevels = getResources().obtainTypedArray(R.array.offline_levels);
        for (int i = LEVEL0; i <= LEVEL9; i++) {
            String[] currentLevel = getResources().getStringArray(offlineLevels.getResourceId(i, 0));
            String startPageTitle = currentLevel[0];
            String endPageTitle = currentLevel[1];
            Integer pageTreeSize = Integer.parseInt(currentLevel[2]);
            offlineLevelStartPages.add(startPageTitle);
            offlineLevelEndPages.add(endPageTitle);
            offlineLevelTreeSizes.add(pageTreeSize);
        }
        offlineLevels.recycle();
    }
}