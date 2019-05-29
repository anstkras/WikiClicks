package ru.hse.wikiclicks.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ChooseOfflineGame {
    private static final int TREE_SIZE = 2;
    public HashSet<String> pages = new HashSet<>();
    ArrayList<String> edgePages = new ArrayList<>();
    public String startPageName;
    public String endPageUrl = "https://en.m.wikipedia.org/wiki/";

    public void chooseRandomStartPage() {
        pages.clear();
        edgePages.clear();
//        WikiPage startPage = WikiController.getRandomPage();
//        startPageName = startPage.getTitle();
        startPageName = "Demonophobia";
        System.out.println(startPageName);
        getLinksTree(startPageName, TREE_SIZE);
        System.out.println(startPageName);
        System.out.println(pages.size());

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
        endPageUrl = endPageUrl + finalPage;
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
