package ru.hse.wikiclicks.controllers;

public class WikiPage {
    private String title;
    private String id;

    public WikiPage(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
