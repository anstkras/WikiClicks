package ru.hse.wikiclicks.controllers;

import android.net.Uri;
import android.util.Log;

import com.google.common.base.Joiner;

import org.apache.commons.lang3.StringUtils;

import org.json.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/** Class responsible for queries to Wikipedia and interacting with its API. */
public class WikiController {
    private static final String WIKIPEDIA_ERROR = "Wikipedia parsing error";
    private static final String WIKIDATA_ERROR = "Wikidata parsing error";
    private static final String JSOUP_ERROR = "Jsoup execution error";

    /** Method that returns a random WikiPage or the Avatar (band) page if the request for a random page failed. */
    public static WikiPage getRandomPage() {
        // requests list of one random page in json format without namespaces
        try {
            JSONObject queryResult = getQueryResult("list=random", "rnnamespace=0", "rnlimit=1");
            JSONObject result = queryResult.getJSONArray("random").getJSONObject(0);
            return new WikiPage(result.getString("title"), result.getString("id"));
        } catch (JSONException e) {
            Log.e(WIKIDATA_ERROR, "returning random page");
        }
        return new WikiPage("Avatar (band)","26296973");
    }

    /**
     * Method that checks whether the given link is correct, i.e. an acceptable move in the game.
     * @param url a given url.
     * @return currently returns true if the url's host is the mobile english wikipedia,
     *  and url is a wiki page from main namespace.
     */
    public static boolean isCorrectWikipediaLink(String url) {
        return "en.m.wikipedia.org".equals(Uri.parse(url).getHost()) && isNamespaceCorrect(getPageFromUrl(url).getId());
    }

    /** * Method that checks namespace of page is the main namespace, i.e. an acceptable link. */
    private static boolean isNamespaceCorrect(String id) {
        try {
            JSONObject queryResult = getQueryResult("prop=info", "pageids=" + id);
            return "0".equals(queryResult.getJSONObject("pages").getJSONObject(id).getString("ns"));
        } catch (JSONException e) {
            Log.e(WIKIDATA_ERROR, "checking namespace is correct for id" + id);
        }
        return true;
    }

    /**
     * Gets a list of suggestions for a page prefix, based on the Wikipedia search.
     * @param prefix the already inputted prefix.
     * @return A List of WikiPages of length no more than 15.
     */
    public static List<WikiPage> getSearchSuggestions(String prefix) {
        // requests list of prefixsearch results of length no more than 15
        try {
            JSONObject queryResult = getQueryResult("list=prefixsearch", "prop=info",
                    "origin=*", "pslimit=15", "pssearch=" + prefix);
            JSONArray results = queryResult.getJSONArray("prefixsearch");
            ArrayList<WikiPage> suggestions = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                String page_id = results.getJSONObject(i).getString("pageid");
                String title = results.getJSONObject(i).getString("title");
                suggestions.add(new WikiPage(title, page_id));
            }
            return suggestions;
        } catch (JSONException e) {
            Log.e(WIKIDATA_ERROR, "getting list of suggestions for prefix " + prefix);
        }
        return new ArrayList<>();
    }

    /**
     * Creates a WikiPage from the given url.
     * @param url a correct Wikipedia url.
     * @return the WikiPage that the given URL will redirect to, empty WikiPage if failed.
     */
    public static WikiPage getPageFromUrl(String url) {
        String title = getPageTitleFromUrl(url);
        // requests basic info of page with given title, processing redirects
        try {
            JSONObject queryResult = getQueryResult("redirects", "titles=" + title);
            return new WikiPage(title, queryResult.getJSONObject("pages").names().getString(0));
        } catch (JSONException e) {
            Log.e(WIKIDATA_ERROR,"getting page info for page " + title);
        }
        return new WikiPage();
    }

    /** Returns the title of the page with the given url */
    public static String getPageTitleFromUrl(String url) {
        try {
            url = java.net.URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ignored) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
        String title = url.replace("https://en.m.wikipedia.org/wiki/", "");
        String normalizedTitle = StringUtils.capitalize(title.replaceAll("_", " "));

        return normalizedTitle;
    }

    /** Returns the id of the page that the page with given id redirects to. */
    public static String getRedirectedId(String id) {
        // requests basic info of page with given id, processing redirects
        try {
            JSONObject queryResult = getQueryResult("redirects", "pageids=" + id);
            return queryResult.getJSONObject("pages").names().getString(0);
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR,"retrieving id after redirects for id " + id);
        }
        return id;
    }

    /**  Method that returns a short extract from the Wikipedia page with the given id. */
    public static String getExtract(String id) {
        // requests 1 extract from page with given id no more than 2 sentences long, formatted to show strange symbols
        try {
            JSONObject queryResult = getQueryResult("prop=extracts", "exsentences=2",
                    "explaintext=1", "formatversion=2", "pageids=" + id);
            String result = queryResult.getJSONArray("pages").getJSONObject(0).getString("extract");
            return result.isEmpty() ? "No information available." : result;
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR, "getting extract for id " + id);
        }
        return "No information available.";
    }

    /** Returns Wikidata Id of page (used for controlling bans) by given url. */
    private static String getWikidataIdByUrl(String url) {
        String id = getPageFromUrl(url).getId();
        try {
            JSONObject queryResult = getQueryResult("prop=pageprops", "ppprop=wikibase_item", "redirects", "pageids=" + id);
            return queryResult.getJSONObject("pages").getJSONObject(id).getJSONObject("pageprops").getString("wikibase_item");
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR, "retrieving Wikidata id for id " + id);
        }
        return "";
    }

    public static ArrayList<String> getLinksFromPage(String title) {
        ArrayList<String> links = new ArrayList<>();
        String shouldContinue = null;
        try {
            do {
                JSONObject queryResult;
                if (shouldContinue != null) {
                    queryResult = getQueryResult("prop=links", "pllimit=max", "plnamespace=0",
                            "plcontinue=" + shouldContinue, "titles=" + title);
                } else {
                    queryResult = getQueryResult("prop=links", "pllimit=max", "plnamespace=0", "titles=" + title);
                }
                JSONObject listWithId = queryResult.getJSONObject("pages");
                JSONArray linksList = listWithId.getJSONObject(listWithId.names().getString(0)).getJSONArray("links");
                for (int i = 0; i < linksList.length(); i++) {
                    String linkTitle = linksList.getJSONObject(i).getString("title");
                    links.add(linkTitle);
                }
                try { // get query continuation.
                    shouldContinue = queryResult.getJSONObject("continue").getString("plcontinue");
                } catch (JSONException e) {
                    shouldContinue = null;
                }
            } while (shouldContinue != null);
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR, "retrieving links from page " + title);
            e.printStackTrace();
        }
        return links;
    }

    public static String getUrlForTitle(String title) {
        return "https://en.m.wikipedia.org/wiki/" + title;
    }

    public static ArrayList<String> getLinksToPage(String title) {
        ArrayList<String> links = new ArrayList<>();
        String shouldContinue = null;
        try {
            do {
                JSONObject queryResult;
                if (shouldContinue != null) {
                    queryResult = getQueryResult("list=backlinks", "bllimit=max", "blnamespace=0",
                            "blcontinue=" + shouldContinue, "bltitle=" + title);
                } else {
                    queryResult = getQueryResult("list=backlinks", "bllimit=max", "blnamespace=0", "bltitle=" + title);
                }
                JSONArray linksList = queryResult.getJSONArray("backlinks");
                for (int i = 0; i < linksList.length(); i++) {
                    String linkTitle = linksList.getJSONObject(i).getString("title");
                    links.add(linkTitle);
                }
                try { // get query continuation.
                    shouldContinue = queryResult.getJSONObject("continue").getString("blcontinue");
                } catch (JSONException e) {
                    shouldContinue = null;
                }
            } while (shouldContinue != null);
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR, "retrieving links to page " + title);
        }
        return links;
    }

    public static HashSet<String> getWikidataPropertiesForUrl(String url) {
        String id = getWikidataIdByUrl(url);
        HashSet<String> properties = new HashSet<>();
        JSONArray results = new JSONArray();
        try {
            results = getWikidataQueryResult("entity=" + id, "property=P31").getJSONArray("P31");
        } catch (JSONException ignored) { //no instances of P31 for this id, equivalent to empty array.
        }
        for (int i = 0; i < results.length(); i++) {
            try {
                properties.add(results.getJSONObject(i).
                        getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getString("id"));
            } catch (JSONException e) {
                Log.e(WIKIDATA_ERROR, "parsing properties for id " + id);
            }
        }
        return properties;
    }

    private static JSONObject getWikidataQueryResult(String... queryArgs) {
        String query = constructQuery("https://www.wikidata.org/w/api.php?action=wbgetclaims", queryArgs);
        try {
            return new JSONObject(executeWikiRequest(query)).getJSONObject("claims");
        } catch (JSONException e) {
            Log.e(WIKIDATA_ERROR, "for query " + query + ": " + e.getMessage());
        }
        return new JSONObject();
    }

    private static JSONObject getQueryResult(String... queryArgs) {
        String query = constructQuery("https://en.wikipedia.org/w/api.php?action=query", queryArgs);
        try {
            return new JSONObject(executeWikiRequest(query)).getJSONObject("query");
        } catch (JSONException e) {
            Log.e(WIKIPEDIA_ERROR, "for query " + query + ": " + e.getMessage());
        }
        return new JSONObject();
    }

    private static String executeWikiRequest(String query) {
        String searchResult = "";
        try {
            searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            Log.e(JSOUP_ERROR, e.getMessage() + " for query " + query);
        }
        return searchResult;
    }

    private static String constructQuery(String baseActionLink, String... args) {
        return Joiner.on('&').join(baseActionLink, "format=json", Joiner.on('&').join(args));
    }
}
