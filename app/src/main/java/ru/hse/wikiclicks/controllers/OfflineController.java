package ru.hse.wikiclicks.controllers;

import org.jsoup.Connection;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class OfflineController {
    String outputDirectory;

    public OfflineController(ChooseOfflineGame game, String directory) {
        outputDirectory = directory;
        for (String title : game.pages) {
            downloadPage(title);
        }
    }

    private void downloadPage(String title) {
        try {
            String url = "https://en.m.wikipedia.org/wiki/" + title;
            final Connection.Response response = Jsoup.connect(url).timeout(0).ignoreContentType(true).execute();
            String webPage = response.parse().html();
            //dirty fix for links, inter-page ones might not work
            System.out.println(normalize(title));
            webPage = webPage.replaceAll("href=\"", "href=\"" + "https://");
            final File file = new File(outputDirectory, normalize(title));
            FileUtils.writeStringToFile(file, webPage, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String normalize(String title) {
        return title.replaceAll(" ", "_").toLowerCase();
    }
}


