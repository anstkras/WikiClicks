package ru.hse.wikiclicks.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashSet;

import static ru.hse.wikiclicks.controllers.WikiController.getWikidataIdByUrl;

/** Class responsible for checking the properties of a page according to the Wikidata database. */
public class BanController {
    private final HashSet<String> properties = new HashSet<>();

    /** Creates the BanController for the Wikipedia page with the given URL. */
    public BanController(String url) {
        String wikidataId = getWikidataIdByUrl(url);
        if (wikidataId.equals("")) {
            return;
        }
        properties.addAll(WikiController.getWikidataPropertiesById(wikidataId));
    }

    /** Checks if given Wikidata element is an instance of the given entity. */
    private boolean isInstanceOfEntity(String entity) {
        return properties.contains(entity);
    }

    private boolean isRealCountry() {
        return isInstanceOfEntity("Q6256");
    }

    private boolean isFictionalCountry() {
        return isInstanceOfEntity( "Q1145276");
    }

    /** Checks whether the controller's URL is a Wikipedia page about a country. */
    public boolean isYear() {
        return isInstanceOfEntity("Q577");
    }

    /** Checks whether the controller's URL is a Wikipedia page about a year. */
    public boolean isCountry() {
        return isRealCountry() || isFictionalCountry();
    }
}
