package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

public class StepsGameMode implements GameMode {
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

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<StepsGameMode> CREATOR = new Creator<StepsGameMode>() {
        @Override
        public StepsGameMode createFromParcel(Parcel in) {
            return new StepsGameMode();
        }


        @Override
        public StepsGameMode[] newArray(int size) {
            return new StepsGameMode[size];
        }
    };

    public StepsGameMode() {
    }
}
