package ru.hse.wikiclicks.controllers;

import android.net.Uri;

public class MainController {
    public static String getRandomPageLink() {
        return "https://en.wikipedia.org/wiki/Special:Random";
    }

    public static boolean isCorrectWikipediaLink(String url) {
        return Uri.parse(url).getHost().equals("en.m.wikipedia.org");
    }
}
