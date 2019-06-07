package ru.hse.wikiclicks.controllers.download;

import android.util.Log;

import org.jsoup.Connection;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import ru.hse.wikiclicks.controllers.wiki.WikiController;

public class OfflineController {
    public static final String CONFIRMATION = "wikiclicksDownloadedGame";
    private static final int FAIL_MARGIN = 10;

    public static void downloadTree(ChooseOfflineGame game, String outputDirectory, int confirmationNumber) {
        if (game.hasFailed()) {
            return;
        }
        int fails = 0;
        for (String title : game.getPages()) {
            try {
                downloadPage(title, outputDirectory);
            } catch (IOException e) {
                Log.e("Failed page download", title);
                if (++fails >= FAIL_MARGIN) {
                    return;
                }
            }
        }
        downloadConfirmation(outputDirectory, confirmationNumber);
    }

    private static void downloadPage(String unformattedTitle, String outputDirectory) throws IOException {
        String url = WikiController.getUrlForTitle(unformattedTitle);
        String title = WikiController.getPageTitleFromUrl(url);
        final File file = new File(outputDirectory, title);
        if (file.exists()) {
            return; //assume page already is downloaded
        }
        final Connection connection = Jsoup.connect(url).timeout(0).ignoreContentType(true);
        try {
            String webPage = connection.execute().parse().html();
            FileUtils.writeStringToFile(file, removeExtras(webPage), "UTF-8");
        } catch (HttpStatusException e) {
            Log.e("Failed parsing page", title + ": " + e.getMessage());
        }
    }

    private static String removeExtras(String html) {
        String addStyle = " style=\"display:none;\"";

        // break all links on page: necessary for simple WebView clicking.
        String removedLinks =  html.replaceAll("href=\"", "href=\"" + "https://en.m.wikipedia.org");

        // remove links to references from main text to avoid clicking on them.
        String removedReferences = removedLinks.replaceAll("<sup", "<sup" + addStyle);

        // remove extra elements from the top of the page.
        String removedMainMenu = removedReferences.replaceAll("id=\"mw-mf-main-menu-button\"",
                "id=\"mw-mf-main-menu-button\"" + addStyle);
        String removedBrandingBox = removedMainMenu.replaceAll("class=\"branding-box\"",
                "class=\"branding-box\"" + addStyle);
        String removedSearchBox = removedBrandingBox.replaceAll("class=\"search-box\"",
                "class=\"search-box\"" + addStyle);
        String removedSearchIcon = removedSearchBox.replaceAll("id=\"searchIcon\"",
                "id=\"searchIcon\"" + addStyle);
        String removedActionsMenu = removedSearchIcon.replaceAll("class=\"page-actions-menu\"",
                "class=\"page-actions-menu\"" + addStyle);

        // remove Edit button
        String removedEditButtons = removedActionsMenu.replaceAll("span class=\"mw-editsection\"",
                "span class=\"mw-editsection\"" + addStyle);

//        remove uplinks
        String removedUplinks = removedEditButtons.replaceAll("<span class=\"mw-cite-backlink\"",
                "<span class=\"mw-cite-backlink\"" + addStyle);

        // remove "Retrieved from info"
        String removedRetrievedFrom = removedUplinks.replaceAll("class=\"printfooter\"",
                "class=\"printfooter\"" + addStyle);
        return removedRetrievedFrom;
    }

    public static String readPage(String title, String inputDirectory) throws IOException {
        return FileUtils.readFileToString(new File(inputDirectory, title), "UTF-8");
    }

    private static void downloadConfirmation(String outputDirectory, int confirmationNumber) {
        final File file = new File(outputDirectory, CONFIRMATION + confirmationNumber);
        try {
            FileUtils.writeStringToFile(file, "downloaded successfully", "UTF-8");
        } catch (IOException ignored) { //lack of confirmation will show that failure happened.
        }
    }
}
