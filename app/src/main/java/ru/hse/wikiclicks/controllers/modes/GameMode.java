package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcelable;

/** Interface that represents game mode in wikiClicks game */
public interface GameMode extends Parcelable {

    /** Returns if the time is counted */
    boolean timeModeEnabled();

    /** Returns if the steps are counted */
    boolean stepsModeEnabled();

    /** Returns if the usage of years url is banned */
    boolean banYearsEnabled();

    /** Returns if the usage of countries url is banned */
    boolean banCountriesEnabled();

    /** Returns if the back button press is be banned */
    boolean banBackEnabled();

    /** Implements visitor pattern to add game mode dependent functionality */
    <T> T accept(GameModeVisitor<T> visitor);
}
