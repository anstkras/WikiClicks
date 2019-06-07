package ru.hse.wikiclicks.controllers.modes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;

import androidx.preference.PreferenceManager;

/**
 * Class that represents custom game mode where all features
 * could be chosen by user
 */
public class CustomGameMode implements GameMode {
    private final boolean isTimeModeEnabled;
    private final boolean isStepsModeEnabled;
    private final boolean isBanYearsEnabled;
    private final boolean isBanCountriesEnabled;
    private final boolean isBanBackEnabled;

    /** Constructs a custom game mode using the specified features */
    public CustomGameMode(boolean isTimeModeEnabled, boolean isStepsModeEnabled, boolean isBanYearsEnabled,
                          boolean isBanCountriesEnabled, boolean isBanBackEnabled) {
        this.isTimeModeEnabled = isTimeModeEnabled;
        this.isStepsModeEnabled = isStepsModeEnabled;
        this.isBanYearsEnabled = isBanYearsEnabled;
        this.isBanCountriesEnabled = isBanCountriesEnabled;
        this.isBanBackEnabled = isBanBackEnabled;
    }

    /** Constructs a custom game mode using shared preferences */
    public CustomGameMode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.isTimeModeEnabled = sharedPreferences.getBoolean("pref_time_mode", false);
        this.isStepsModeEnabled = sharedPreferences.getBoolean("pref_steps_mode", false);
        this.isBanYearsEnabled = sharedPreferences.getBoolean("pref_years_mode", false);
        this.isBanCountriesEnabled = sharedPreferences.getBoolean("pref_country_mode", false);
        this.isBanBackEnabled = sharedPreferences.getBoolean("pref_back_mode", false);
    }

    /** {@inheritDoc} */
    @Override
    public boolean timeModeEnabled() {
        return isTimeModeEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean stepsModeEnabled() {
        return isStepsModeEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banYearsEnabled() {
        return isBanYearsEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banCountriesEnabled() {
        return isBanCountriesEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banBackEnabled() {
        return isBanBackEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T accept(GameModeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    /** {@inheritDoc} */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        boolean[] features = {isTimeModeEnabled, isStepsModeEnabled, isBanYearsEnabled, isBanCountriesEnabled, isBanBackEnabled};
        dest.writeBooleanArray(features);
    }

    public static final Creator<CustomGameMode> CREATOR = new Creator<CustomGameMode>() {
        /** {@inheritDoc} */
        @Override
        public CustomGameMode createFromParcel(Parcel in) {
            boolean[] features = new boolean[5];
            in.readBooleanArray(features);
            return new CustomGameMode(features[0], features[1], features[2], features[3], features[4]);
        }

        /** {@inheritDoc} */
        @Override
        public CustomGameMode[] newArray(int size) {
            return new CustomGameMode[size];
        }
    };
}
