package ru.hse.wikiclicks.controllers.wiki;

import java.util.HashSet;

/** Class responsible for checking the properties of a page according to the Wikidata database. */
public class BanController {
    private static final String COUNTRY_ID = "Q6256";
    private static final String FICTIONAL_COUNTRY_ID = "Q1145276";
    private static final String HISTORICAL_COUNTRY_ID = "Q3024240";
    private static final String YEAR_ID = "Q577";
    private static final String CALENDAR_YEAR_ID = "Q3186692";
    private static final String BC_YEAR_ID = "Q29964144";

    private final HashSet<String> properties = new HashSet<>();

    /** Creates the BanController for the Wikipedia page with the given URL. */
    public BanController(String url) {
        properties.addAll(WikiController.getWikidataPropertiesForUrl(url));
    }

    /** Checks if given Wikidata element is an instance of the given entity. */
    private boolean isInstanceOfEntity(String entity) {
        return properties.contains(entity);
    }

    /*
     * Все эти константы стоит сделать отдельными private static final String константами
     */
    private boolean isRealCountry() {
        return isInstanceOfEntity(COUNTRY_ID);
    }

    private boolean isHistoricalCountry() {
        return isInstanceOfEntity(HISTORICAL_COUNTRY_ID);
    }

    private boolean isFictionalCountry() {
        return isInstanceOfEntity(FICTIONAL_COUNTRY_ID);
    }

    private boolean isNormalYear() {
        return isInstanceOfEntity(YEAR_ID);
    }

    private boolean isCalendarYear() {
        return isInstanceOfEntity(CALENDAR_YEAR_ID);
    }

    private boolean isYearBC() {
        return isInstanceOfEntity(BC_YEAR_ID);
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
