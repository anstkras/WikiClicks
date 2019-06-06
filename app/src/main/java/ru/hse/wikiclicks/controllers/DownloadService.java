package ru.hse.wikiclicks.controllers;

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

public class DownloadService extends JobIntentService {
    public static final int JOB_ID = 179;

    public void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DownloadService.class, JOB_ID, work);
    }

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

        ChooseOfflineGame game = new ChooseOfflineGame(startTitle, finishTitle, levelSize);
        OfflineController.downloadTree(game, directory, levelNumber);

        File hasDownloadHappened = new File(directory, OfflineController.CONFIRMATION + levelNumber);
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