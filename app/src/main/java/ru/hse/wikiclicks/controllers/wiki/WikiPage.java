package ru.hse.wikiclicks.controllers.wiki;

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

    /** Sets this WikiPage to be equal to the given one. */
    public void set(WikiPage other) {
        title = other.title;
        id = other.id;
    }

    /** Getter for title field. */
    public String getTitle() {
        return title;
    }

    /** Getter for id field. */
    public String getId() {
        return id;
    }

    /**  Method that sets the page reference to null. */
    public void clear() {
        this.title = null;
        this.id = null;
    }

    /** Returns the WikiPage in a user-friendly format, i.e. title only. */
    @Override
    public String toString() {
        return title;
    }
}
