package ru.hse.wikiclicks.controllers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import ru.hse.wikiclicks.activities.DownloadLevelsActivity;

public class DownloadService extends JobIntentService {
    private static final String RECEIVER_KEY = "receiver";
    public static final int JOB_ID = 179;

    public void enqueueWork(Context context, Intent work, ResultReceiver receiver) {
        work.putExtra(RECEIVER_KEY, receiver);
        enqueueWork(context, DownloadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        if (intent == null) {
            return; //this is not my work
        }

        Bundle extras = intent.getExtras();
        assert extras != null;
        String finishTitle = extras.getString(DownloadLevelsActivity.OFFLINE_FINISH_TITLE_KEY);
        String startTitle = extras.getString(DownloadLevelsActivity.OFFLINE_START_TITLE_KEY);
        String directory = extras.getString(DownloadLevelsActivity.OFFLINE_DIRECTORY_KEY);
        int levelSize = extras.getInt(DownloadLevelsActivity.OFFLINE_STEPS_TREE_SIZE_KEY);
        int levelNumber = extras.getInt(DownloadLevelsActivity.OFFLINE_LEVEL_NUMBER_KEY);
        ResultReceiver receiver = intent.getParcelableExtra("receiver");

        ChooseOfflineGame game = new ChooseOfflineGame(startTitle, finishTitle, levelSize);
        OfflineController.downloadTree(game, directory, levelNumber);

        Bundle bundle = new Bundle();
        bundle.putString("data", "Download of level " + levelNumber + " finished.");
        receiver.send(JOB_ID, bundle);
    }
}