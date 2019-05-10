package ru.hse.wikiclicks.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

import static ru.hse.wikiclicks.controllers.WikiController.getWikidataIdByUrl;

/** Class responsible for queries to Wikidata and interacting with its API. Might need merging to WikiController. */
public class BanController {
    /** Checks if given Wikidata element is an instance of the given entity. */
    private static boolean isElementInstanceOfEntity(String element, String entity) {
        if (element.equals("")) {
            return false;
        }
        // P31 is "instance of"
        String query = "https://www.wikidata.org/w/api.php?action=wbgetclaims&format=json&entity=" + element + "&property=P31";
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            JSONArray results;
            try {
                results = json.getJSONObject("claims").getJSONArray("P31");
            } catch (JSONException e) {
                return false;
            }
            for (int i = 0; i < results.length(); i++) {
                String id = results.getJSONObject(i).getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getString("id");
                if (id.equals(entity)) {
                    return true;
                }
            }
            return false; // none were correct
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return false; // if getting information failed, best to assume page is correct
    }

    public static boolean isRealCountry(String url) {
        return isElementInstanceOfEntity(getWikidataIdByUrl(url), "Q6256");
    }

    public static boolean isFictionalCountry(String url) {
        return isElementInstanceOfEntity(getWikidataIdByUrl(url), "Q1145276"); //Q57662985
    }

    public static boolean isCountry(String url) {
        return isRealCountry(url) || isFictionalCountry(url);
    }

    public static boolean isYear(String url) {
        return isElementInstanceOfEntity(getWikidataIdByUrl(url), "Q3186692"); //Q186408
    }

    private static void failedJSON(JSONException e) {
        Log.e("Wikidata parsing error", e.getMessage());
    }

    private static void failedExecute(IOException e) {
        Log.e("Query execution error", e.getMessage());
    }

}
