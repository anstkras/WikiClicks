package ru.hse.wikiclicks.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashSet;

import static ru.hse.wikiclicks.controllers.WikiController.getWikidataIdByUrl;

/** Class responsible for queries to Wikidata and interacting with its API. */
public class BanController {
    private final HashSet<String> properties = new HashSet<>();

    /** Creates the BanController for the Wikipedia page with the given URL. */
    public BanController(String url) {
        String wikidataId = getWikidataIdByUrl(url);
        if (wikidataId.equals("")) {
            return;
        }
        String query = "https://www.wikidata.org/w/api.php?action=wbgetclaims&format=json&entity=" + wikidataId + "&property=P31";
        JSONArray results = new JSONArray();
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            results = json.getJSONObject("claims").getJSONArray("P31");
        } catch (JSONException ignored) { //no instances of
        } catch (IOException e) {
            failedExecute(e);
        }
        for (int i = 0; i < results.length(); i++) {
            try {
                properties.add(results.getJSONObject(i).
                        getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getString("id"));
            } catch (JSONException e) {
                failedJSON(e);
            }
        }
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

    /** Checks whether the contoller's URL is a Wikipedia page about a country. */
    public boolean isYear() {
        return isInstanceOfEntity("Q577");
    }

    /** Checks whether the contoller's URL is a Wikipedia page about a year. */
    public boolean isCountry() {
        return isRealCountry() || isFictionalCountry();
    }

    private static void failedExecute(IOException e) {
        Log.e("Query execution error", e.getMessage());
    }

    private static void failedJSON(JSONException e) {
        Log.e("Wikidata parsing error", e.getMessage());
    }

}
