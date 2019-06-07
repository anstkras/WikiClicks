package ru.hse.wikiclicks.controllers.wiki;

import java.util.HashSet;

/** Class responsible for checking the properties of a page according to the Wikidata database. */
public class BanController {
    private final HashSet<String> properties = new HashSet<>();

    /** Creates the BanController for the Wikipedia page with the given URL. */
    public BanController(String url) {
        properties.addAll(WikiController.getWikidataPropertiesForUrl(url));
    }

    /** Checks if given Wikidata element is an instance of the given entity. */
    private boolean isInstanceOfEntity(String entity) {
        return properties.contains(entity);
    }

    private boolean isRealCountry() {
        return isInstanceOfEntity("Q6256");
    }

    private boolean isHistoricalCountry() {
        return isInstanceOfEntity("Q3024240");
    }

    private boolean isFictionalCountry() {
        return isInstanceOfEntity( "Q1145276");
    }

    private boolean isNormalYear() {
        return isInstanceOfEntity("Q577");
    }

    private boolean isCalendarYear() {
        return isInstanceOfEntity("Q3186692");
    }

    private boolean isYearBC() {
        return isInstanceOfEntity("Q29964144");
    }

    /** Checks whether the controller's URL is a Wikipedia page about a year. */
    public boolean isYear() {
        return isNormalYear() || isCalendarYear() || isYearBC();
    }

    /** Checks whether the controller's URL is a Wikipedia page about a country. */
    public boolean isCountry() {
        return isRealCountry() || isFictionalCountry() || isHistoricalCountry();
    }
}
