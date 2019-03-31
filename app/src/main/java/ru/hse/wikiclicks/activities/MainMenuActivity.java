package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


import ru.hse.wikiclicks.R;
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button startGameButton = findViewById(R.id.StartGameButton);
        System.out.println(startGameButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGame = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(startGame);
            }
        });
    }
}
