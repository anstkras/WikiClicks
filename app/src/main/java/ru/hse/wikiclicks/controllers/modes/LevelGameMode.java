package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

/** Class that represents level game mode */
public class LevelGameMode implements GameMode {
    private final int level;

    public LevelGameMode(int level) {
        this.level = level;
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

    /** Returns the level number */
    public int getLevel() {
        return level;
    }

    /** {@inheritDoc} */
    @Override
    public int describeContents() {
        return hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
    }

    public static final Creator<LevelGameMode> CREATOR = new Creator<LevelGameMode>() {
        /** {@inheritDoc} */
        @Override
        public LevelGameMode createFromParcel(Parcel in) {
            return new LevelGameMode(in.readInt());
        }

        /** {@inheritDoc} */
        @Override
        public LevelGameMode[] newArray(int size) {
            return new LevelGameMode[size];
        }
    };
}
