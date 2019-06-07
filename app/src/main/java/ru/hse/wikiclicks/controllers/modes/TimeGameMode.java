package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcel;

public class TimeGameMode implements GameMode { // TODO make singleton?
    @Override
    public boolean timeModeEnabled() {
        return true;
    }

    @Override
    public boolean stepsModeEnabled() {
        return false;
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

    public static final Creator<TimeGameMode> CREATOR = new Creator<TimeGameMode>() {
        @Override
        public TimeGameMode createFromParcel(Parcel in) {
            return new TimeGameMode();
        }


        @Override
        public TimeGameMode[] newArray(int size) {
            return new TimeGameMode[size];
        }
    };

    public TimeGameMode() {
    }
}
