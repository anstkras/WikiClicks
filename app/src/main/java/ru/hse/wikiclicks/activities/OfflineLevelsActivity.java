package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;

public class OfflineLevelsActivity extends AppCompatActivity {
    final static String[] offlineLevelStartPages = {"", "Vanity Fair (novel)", "Women in Russia", "History of tea in Japan",
            "Invisible Pink Unicorn", "Demonophobia", "Sexuality of Adolf Hitler",
            "Moscow", "Lady Justice", "Butte", "Anne Hathaway (wife of Shakespeare)"};
    final static String[] offlineLevelEndPages = {"", "Star Wars", "Google", "\"Hello, World!\" program",
            "Game of Thrones", "Grigori Rasputin", "Squatting",
            "Prada", "Seafood", "Seven Wonders of the Ancient World", "Anne Hathaway"};
    final static int[] offlineLevelTreeSizes = {-1, 2, 2, 2, 2, 3, 2, 1, 2, 2, 3};

    public final static int LEVEL1 = 1;
    public final static int LEVEL2 = 2;
    public final static int LEVEL3 = 3;
    public final static int LEVEL4 = 4;
    public final static int LEVEL5 = 5;
    public final static int LEVEL6 = 6;
    public final static int LEVEL7 = 7;
    public final static int LEVEL8 = 9;
    public final static int LEVEL9 = 9;
    public final static int LEVEL10 = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_levels);

        createButtonForLevel((Button)findViewById(R.id.level1), LEVEL1);
        createButtonForLevel((Button)findViewById(R.id.level2), LEVEL2);
        createButtonForLevel((Button)findViewById(R.id.level3), LEVEL3);
        createButtonForLevel((Button)findViewById(R.id.level4), LEVEL4);
        createButtonForLevel((Button)findViewById(R.id.level5), LEVEL5);
        createButtonForLevel((Button)findViewById(R.id.level6), LEVEL6);
        createButtonForLevel((Button)findViewById(R.id.level7), LEVEL7);
        createButtonForLevel((Button)findViewById(R.id.level8), LEVEL8);
        createButtonForLevel((Button)findViewById(R.id.level9), LEVEL9);
        createButtonForLevel((Button)findViewById(R.id.level10), LEVEL10);
    }

    private void createButtonForLevel(Button button, int level) {
        final int currentLevel = level;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(currentLevel);
            }
        });
    }

    private void startGame(int level) {
        Intent startGame = new Intent(OfflineLevelsActivity.this, OfflineGameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, offlineLevelStartPages[level]);
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, offlineLevelEndPages[level]);
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }
}