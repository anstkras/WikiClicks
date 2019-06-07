package ru.hse.wikiclicks.controllers.modes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;

import androidx.preference.PreferenceManager;

public class CustomGameMode implements GameMode {
    private final boolean isTimeModeEnabled;
    private final boolean isStepsModeEnabled;
    private final boolean isBanYearsEnabled;
    private final boolean isBanCountriesEnabled;
    private final boolean isBanBackEnabled;

    public CustomGameMode(boolean isTimeModeEnabled, boolean isStepsModeEnabled, boolean isBanYearsEnabled,
                          boolean isBanCountriesEnabled, boolean isBanBackEnabled) {
        this.isTimeModeEnabled = isTimeModeEnabled;
        this.isStepsModeEnabled = isStepsModeEnabled;
        this.isBanYearsEnabled = isBanYearsEnabled;
        this.isBanCountriesEnabled = isBanCountriesEnabled;
        this.isBanBackEnabled = isBanBackEnabled;
    }

    public CustomGameMode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.isTimeModeEnabled = sharedPreferences.getBoolean("pref_time_mode", false);
        this.isStepsModeEnabled = sharedPreferences.getBoolean("pref_steps_mode", false);
        this.isBanYearsEnabled = sharedPreferences.getBoolean("pref_years_mode", false);
        this.isBanCountriesEnabled = sharedPreferences.getBoolean("pref_country_mode", false);
        this.isBanBackEnabled = sharedPreferences.getBoolean("pref_back_mode", false);
    }

    @Override
    public boolean timeModeEnabled() {
        return isTimeModeEnabled;
    }

    @Override
    public boolean stepsModeEnabled() {
        return isStepsModeEnabled;
    }

    @Override
    public boolean banYearsEnabled() {
        return isBanYearsEnabled;
    }

    @Override
    public boolean banCountriesEnabled() {
        return isBanCountriesEnabled;
    }

    @Override
    public boolean banBackEnabled() {
        return isBanBackEnabled;
    }

    @Override
    public <T> T accept(GameModeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        boolean[] features = {isTimeModeEnabled, isStepsModeEnabled, isBanYearsEnabled, isBanCountriesEnabled, isBanBackEnabled};
        dest.writeBooleanArray(features);
    }

    public static final Creator<CustomGameMode> CREATOR = new Creator<CustomGameMode>() {
        @Override
        public CustomGameMode createFromParcel(Parcel in) {
            boolean[] features = new boolean[5];
            in.readBooleanArray(features);
            return new CustomGameMode(features[0], features[1], features[2], features[3], features[4]);
        }


        @Override
        public CustomGameMode[] newArray(int size) {
            return new CustomGameMode[size];
        }
    };
}
