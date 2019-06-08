package ru.hse.wikiclicks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.preference.PreferenceFragmentCompat;
import ru.hse.wikiclicks.R;
import ru.hse.wikiclicks.controllers.modes.CustomGameMode;

import static ru.hse.wikiclicks.activities.SelectModeActivity.GAME_MODE_KEY;

/** Activity for choosing settings of the custom game mode. */
public class CustomSettingsActivity extends AppCompatActivity {
    /** Creates the custom settings preference manager activity. */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_settings);

        PreferenceFragmentCompat settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.for_settings_fragment, settingsFragment).commit();

        setStartGameButton();
    }

    private void setStartGameButton() {
        final Button startGameButton = findViewById(R.id.start_custom_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent getEndpointsIntent = new Intent(CustomSettingsActivity.this, GetEndpointsActivity.class);
                getEndpointsIntent.putExtra(GAME_MODE_KEY, new CustomGameMode(getApplicationContext()));
                startActivity(getEndpointsIntent);
            }
        });
    }

    /** Class for storing the Fragment for choosing the custom settings. */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings_ui);
        }
    }
}