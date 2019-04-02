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
        String query = "https://en.wikipedia.org/w/api.php?action=query&list=search&prop=info&inprop=url&utf8=&format=json&origin=*&srlimit=30&srsearch=" + prefix;
        try {
            String searchResult = Jsoup.connect(query).timeout(0).ignoreContentType(true).execute().body();
            System.out.println(searchResult);
            JSONObject json = new JSONObject(searchResult);
            JSONArray results = json.getJSONObject("query").getJSONArray("search");
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
}
