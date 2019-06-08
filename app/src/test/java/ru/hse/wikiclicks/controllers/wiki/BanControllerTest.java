package ru.hse.wikiclicks.controllers.wiki;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BanControllerTest {
    @Test
    @Config(manifest = Config.NONE)
    public void testCountriesAreCountriesSimple() {
        BanController russianController = new BanController(WikiController.getUrlForTitle("Russia"));
        assertTrue(russianController.isCountry());

        BanController swissController = new BanController(WikiController.getUrlForTitle("Switzerland"));
        assertTrue(swissController.isCountry());

        BanController ukController = new BanController(WikiController.getUrlForTitle("UK"));
        assertTrue(ukController.isCountry());

        BanController afghanController = new BanController(WikiController.getUrlForTitle("Afghanistan"));
        assertTrue(afghanController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testCountriesAreCountriesFictional() {
        BanController wakandanController = new BanController(WikiController.getUrlForTitle("Wakanda"));
        assertTrue(wakandanController.isCountry());

        BanController absurdController = new BanController(WikiController.getUrlForTitle("Absurdistan"));
        assertTrue(absurdController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testCountriesAreCountriesPreviouslyExisting() {
        BanController ussrController = new BanController(WikiController.getUrlForTitle("USSR"));
        assertTrue(ussrController.isCountry());

        BanController chinaController = new BanController(WikiController.getUrlForTitle("Republic_of_China_(1912–1949)"));
        assertTrue(chinaController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotCountriesAreNotCountriesSimple() {
        BanController appleController = new BanController(WikiController.getUrlForTitle("Apple"));
        assertFalse(appleController.isCountry());

        BanController yodaController = new BanController(WikiController.getUrlForTitle("Apple"));
        assertFalse(yodaController.isCountry());

        BanController randomController = new BanController(WikiController.getUrlForTitle("What's on Tonight"));
        assertFalse(randomController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotCountriesAreNotCountriesUnrecognized() {
        BanController transnistriaController = new BanController(WikiController.getUrlForTitle("Transnistria"));
        assertFalse(transnistriaController.isCountry());

        BanController nkController = new BanController(WikiController.getUrlForTitle("Nagorno-Karabakh"));
        assertFalse(nkController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotCountriesAreNotCountriesCountryMusic() {
        BanController countryController = new BanController(WikiController.getUrlForTitle("Country music"));
        assertFalse(countryController.isCountry());

        BanController alanController = new BanController(WikiController.getUrlForTitle("Alan Jackson"));
        assertFalse(alanController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotCountriesAreNotCountriesOtherGeography() {
        BanController fifthController = new BanController(WikiController.getUrlForTitle("Fifth Avenue"));
        assertFalse(fifthController.isCountry());

        BanController manhattanController = new BanController(WikiController.getUrlForTitle("Manhattan"));
        assertFalse(manhattanController.isCountry());

        BanController nycController = new BanController(WikiController.getUrlForTitle("New York City"));
        assertFalse(nycController.isCountry());

        BanController nysController = new BanController(WikiController.getUrlForTitle("New York (State)"));
        assertFalse(nysController.isCountry());

        BanController americaController = new BanController(WikiController.getUrlForTitle("North America"));
        assertFalse(americaController.isCountry());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testYearsAreYearsSimple() {
        BanController mmController = new BanController(WikiController.getUrlForTitle("2000"));
        assertTrue(mmController.isYear());

        BanController mmxixController = new BanController(WikiController.getUrlForTitle("2019"));
        assertTrue(mmxixController.isYear());

        BanController mcclxxivController = new BanController(WikiController.getUrlForTitle("1274"));
        assertTrue(mcclxxivController.isYear());

        BanController bcController = new BanController(WikiController.getUrlForTitle("540 BC"));
        assertTrue(bcController.isYear());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotYearsAreNotYearsSimple() {
        BanController appleController = new BanController(WikiController.getUrlForTitle("Apple"));
        assertFalse(appleController.isYear());

        BanController yodaController = new BanController(WikiController.getUrlForTitle("Apple"));
        assertFalse(yodaController.isYear());

        BanController randomController = new BanController(WikiController.getUrlForTitle("What's on Tonight"));
        assertFalse(randomController.isYear());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotYearsAreNotYearsOtherTimePeriods() {
        BanController centuryController = new BanController(WikiController.getUrlForTitle("7th century BC"));
        assertFalse(centuryController.isYear());

        BanController secondController = new BanController(WikiController.getUrlForTitle("Second"));
        assertFalse(secondController.isYear());

        BanController periodController = new BanController(WikiController.getUrlForTitle("Regency"));
        assertFalse(periodController.isYear());

        BanController decadeController = new BanController(WikiController.getUrlForTitle("1970s"));
        assertFalse(decadeController.isYear());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testNotYearsAreNotYearsTitlesWithYearInThem() {
        BanController sep11Controller = new BanController(WikiController.getUrlForTitle("9/11/2001"));
        assertFalse(sep11Controller.isYear());

        BanController electionController = new BanController(WikiController.getUrlForTitle("2016_United_States_presidential_election"));
        assertFalse(electionController.isYear());

        BanController fashionController = new BanController(WikiController.getUrlForTitle("1990s_in_fashion"));
        assertFalse(fashionController.isYear());
    }

    @Test
    @Config(manifest = Config.NONE)
    public void testCountriesAreNotYearsAndYearsAreNotCountries() {
        BanController ukController = new BanController(WikiController.getUrlForTitle("UK"));
        assertFalse(ukController.isYear());

        BanController afghanController = new BanController(WikiController.getUrlForTitle("Afghanistan"));
        assertFalse(afghanController.isYear());

        BanController mmxixController = new BanController(WikiController.getUrlForTitle("2019"));
        assertFalse(mmxixController.isCountry());

        BanController bcController = new BanController(WikiController.getUrlForTitle("540 BC"));
        assertFalse(bcController.isCountry());
    }
}
