package ru.hse.wikiclicks.controllers;

import android.net.Uri;
import android.util.Log;

import org.json.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Class responsible for queries to Wikipedia and interacting with its API. */
public class WikiController {

    /** Method that returns a random WikiPage or the Avatar (band) page if the request for a random page failed. */
    public static WikiPage getRandomPage() {
        // requests list of one random page in json format without namespaces
        String query = "https://en.wikipedia.org/w/api.php?action=query&list=random&format=json&rnnamespace=0&rnlimit=1";
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            JSONObject result = json.getJSONObject("query").getJSONArray("random").getJSONObject(0);
            return new WikiPage(result.getString("title"), result.getString("id"));
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return new WikiPage("Avatar (band)","26296973");
    }

    /**
     * Method that checks whether the given link is correct, i.e. an acceptable move in the game.
     * @param url a given url.
     * @return currently returns true if the url's host is the mobile english wikipedia, will be improved.
     */
    public static boolean isCorrectWikipediaLink(String url) {
        return "en.m.wikipedia.org".equals(Uri.parse(url).getHost());
    }

    /** Method that returns an url for the given page id. */
    public static String getPageLinkById(String id) {
        return "https://en.m.wikipedia.org/?curid=" + id;
    }

    /**
     * Gets a list of suggestions for a page prefix, based on the Wikipedia search.
     * @param prefix the already inputted prefix.
     * @return A List of WikiPages of length no more than 15.
     */
    public static List<WikiPage> getSearchSuggestions(String prefix) {
        // requests list of prefixsearch results of length no more than 15
        String query = "https://en.wikipedia.org/w/api.php?action=query&list=prefixsearch&prop=info&format=json&origin=*&pslimit=15&pssearch=" + prefix;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            JSONArray results = json.getJSONObject("query").getJSONArray("prefixsearch");
            ArrayList<WikiPage> suggestions = new ArrayList<>();
            for (int i = 0; i < results.length(); ++i) {
                String page_id = results.getJSONObject(i).getString("pageid");
                String title = results.getJSONObject(i).getString("title");
                suggestions.add(new WikiPage(title, page_id));
            }
            return suggestions;
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return new ArrayList<>();
    }

    /**
     * Creates a WikiPage from the given url.
     * @param url a correct Wikipedia url.
     * @return the WikiPage that the given URL will redirect to, empty WikiPage if failed.
     */
    public static WikiPage getPageFromUrl(String url) {
        String title = url.replace("https://en.m.wikipedia.org/wiki/", "");
        // requests basic info of page with given title, processing redirects
        String query = "https://en.wikipedia.org/w/api.php?action=query&format=json&redirects&titles=" + title;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            return new WikiPage(title, json.getJSONObject("query").getJSONObject("pages").names().getString(0));
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return new WikiPage();
    }

    /** Returns the id of the page that the page with given id redirects to. */
    public static String getRedirectedId(String id) {
        // requests basic info of page with given id, processing redirects
        String query = "https://en.wikipedia.org/w/api.php?action=query&format=json&redirects&pageids=" + id;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            return json.getJSONObject("query").getJSONObject("pages").names().getString(0);
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return id;
    }

    /**  Method that returns a short extract from the Wikipedia page with the given id. */
    public static String getExtract(String id) {
        // requests 1 extract from page with given id no more than 3 sentences long, formatted to show strange symbols
        String query = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exlimit=1&exsentences=3&explaintext=1&format=json&formatversion=2&pageids=" + id;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            return json.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
        } catch (IOException e) {
            failedExecute(e);
        } catch (JSONException e) {
            failedJSON(e);
        }
        return "";
    }

    private static void failedJSON(JSONException e) {
        Log.e("Wikipedia parsing error", e.getMessage());
    }

    private static void failedExecute(IOException e) {
        Log.e("Query execution error", e.getMessage());
    }
}
