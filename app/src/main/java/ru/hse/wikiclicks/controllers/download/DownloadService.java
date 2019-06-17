package ru.hse.wikiclicks.controllers.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.activities.OfflineLevelsActivity;

import static ru.hse.wikiclicks.activities.OfflineLevelsActivity.NOTIFICATION_ID;

/** Service that generates an offline game and downloads it. */
public class DownloadService extends JobIntentService {
    /** The job id for all DownloadServices. */
    private static final int JOB_ID = 179;

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

        String resultText = getString(R.string.download_success_message);
        if (!DownloadController.checkConfirmation(directory, levelNumber)) { //download failed
            resultText = getString(R.string.download_failed_message);
        }

        notifyOfFinishedDownload(resultText, levelNumber);
    }

    /** Creates a notification that opens the levels menu upon clicking it. */
    private void notifyOfFinishedDownload(String resultText, int levelNumber) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID + levelNumber);

        Intent newIntent = new Intent(this, OfflineLevelsActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, newIntent, 0);

        String channelId = getString(R.string.channel_id);
        NotificationCompat.Builder finishedDownload = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_wikipedia)
                .setContentTitle(getString(R.string.download_finished_message, levelNumber))
                .setContentText(resultText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(NOTIFICATION_ID + levelNumber, finishedDownload.build());
    }
}