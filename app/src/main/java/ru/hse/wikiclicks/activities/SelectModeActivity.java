package ru.hse.wikiclicks.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.modes.StepsGameMode;
import ru.hse.wikiclicks.controllers.modes.TimeGameMode;

public class SelectModeActivity extends AppCompatActivity {
    static final String GAME_MODE_KEY = "game_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        setUpTimeModeButton();
        setStepsModeButton();
        setUpCustomModeButton();
    }

    private void setUpTimeModeButton() {
        final Button timeModeButton = findViewById(R.id.time_mode_button);
        timeModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeModeButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent getEndpointsIntent = new Intent(SelectModeActivity.this, GetEndpointsActivity.class);
                getEndpointsIntent.putExtra(GAME_MODE_KEY, new TimeGameMode());
                startActivity(getEndpointsIntent);
            }
        });
    }

    private void setStepsModeButton() {
        final Button stepsModeButton = findViewById(R.id.steps_mode_button);
        stepsModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsModeButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent getEndpointsIntent = new Intent(SelectModeActivity.this, GetEndpointsActivity.class);
                getEndpointsIntent.putExtra(GAME_MODE_KEY, new StepsGameMode());
                startActivity(getEndpointsIntent);
            }
        });
    }

    private void setUpCustomModeButton() {
        final Button customModeButton = findViewById(R.id.custom_mode_button);
        customModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customModeButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent settingsIntent = new Intent(SelectModeActivity.this, CustomSettingsActivity.class);
                startActivity(settingsIntent);
            }
        });
    }
}
