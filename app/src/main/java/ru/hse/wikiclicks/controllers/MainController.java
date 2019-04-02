package ru.hse.wikiclicks.controllers;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainController {
    public static String getRandomPageLink() {
        return "https://en.m.wikipedia.org/wiki/Special:Random";
    }

    public static WikiPage getRandomPage() {
        String query = "https://en.wikipedia.org/w/api.php?action=query&list=random&format=json&rnnamespace=0&rnlimit=1";
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            System.out.println(searchResult);
            JSONObject json = new JSONObject(searchResult);
            JSONObject result = json.getJSONObject("query").getJSONArray("random").getJSONObject(0);
            return new WikiPage(result.getString("title"), result.getString("id"));
        } catch (IOException e) {
            System.out.println("Internet connection failed.");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON failed");
            e.printStackTrace();
        }
        return new WikiPage("Avatar (band)","26296973");
    }

    public static boolean isCorrectWikipediaLink(String url) {
        return "en.m.wikipedia.org".equals(Uri.parse(url).getHost());
    }

    public static String getPageLinkById(String id) {
        return "https://en.m.wikipedia.org/?curid=" + id;
    }

    public static List<WikiPage> getSearchSuggestions(String prefix) {
        String query = "https://en.wikipedia.org/w/api.php?action=query&list=prefixsearch&prop=info&inprop=url&utf8=&format=json&origin=*&pslimit=15&pssearch=" + prefix;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            System.out.println(searchResult);
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
            System.out.println("Internet connection failed.");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON failed");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String getURLForId(String id) {
        String query = "https://en.wikipedia.org/w/api.php?action=query&prop=info&inprop=url&format=json&pageids=" + id;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            System.out.println(searchResult);
            JSONObject json = new JSONObject(searchResult);
            String desktopURL = json.getJSONObject("query").getJSONObject("pages").getJSONObject(id).getString("canonicalurl");
            return desktopURL.replace("en.wikipedia", "en.m.wikipedia");
        } catch (IOException e) {
            System.out.println("Internet connection failed.");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON failed");
            e.printStackTrace();
        }
        return "";
    }

    public static String getExtract(String id) {
        String query = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exlimit=1&exsentences=3&continue=&explaintext=1&format=json&formatversion=2&pageids=" + id;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            JSONObject json = new JSONObject(searchResult);
            return json.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
        } catch (IOException e) {
            System.out.println("Internet connection failed.");
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("JSON failed");
            e.printStackTrace();
        }
        return "";
    }
}
