package ru.hse.wikiclicks.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.hse.wikiclicks.R;

public class SelectModeActivity extends AppCompatActivity {
    static final String GAME_MODE_KEY = "game_mode";
    static final String TIME_MODE = "time_mode";
    static final String STEPS_MODE = "steps_mode";

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
                getEndpointsIntent.putExtra(GAME_MODE_KEY, TIME_MODE);
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
                getEndpointsIntent.putExtra(GAME_MODE_KEY, STEPS_MODE);
                startActivity(getEndpointsIntent);
            }
        });
    }

    private void setUpCustomModeButton() {
        final Button customModeButton = findViewById(R.id.custom_mode_button);
        customModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Not implemented yet :(", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
