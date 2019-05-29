package ru.hse.wikiclicks.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class GameModeFactory {
    public static GameMode getGameMode(String mode, int level, Context context) {
        if (mode.equals("time_mode")) {
            return new TimeGameMode();
        }
        if (mode.equals("steps_mode")) {
            return new StepsGameMode();
        }
        if (mode.equals("custom_mode")) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return new CustomGameMode(sharedPreferences.getBoolean("pref_time_mode", false),
                    sharedPreferences.getBoolean("pref_steps_mode", false),
                    sharedPreferences.getBoolean("pref_years_mode", false),
                    sharedPreferences.getBoolean("pref_country_mode", false),
                    sharedPreferences.getBoolean("pref_back_mode", false));
        }
        if (mode.equals("level_mode")) {
            return new LevelGameMode(level);
        }

        throw new IllegalArgumentException("No such mode exists: " + mode);
    }
}
