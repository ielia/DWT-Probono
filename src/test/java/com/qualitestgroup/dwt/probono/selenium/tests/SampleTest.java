package com.qualitestgroup.dwt.probono.selenium.tests;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.googlesearch.GoogleSearchHomePage;
import com.qualitestgroup.dwt.probono.selenium.pageobjects.googlesearch.GoogleSearchResultsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;

public class SampleTest extends TestNGTest {
    protected static final Duration FEELING_LUCKY_URL_CHANGE_TIMEOUT = Duration.ofSeconds(10);
    protected static final NumberFormat NUMBER_FORMATTER = new DecimalFormat("#,###");

    @DataProvider(name = "fruits", parallel = true)
    public Object[][] data() {
        return new Object[][]{
                new Object[]{"banana"}, new Object[]{"mango"}, new Object[]{"kiwi"}, new Object[]{"melon"},
                new Object[]{"papaya"}
        };
    }

    @Test(dataProvider = "fruits", testName = "Google Search '%s' returns results")
    public void google_search_fruit_returns_results(String fruit) {
        GoogleSearchResultsPage resultsPage =
                new GoogleSearchHomePage(getDriver())
                        .go()
                        .search(fruit);

        Assert.assertTrue(getDriver().getTitle().contains(fruit),
                String.format("Results page title did not contain '%s'.", fruit));

        Reporter.log(String.format("# of %ss in the whole Internet: %s.", fruit,
                NUMBER_FORMATTER.format(resultsPage.getNumberOfResults())), true);
    }

    @Test(dataProvider = "fruits", testName = "Google Search Feeling Lucky with '%s' goes to an external page")
    public void google_feeling_lucky_goes_to_an_external_page(String fruit) {
        new GoogleSearchHomePage(getDriver())
                .go()
                .feelingLucky(fruit);

        new WebDriverWait(getDriver(), FEELING_LUCKY_URL_CHANGE_TIMEOUT).until(
                driver -> driver.getCurrentUrl() != null && !driver.getCurrentUrl().startsWith(GoogleSearchHomePage.URL)
        );

        Reporter.log(
                String.format("Feeling Lucky at \"%s\" (\"%s\").", getDriver().getTitle(), getDriver().getCurrentUrl()),
                true
        );
    }
}
