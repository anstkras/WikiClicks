package ru.hse.wikiclicks.controllers;

import org.jsoup.Connection;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class OfflineController {
    private static String outputDirectory;

    public static void downloadTree(ChooseOfflineGame game) {
        for (String title : game.getPages()) {
            downloadPage(title);
        }
    }

    private static void downloadPage(String title) {
        try {
            final File file = new File(outputDirectory, normalize(title));
            if (file.exists()) {
                return; //assume page already is downloaded
            }
            String url = "https://en.m.wikipedia.org/wiki/" + title;
            final Connection.Response response = Jsoup.connect(url).timeout(0).ignoreContentType(true).execute();
            String webPage = response.parse().html();
            //dirty fix for links, inter-page ones might not work
            webPage = webPage.replaceAll("href=\"", "href=\"" + "https://");
            FileUtils.writeStringToFile(file, webPage, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readPage(String title) throws IOException {
        return FileUtils.readFileToString(new File(outputDirectory, normalize(title)), "UTF-8");
    }

    public static String normalize(String title) {
        return title.replaceAll(" ", "_").toLowerCase();
    }

    public static void setOutputDirectory(String directory) {
        outputDirectory = directory;
    }

    public static String getOutputDirectory() {
        return outputDirectory;
    }
}


