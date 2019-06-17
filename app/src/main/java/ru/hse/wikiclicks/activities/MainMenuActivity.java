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

/** Activity that represents the main menu of the game. */
public class MainMenuActivity extends AppCompatActivity {

    /** Creates the main menu. */
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
        setUpBookmarksButton();
        setUpChallengesButton();
    }

    private void setUpBookmarksButton() {
        final Button bookmarksButton = findViewById(R.id.bookmarks_button);
        setUpListenerToActivity(bookmarksButton, BookmarkActivity.class);
    }

    private void setUpChallengesButton() {
        final Button challengesButton = findViewById(R.id.challenges_button);
        setUpListenerToActivity(challengesButton, ChallengesActivity.class);
    }

    private void setUpStatisticsButton() {
        final Button statisticsButton = findViewById(R.id.statistics_button);
        setUpListenerToActivity(statisticsButton, StatisticsActivity.class);
    }

    private void setUpOfflineButton() {
        final Button offlineButton = findViewById(R.id.offline_button);
        setUpListenerToActivity(offlineButton, OfflineGameActivity.class);
    }

    private void setUpStartGameButton() {
        final Button startGameButton = findViewById(R.id.start_game_button);
        setUpListenerToActivity(startGameButton, SelectModeActivity.class);
    }

    /*
     * Sets onClickListener for specified button that starts specified activity and
     * changes the button's color
     */
    private void setUpListenerToActivity(final Button button, final Class<?> activity) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent startActivity = new Intent(MainMenuActivity.this, activity);
                startActivity(startActivity);
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
                /*
                 * Все сеттеры у этого builder'а возвращают его же, так что можно было бы сделать
                 *   цепочку вызовов без повторения переменной builder
                 *
                 * Все эти строки стоит вынести в текстовые ресурсы
                 * 1. Так проще за ними следить и менять при необходимости
                 * 2. Это очень нужно для локализации приложения
                 * Это же относится ко всем остальным захаркоженным в проекте строкам
                 */
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
