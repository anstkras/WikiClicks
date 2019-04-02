package ru.hse.wikiclicks.controllers;

public class WikiPage {
    private String title;
    private String id;

    public WikiPage() {
        clear();
    }

    public WikiPage(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public void set(WikiPage other) {
        title = other.title;
        id = other.id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void clear() {
        this.title = null;
        this.id = null;
    }
}
