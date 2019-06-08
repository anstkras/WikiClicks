package ru.hse.wikiclicks.controllers.modes;

import android.os.Parcelable;

/** Interface that represents game mode in WikiClicks game. */
public interface GameMode extends Parcelable {

    /** Returns true if the time is counted, false otherwise.  */
    boolean timeModeEnabled();

    /** Returns true if the steps are counted, false otherwise. */
    boolean stepsModeEnabled();

    /** Returns true if the usage of year urls is banned, false otherwise. */
    boolean banYearsEnabled();

    /** Returns true if the usage of country urls is banned, false otherwise. */
    boolean banCountriesEnabled();

    /** Returns true if the back button press is banned, false otherwise. */
    boolean banBackEnabled();

    /** Implements visitor pattern to add game mode dependent functionality. */
    <T> T accept(GameModeVisitor<T> visitor);
}
