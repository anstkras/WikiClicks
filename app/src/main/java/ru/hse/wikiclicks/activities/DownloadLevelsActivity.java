package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.OfflineController;

import static ru.hse.wikiclicks.activities.OfflineLevelsActivity.*;

public class DownloadLevelsActivity extends AppCompatActivity {
    public static final String OFFLINE_FINISH_TITLE_KEY = "offline_finish_title";
    public static final String OFFLINE_START_TITLE_KEY = "offline_start_title";
    public static final String OFFLINE_STEPS_TREE_SIZE_KEY = "offline_steps_tree_size";
    public static final String OFFLINE_DIRECTORY_KEY = "offline_directory";
    public static final String OFFLINE_LEVEL_NUMBER_KEY = "offline_level_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_levels);

        createDownloadButtonForLevel((Button)findViewById(R.id.level1), LEVEL1);
        createDownloadButtonForLevel((Button)findViewById(R.id.level2), LEVEL2);
        createDownloadButtonForLevel((Button)findViewById(R.id.level3), LEVEL3);
        createDownloadButtonForLevel((Button)findViewById(R.id.level4), LEVEL4);
        createDownloadButtonForLevel((Button)findViewById(R.id.level5), LEVEL5);
        createDownloadButtonForLevel((Button)findViewById(R.id.level6), LEVEL6);
        createDownloadButtonForLevel((Button)findViewById(R.id.level7), LEVEL7);
        createDownloadButtonForLevel((Button)findViewById(R.id.level8), LEVEL8);
        createDownloadButtonForLevel((Button)findViewById(R.id.level9), LEVEL9);
        createDownloadButtonForLevel((Button)findViewById(R.id.level10), LEVEL10);
    }

    private void createDownloadButtonForLevel(Button button, int level) {
        final int currentLevel = level;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadLevel(currentLevel);
            }
        });
    }

    private void downloadLevel(int levelNumber) {
        //check if level has been downloaded already
        String downloadDirectory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        File hasDownloadHappened = new File(downloadDirectory, OfflineController.CONFIRMATION + levelNumber);
        System.out.println(hasDownloadHappened.getAbsolutePath());
        if (hasDownloadHappened.exists()) {
            Toast toast = Toast.makeText(getApplicationContext(),"This level has already been downloaded.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        //download level in new activity
        Intent downloadLevelIntent = new Intent(DownloadLevelsActivity.this, DownloadActivity.class);
        Bundle offlineLevelInfo = new Bundle();
        offlineLevelInfo.putString(OFFLINE_START_TITLE_KEY, offlineLevelStartPages[levelNumber]);
        offlineLevelInfo.putString(OFFLINE_FINISH_TITLE_KEY, offlineLevelEndPages[levelNumber]);
        offlineLevelInfo.putString(OFFLINE_DIRECTORY_KEY, downloadDirectory);
        offlineLevelInfo.putInt(OFFLINE_STEPS_TREE_SIZE_KEY, offlineLevelTreeSizes[levelNumber]);
        offlineLevelInfo.putInt(OFFLINE_LEVEL_NUMBER_KEY, levelNumber);
        downloadLevelIntent.putExtras(offlineLevelInfo);
        startActivity(downloadLevelIntent);
    }
}
