package ru.hse.wikiclicks.controllers.download;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.activities.OfflineLevelsActivity;

import static ru.hse.wikiclicks.activities.OfflineLevelsActivity.NOTIFICATION_ID;

/** Service that generates an offline game and downloads it. */
public class DownloadService extends JobIntentService {
    /** The job id for all DownloadServices. */
    public static final int JOB_ID = 179;

    /** Adds the given Intent to the queue of work to do. */
    public void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadService.class, JOB_ID, work);
    }

    /** Generates and downloads an offline game in a separate thread. Displays a notification upon finishing. */
    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        if (intent == null) {
            return; //this is not my work
        }

        Bundle extras = intent.getExtras();
        assert extras != null;
        String finishTitle = extras.getString(OfflineLevelsActivity.OFFLINE_FINISH_TITLE_KEY);
        String startTitle = extras.getString(OfflineLevelsActivity.OFFLINE_START_TITLE_KEY);
        String directory = extras.getString(OfflineLevelsActivity.OFFLINE_DIRECTORY_KEY);
        int levelSize = extras.getInt(OfflineLevelsActivity.OFFLINE_STEPS_TREE_SIZE_KEY);
        int levelNumber = extras.getInt(OfflineLevelsActivity.OFFLINE_LEVEL_NUMBER_KEY);

        OfflineGameSelector game = new OfflineGameSelector(startTitle, finishTitle, levelSize);
        DownloadController.downloadTree(game, directory, levelNumber);

        File hasDownloadHappened = new File(directory, DownloadController.CONFIRMATION + levelNumber);
        String resultText = "Download finished successfully!";
        if (!hasDownloadHappened.exists()) { //download failed
            resultText = "Downloading the level failed. Please try again.";
        }

        final NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID + levelNumber);
        NotificationCompat.Builder finishedDownload = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_wikipedia)
                .setContentTitle("Downloading level " + levelNumber)
                .setContentText(resultText)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(NOTIFICATION_ID + levelNumber, finishedDownload.build());
    }
}