package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

public class LevelGameMode implements GameMode {
    private final int level;

    public LevelGameMode(int level) {
        this.level = level;
    }

    @Override
    public boolean timeModeEnabled() {
        return false;
    }

    @Override
    public boolean stepsModeEnabled() {
        return true;
    }

    @Override
    public boolean banYearsEnabled() {
        return false;
    }

    @Override
    public boolean banCountriesEnabled() {
        return false;
    }

    @Override
    public boolean banBackEnabled() {
        return false;
    }

    @Override
    public <T> T accept(GameModeVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
    }

    public static final Creator<LevelGameMode> CREATOR = new Creator<LevelGameMode>() {
        @Override
        public LevelGameMode createFromParcel(Parcel in) {
            return new LevelGameMode(in.readInt());
        }


        @Override
        public LevelGameMode[] newArray(int size) {
            return new LevelGameMode[size];
        }
    };
}
