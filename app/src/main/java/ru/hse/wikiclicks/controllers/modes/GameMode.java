package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcelable;

public interface GameMode extends Parcelable {
    boolean timeModeEnabled();

    boolean stepsModeEnabled();

    boolean banYearsEnabled();

    boolean banCountriesEnabled();

    boolean banBackEnabled();

    <T> T accept(GameModeVisitor<T> visitor);
}
