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

public class DownloadLevelsActivity extends AppCompatActivity {
    public static final String OFFLINE_FINISH_TITLE_KEY = "offline_finish_title";
    public static final String OFFLINE_START_TITLE_KEY = "offline_start_title";
    public static final String OFFLINE_STEPS_TREE_SIZE_KEY = "offline_steps_tree_size";
    public static final String OFFLINE_DIRECTORY_KEY = "offline_directory";
    public static final String OFFLINE_LEVEL_NUMBER_KEY = "offline_level_number";

    private final static int LEVEL1 = 1;
    private final static int LEVEL2 = 2;
    private final static int LEVEL3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_levels);

        Button level1Button = findViewById(R.id.level1);
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Demonophobia";
                String finishPageTitle = "Qalb";
                downloadLevel(startPageTitle, finishPageTitle, 2, LEVEL1);
            }
        });
        Button level2Button = findViewById(R.id.level2);
        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Sexuality of Adolf Hitler";
                String finishPageTitle = "Squatting";
                downloadLevel(startPageTitle, finishPageTitle, 2, LEVEL2);
            }
        });
        Button level3Button = findViewById(R.id.level3);
        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Vanity Fair (novel)";
                String finishPageTitle = "Star Wars";
                downloadLevel(startPageTitle, finishPageTitle, 2, LEVEL3);
            }
        });
    }

    private void downloadLevel(String startTitle, String finishTitle, int treeSize, int levelNumber) {
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
        offlineLevelInfo.putString(OFFLINE_START_TITLE_KEY, startTitle);
        offlineLevelInfo.putString(OFFLINE_FINISH_TITLE_KEY, finishTitle);
        offlineLevelInfo.putString(OFFLINE_DIRECTORY_KEY, downloadDirectory);
        offlineLevelInfo.putInt(OFFLINE_STEPS_TREE_SIZE_KEY, treeSize);
        offlineLevelInfo.putInt(OFFLINE_LEVEL_NUMBER_KEY, levelNumber);
        downloadLevelIntent.putExtras(offlineLevelInfo);
        startActivity(downloadLevelIntent);
    }
}
