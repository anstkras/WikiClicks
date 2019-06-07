package ru.hse.wikiclicks.controllers.download;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import ru.hse.wikiclicks.controllers.wiki.WikiController;

import static com.google.android.gms.common.internal.Preconditions.checkArgument;

/** Class responsible for generating the offline game, including the list of necessary pages to download. */
public class OfflineGameSelector {
    private static final int PAGE_TREE_GENERATION_WORKED = 10;
    private static final int PAGE_RANDOM_CONSTANT = 42;
    private static final int PAGE_SELECTION_PROBABILITY = 6;

    private static final int MIN_TREE_SIZE = 1;
    private static final int MAX_TREE_SIZE = 3;
    private final int tree_size;
    private HashSet<String> pages = new HashSet<>();

    private String startPageName;
    private String endPageName;

    /**
     * Creates offline game from the given start page to the given end page, assuming given distance between them.
     * @param startPageName the starting page's name.
     * @param endPageName the end page's name, as it links by some shortest path from the starting page.
     * @param distance the distance between start and end pages. Should be no more than MAX_TREE_SIZE.
     */
    public OfflineGameSelector(String startPageName, String endPageName, int distance) {
        checkArgument(MIN_TREE_SIZE <= distance && distance <= MAX_TREE_SIZE);
        tree_size = distance;
        this.startPageName = startPageName;
        this.endPageName = endPageName;
        if (tree_size == MIN_TREE_SIZE) {
            getExtendedLinksTree();
        } else if (tree_size == MAX_TREE_SIZE) {
            getShortenedLinksTree();
        } else {
            getLinksTree(startPageName, tree_size);
        }
        pages.add(endPageName);
    }

    /** Method that was used to generate the levels locally. Provides a random end page for the normal link tree. */
    private String chooseRandomPossibleEndPage() {
        ArrayList<String> edgePages = new ArrayList<>(pages);
        Collections.shuffle(edgePages);
        ArrayList<String> finalLinks = WikiController.getLinksFromPage(edgePages.get(0));
        Collections.shuffle(finalLinks);
        String finalPage = finalLinks.get(finalLinks.size() - 1);
        for (int i = 0; i < finalLinks.size(); i++) {
            if (!pages.contains(finalLinks.get(i))) {
                finalPage = finalLinks.get(i);
                break;
            }
        }
        return finalPage;
    }

    /**
     * Link tree generation for link tree of depth one. Use with caution.
     * While normally a link tree of depth one is not enough to pose a challenge, the absence of a search bar in the app
     * makes it possible to choose a large enough page not obviously connected to the end page to make this interesting.
     * Apart from the original page, this method also randomly downloads several neighboring pages to confuse players,
     * and hopefully make them believe there is some smart algorithm that found a larger path and downloaded the necessary pages.
     */
    private void getExtendedLinksTree() {
        Random random = new Random(PAGE_RANDOM_CONSTANT);
        ArrayList<String> links = WikiController.getLinksFromPage(startPageName);
        if (links.size() > 0) { //check page exists
            pages.add(startPageName);
        }
        for (String newPageTitle : links) {
            if (random.nextInt() % PAGE_SELECTION_PROBABILITY == 0) {
                pages.add(newPageTitle);
            }
        }
    }

    /**
     * Standard link tree generation.
     * In practice, a link tree of depth two is small enough to download easily, but tricky enough to be annoying.
     * As such, this method is the base one to use and most games should be generated with this option.
     * @param page the current Wikipedia page.
     * @param depth the current depth of the tree. Original depth for this method should be 2.
     */
    private void getLinksTree(String page, int depth) {
        if (depth == 0 || pages.contains(page)) {
            return;
        }
        pages.add(page);
        if (depth != 1) {
            ArrayList<String> links = WikiController.getLinksFromPage(page);
            for (String newPageTitle : links) {
                getLinksTree(newPageTitle, depth - 1);
            }
        }
    }

    /**
     * Link tree generation for large trees, optimized to avoid saving too many pages.
     * Downloads a smaller link tree from the starting page, as well as pages that link to the end page.
     */
    private void getShortenedLinksTree() {
        getLinksTree(startPageName, tree_size - 1);
        pages.addAll(WikiController.getLinksToPage(endPageName));
    }

    /** A getter for the set of generated link titles. */
    public HashSet<String> getPages() {
        return pages;
    }

    /**
     * Checks whether a tree of acceptable size has been collected.
     * A small tree is a sign of a lacking internet connection or a boring game and shouldn't be used.
     */
    public boolean hasFailed() {
        return pages.size() <= PAGE_TREE_GENERATION_WORKED;
    }
}
