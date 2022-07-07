package com.qualitestgroup.dwt.probono.selenium.tests;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.GoogleSearchPage;
import com.qualitestgroup.dwt.probono.selenium.pageobjects.GoogleSearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.text.DecimalFormat;

public class SampleTest extends TestNGTest {
    DecimalFormat formatter = new DecimalFormat("#,###.00");

    @DataProvider(name = "fruits")
    public Object[][] data() {
        return new Object[][] { new Object[] { "banana" }, new Object[] { "mango" } };
    }

    @Test(dataProvider = "fruits", testName="Google Search '%s' returns results")
    public void google_search_fruit_returns_results(String fruit) {
        GoogleSearchPage searchPage = new GoogleSearchPage(this.getDriver());
        searchPage.go();

        GoogleSearchResultsPage resultsPage = searchPage.search(fruit);

        Assert.assertTrue(this.getDriver().getTitle().contains(fruit),
                String.format("Results page title did not contain '%s'.", fruit));

        System.out.printf("# of %ss in the whole Internet: %s%n", fruit,
                formatter.format(resultsPage.getNumberOfResults()));
    }
}
