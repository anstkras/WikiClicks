package ru.hse.wikiclicks.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.hse.wikiclicks.R;

import static ru.hse.wikiclicks.activities.SelectModeActivity.CUSTOM_MODE;
import static ru.hse.wikiclicks.activities.SelectModeActivity.GAME_MODE_KEY;
import static ru.hse.wikiclicks.activities.SelectModeActivity.STEPS_MODE;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        Fragment settingsFragment = new SettingsFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            transaction.add(R.id.for_settings_fragment, settingsFragment, "settings_screen");
        }

        transaction.commit();

        setStartGameButton();
    }


    private void setStartGameButton() {
        final Button startGameButton = findViewById(R.id.start_custom_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameButton.setTextColor(getResources().getColor(R.color.colorUsed));
                Intent getEndpointsIntent = new Intent(SettingsActivity.this, GetEndpointsActivity.class);
                getEndpointsIntent.putExtra(GAME_MODE_KEY, CUSTOM_MODE);
                startActivity(getEndpointsIntent);
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_ui);
        }
    }
}