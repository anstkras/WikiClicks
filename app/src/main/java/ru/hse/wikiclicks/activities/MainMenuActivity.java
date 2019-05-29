package ru.hse.wikiclicks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import ru.hse.wikiclicks.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setUpStartGameButton();
        setUpExitButton();
        setUpStatisticsButton();
        setUpOfflineButton();
        setUpBookMarksButton();
        setUpChallengesButton();
        setUpDownloadButton();
    }

    private void setUpBookMarksButton() {
        final Button bookmarksButton = findViewById(R.id.bookmarks_button);
        bookmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarksButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent bookmarksIntent = new Intent(MainMenuActivity.this, BookmarkActivity.class);
                startActivity(bookmarksIntent);
            }
        });
    }

    private void setUpChallengesButton() {
        final Button challengesButton = findViewById(R.id.challenges_button);
        challengesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengesButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent challengesIntent = new Intent(MainMenuActivity.this, ChallengesActivity.class);
                startActivity(challengesIntent);
            }
        });
    }

    private void setUpStatisticsButton() {
        final Button statisticsButton = findViewById(R.id.statistics_button);
        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statisticsButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent statisticsIntent = new Intent(MainMenuActivity.this, StatisticsActivity.class);
                startActivity(statisticsIntent);
            }
        });
    }

    private void setUpOfflineButton() {
        final Button offlineButton = findViewById(R.id.offline_button);
        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offlineButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent offlineGameIntent = new Intent(MainMenuActivity.this, OfflineLevelsActivity.class);
                startActivity(offlineGameIntent);
            }
        });
    }

    private void setUpDownloadButton() {
        final Button downloadButton = findViewById(R.id.download_button);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadButton.setTextColor(getResources().getColor(R.color.colorUsed));
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                builder.setTitle("Download stuff");
                builder.setMessage("This strange button downloads several online games onto your device.\n" +
                        "It may take some time. Do you want to proceed?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent downloadIntent = new Intent(MainMenuActivity.this, DownloadActivity.class);
                        startActivity(downloadIntent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setUpStartGameButton() {
        final Button startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent startGame = new Intent(MainMenuActivity.this, SelectModeActivity.class);
                startActivity(startGame);
            }
        });
    }

    private void setUpExitButton() {
        final Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setTextColor(getResources().getColor(R.color.colorUsed));
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Are you sure you want to exit?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
