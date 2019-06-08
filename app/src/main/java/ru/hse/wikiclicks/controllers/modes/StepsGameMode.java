package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

/**
 * Class that represents steps game mode.
 * This is a singleton.
 */
public class StepsGameMode implements GameMode {

    /** Returns the singleton instance. */
    public static StepsGameMode getInstance() {
        return INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public boolean timeModeEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean stepsModeEnabled() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banYearsEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banCountriesEnabled() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean banBackEnabled() {
        return false;
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
    }

    public static final Creator<StepsGameMode> CREATOR = new Creator<StepsGameMode>() {
        /** {@inheritDoc} */
        @Override
        public StepsGameMode createFromParcel(Parcel in) {
            return new StepsGameMode();
        }

        /** {@inheritDoc} */
        @Override
        public StepsGameMode[] newArray(int size) {
            return new StepsGameMode[size];
        }
    };

    private StepsGameMode() {
    }

    private static final StepsGameMode INSTANCE = new StepsGameMode();
}
