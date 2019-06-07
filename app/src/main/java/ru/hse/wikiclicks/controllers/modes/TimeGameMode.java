package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

/**
 * Class that represents time game mode.
 * This is a singleton.
 */
public class TimeGameMode implements GameMode {

    /** Returns the singleton instance */
    public static TimeGameMode getInstance() {
        return INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public boolean timeModeEnabled() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean stepsModeEnabled() {
        return false;
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

    public static final Creator<TimeGameMode> CREATOR = new Creator<TimeGameMode>() {
        /** {@inheritDoc} */
        @Override
        public TimeGameMode createFromParcel(Parcel in) {
            return new TimeGameMode();
        }

        /** {@inheritDoc} */
        @Override
        public TimeGameMode[] newArray(int size) {
            return new TimeGameMode[size];
        }
    };

    private TimeGameMode() {
    }

    private static final TimeGameMode INSTANCE = new TimeGameMode();
}
