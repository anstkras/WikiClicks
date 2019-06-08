package ru.hse.wikiclicks.controllers.wiki;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class WikiControllerTest {

    @Test
    @Config(manifest = Config.NONE)
    public void testRandomDiffers() {
        Set<WikiPage> randomPages = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            randomPages.add(WikiController.getRandomPage());
        }
        assertTrue(randomPages.size() > 1);
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testWikipediaLinkCorrectness() {
        assertTrue(WikiController.isCorrectWikipediaLink("https://en.m.wikipedia.org/wiki/Demonophobia"));
        assertFalse(WikiController.isCorrectWikipediaLink("https://en.m.wikipedia.org/wiki/Wikipedia:Wiki_Game"));
        assertFalse(WikiController.isCorrectWikipediaLink("https://wiki.compscicenter.ru/index.php/WikiClicks"));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testWikipediaSearchSuggestions() {
        List<WikiPage> suggestions = WikiController.getSearchSuggestions("Julia Roberts");
        assertTrue(suggestions.size() > 0);
        assertEquals("Julia Roberts", suggestions.get(0).getTitle());
        assertEquals("16553", suggestions.get(0).getId());

        List<WikiPage> prefixSuggestions = WikiController.getSearchSuggestions("Julia Robert");
        assertTrue(prefixSuggestions.size() > 0);
        assertEquals("Julia Roberts", prefixSuggestions.get(0).getTitle());
        assertEquals("16553", prefixSuggestions.get(0).getId());

        List<WikiPage> differentSuggestions = WikiController.getSearchSuggestions("Queen");
        assertTrue(differentSuggestions.size() > 0);
        assertNotEquals("Julia Roberts", differentSuggestions.get(0).getTitle());
        assertNotEquals("16553", differentSuggestions.get(0).getId());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetPageFromUrl() {
        WikiPage elizabethPage = WikiController.getPageFromUrl("https://en.m.wikipedia.org/wiki/Queen_Elizabeth_The_Queen_Mother");
        assertEquals(elizabethPage.getId(), WikiController.getPageFromUrl("https://en.m.wikipedia.org/wiki/The_Queen_Mother").getId());
        assertEquals(elizabethPage.getId(), WikiController.getPageFromUrl("https://en.m.wikipedia.org/wiki/Her Majesty Queen Elizabeth The Queen Mother").getId());
        assertEquals(elizabethPage.getId(), WikiController.getPageFromUrl("https://en.m.wikipedia.org/wiki/Most dangerous woman in Europe").getId());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetPageTitleFromUrl() {
        String longUrl = "https://en.m.wikipedia.org/wiki/But_what_about_the_noise_of_crumpling_paper_which_he_used_to_do_in_order_to_paint_the_series_of_%22Papiers_froiss%C3%A9s%22_or_tearing_up_paper_to_make_%22Papiers_d%C3%A9chir%C3%A9s%3F%22_Arp_was_stimulated_by_water_(sea,_lake,_and_flowing_waters_like_rivers),_forests";
        String longTitle = "But what about the noise of crumpling paper which he used to do in order to paint the series of \"Papiers froissés\" or tearing up paper to make \"Papiers déchirés?\" Arp was stimulated by water (sea, lake, and flowing waters like rivers), forests";
        assertEquals(longTitle, WikiController.getPageTitleFromUrl(longUrl));

        String percentUrl = "https://en.m.wikipedia.org/wiki/%25_difference";
        String percentTitle = "% difference";
        assertEquals(percentTitle, WikiController.getPageTitleFromUrl(percentUrl));

        String normalUrl = "https://en.m.wikipedia.org/wiki/Apple";
        String normalTitle = "Apple";
        assertEquals(normalTitle, WikiController.getPageTitleFromUrl(normalUrl));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetRedirectedId() {
        assertEquals("46744", WikiController.getRedirectedId("55004309"));
        assertEquals("46744", WikiController.getRedirectedId("2450750"));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetExtract() {
        String appleExtract = "An apple is a sweet, edible fruit produced by an apple tree (Malus pumila). Apple trees are cultivated worldwide and are the most widely grown species in the genus Malus.";
        String appleIncExtract = "Apple Inc. is an American multinational technology company headquartered in Cupertino, California, that designs, develops, and sells consumer electronics, computer software, and online services.";
        assertEquals(appleExtract, WikiController.getExtract("18978754"));
        assertEquals(appleIncExtract, WikiController.getExtract("856"));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetLinksFromPage() {
        List<String> links = WikiController.getLinksFromPage("Demonophobia");
        assertTrue(links.contains("Demon"));
        assertTrue(links.contains("Specific phobia"));
        assertTrue(links.contains("Psychology"));
        assertTrue(links.contains("Christianity"));
        assertFalse(links.contains("Demonophobia"));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testGetLinksToPage() {
        List<String> links = WikiController.getLinksToPage("Leptobrachium guangxiense");
        assertTrue(links.contains("Megophryidae"));
        assertTrue(links.contains("Eastern spadefoot toad"));
        assertFalse(links.contains("Leptobrachium guangxiense"));
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testFailedQueries() {
        assertEquals("No information available.", WikiController.getExtract("-179"));
        assertEquals(0, WikiController.getLinksFromPage("-179").size());
        assertEquals(0, WikiController.getLinksToPage("-179").size());
    }
}
