package ru.hse.wikiclicks.controllers;

/**  Class that stores data about a Wikipedia page. */
public class WikiPage {
    private String title;
    private String id;

    /** Empty constructor creates WikiPage that does not store data about any page. */
    public WikiPage() {
        clear();
    }

    /** Constructor that creates WikiPage based on the title and unique Wikipedia pageid. */
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

    /**  Method that sets the page reference to null. */
    public void clear() {
        this.title = null;
        this.id = null;
    }

    @Override
    public String toString() {
        return title;
    }
}
