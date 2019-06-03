package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;

public class OfflineLevelsActivity extends AppCompatActivity {
    public final static int LEVEL1 = 1;
    public final static int LEVEL2 = 2;
    public final static int LEVEL3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_levels);

        Button level1Button = findViewById(R.id.level1);
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Demonophobia";
                String finishPageTitle = "Qalb";
                startGame(startPageTitle, finishPageTitle, LEVEL1);
            }
        });
        Button level2Button = findViewById(R.id.level2);
        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Sexuality of Adolf Hitler";
                String finishPageTitle = "Squatting";
                startGame(startPageTitle, finishPageTitle, LEVEL2);
            }
        });
        Button level3Button = findViewById(R.id.level3);
        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startPageTitle = "Vanity Fair (novel)";
                String finishPageTitle = "Star Wars";
                startGame(startPageTitle, finishPageTitle, LEVEL3);
            }
        });
    }

    private void startGame(String startPageTitle, String finishPageTitle, int level) {
        Intent startGame = new Intent(OfflineLevelsActivity.this, OfflineGameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, startPageTitle);
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, finishPageTitle);
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }
}