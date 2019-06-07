package ru.hse.wikiclicks.controllers.download;

import android.util.Log;

import org.jsoup.Connection;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import ru.hse.wikiclicks.controllers.wiki.WikiController;

/** Class responsible for downloading a chosen offline game and formatting its pages to display well. */
public class DownloadController {
    /** File to be downloaded when pages' download has ended, used as a check if there has been a successful download. */
    public static final String CONFIRMATION = "wikiclicksDownloadedGame";
    private static final int FAIL_MARGIN = 15;

    /**
     * Downloads the chosen game to the given directory and saves a file as confirmation if download succeeded.
     * @param game the game to download.
     * @param outputDirectory the directory to download the game to.
     * @param confirmationNumber the number of the download, a second download with the same number will not be attempted.
     */
    public static void downloadTree(OfflineGameSelector game, String outputDirectory, int confirmationNumber) {
        if (game.hasFailed()) {
            return;
        }
        int fails = 0;
        for (String title : game.getPages()) {
            if (!downloadPage(title, outputDirectory)) {
                if (++fails >= FAIL_MARGIN) { // downloads are failing en masse, stop trying
                    return;
                }
            }
        }
        downloadConfirmation(outputDirectory, confirmationNumber);
    }

    /**
     * Saves the Wikipedia page with the given title into the given directory.
     * The name of the resulting file is the title, formatted. The file has no extensions.
     * @param title a Wikipedia page to save.
     * @param outputDirectory the directory to save the page to.
     * @return false if the page download failed, true otherwise.
     */
    private static boolean downloadPage(String title, String outputDirectory) {
        String url = WikiController.getUrlForTitle(title);
        String titleForWebPage = WikiController.getPageTitleFromUrl(url);
        final File file = new File(outputDirectory, titleForWebPage);
        if (file.exists()) {
            return true; //assume page already is downloaded
        }
        final Connection connection = Jsoup.connect(url).timeout(0).ignoreContentType(true);
        try {
            String webPage = connection.execute().parse().html();
            FileUtils.writeStringToFile(file, removeExtras(webPage), "UTF-8");
            return true;
        } catch (HttpStatusException e) {
            Log.e("Failed parsing page", titleForWebPage + ": " + e.getMessage());
        } catch (IOException e) {
            Log.e("Failed page download", title + ": " + e.getMessage());
        }
        return false;
    }

    /** Removes unneeded elements from the downloaded html. */
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

    /** Reads the page with the given title from the given directory. */
    public static String readPage(String title, String inputDirectory) throws IOException {
        return FileUtils.readFileToString(new File(inputDirectory, title), "UTF-8");
    }

    /** Saves a confirmation file with the given number to the outputDirectory. */
    private static void downloadConfirmation(String outputDirectory, int confirmationNumber) {
        final File file = new File(outputDirectory, CONFIRMATION + confirmationNumber);
        try {
            FileUtils.writeStringToFile(file, "downloaded successfully", "UTF-8");
        } catch (IOException ignored) { //lack of confirmation will show that failure happened.
        }
    }

    /** Checks whether a game with the given confirmation number exists in the directory. */
    public static boolean checkConfirmation(String downloadDirectory, int confirmationNumber) {
        File hasDownloadHappened = new File(downloadDirectory, CONFIRMATION + confirmationNumber);
        return hasDownloadHappened.exists();
    }
}
