package ru.hse.wikiclicks.activities;

import androidx.appcompat.app.AppCompatActivity;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.WikiController;
import ru.hse.wikiclicks.controllers.WikiPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChallengesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challanges);

        Button level1Button = findViewById(R.id.level1);
        level1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WikiPage startPage = new WikiPage("Russia", "25391");
                WikiPage finishPage = new WikiPage("Moscow", "19004");
                startGame(startPage, finishPage);
            }
        });
        Button level2Button = findViewById(R.id.level2);
        level2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WikiPage startPage = new WikiPage("Dog", "4269567");
                WikiPage finishPage = new WikiPage("Cat", "6678");
                startGame(startPage, finishPage);
            }
        });
        Button level3Button = findViewById(R.id.level3);
        level3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WikiPage startPage = new WikiPage("Algorithm", "775");
                WikiPage finishPage = new WikiPage("Javascript", "9845");
                startGame(startPage, finishPage);
            }
        });
    }

    private void startGame(WikiPage startPage, WikiPage finishPage) {
        Intent startGame = new Intent(ChallengesActivity.this, GameActivity.class);
        Bundle pagesInfo = new Bundle();
        pagesInfo.putString(GetEndpointsActivity.START_ID_KEY, startPage.getId());
        pagesInfo.putString(GetEndpointsActivity.START_TITLE_KEY, startPage.getTitle());
        pagesInfo.putString(GetEndpointsActivity.FINISH_ID_KEY, finishPage.getId());
        pagesInfo.putString(GetEndpointsActivity.FINISH_TITLE_KEY, finishPage.getTitle());
        pagesInfo.putString(SelectModeActivity.GAME_MODE_KEY, SelectModeActivity.STEPS_MODE);
        startGame.putExtras(pagesInfo);
        startActivity(startGame);
    }
}
