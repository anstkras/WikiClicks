package ru.hse.wikiclicks.controllers;

import android.util.Log;

import org.jsoup.Connection;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;

public class OfflineController {
    public static final String CONFIRMATION = "wikiclicksDownloadedGame";

    public static void downloadTree(ChooseOfflineGame game, String outputDirectory, int confirmationNumber) {
        for (String title : game.getPages()) {
            try {
                downloadPage(title, outputDirectory);
            } catch (IOException first) {
                try {
                    Thread.sleep(1000);
                    downloadPage(title, outputDirectory);
                } catch (InterruptedException | IOException second) {
                    // well, it really doesn't work. fail without confirmation.
                    Log.e("Failed page download", title);
                    return;
                }
            }
        }
        downloadConfirmation(outputDirectory, confirmationNumber);
    }

    private static void downloadPage(String title, String outputDirectory) throws IOException {
        final File file = new File(outputDirectory, normalize(title));
        if (file.exists()) {
            return; //assume page already is downloaded
        }
        String url = "https://en.wikipedia.org/wiki/" + title;
        final Connection.Response response = Jsoup.connect(url).timeout(0).ignoreContentType(true).execute();
        String webPage = response.parse().html();
        //dirty fix for links, inter-page ones might not work
        webPage = webPage.replaceAll("href=\"", "href=\"" + "https://");
        FileUtils.writeStringToFile(file, webPage, "UTF-8");

    }

    public static String readPage(String title, String inputDirectory) throws IOException {
        return FileUtils.readFileToString(new File(inputDirectory, normalize(title)), "UTF-8");
    }

    public static String normalize(String title) {
        return title.replaceAll(" ", "_").toLowerCase();
    }

    private static void downloadConfirmation(String outputDirectory, int confirmationNumber) {
        final File file = new File(outputDirectory, CONFIRMATION + confirmationNumber);
        System.out.println(file.getAbsolutePath());
        try {
            FileUtils.writeStringToFile(file, "downloaded successfully", "UTF-8");
        } catch (IOException ignored) { //lack of confirmation will show that failure happened.
        }
    }
}
