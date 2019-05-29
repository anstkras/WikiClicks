package ru.hse.wikiclicks.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ChooseOfflineGame {
    private final int tree_size;
    private HashSet<String> pages = new HashSet<>();

    public HashSet<String> getPages() {
        return pages;
    }

    public String getStartPageName() {
        return startPageName;
    }

    public String getEndPageName() {
        return endPageName;
    }

    private ArrayList<String> edgePages = new ArrayList<>();
    private String startPageName;
    private String endPageName;

    public ChooseOfflineGame(String startPageName, String endPageName, int distance) {
        tree_size = distance;
        this.startPageName = startPageName;
        this.endPageName = endPageName;
        initializeGame();
        pages.add(endPageName);
    }

    private String chooseRandomPossibleEndPage() {
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

    private void initializeGame() {
        pages.clear();
        edgePages.clear();
        getLinksTree(startPageName, tree_size);
    }

    private void getLinksTree(String page, int depth) {
        if (depth == 0 || pages.contains(page)) {
            return;
        }
        if (depth == 1) {
            edgePages.add(page);
        }
        pages.add(page);
        ArrayList<String> links = WikiController.getLinksFromPage(page);
        for (String newPageTitle : links) {
            getLinksTree(newPageTitle, depth - 1);
        }
    }
}
