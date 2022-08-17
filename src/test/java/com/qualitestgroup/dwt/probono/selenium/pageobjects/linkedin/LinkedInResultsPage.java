package com.qualitestgroup.dwt.probono.selenium.pageobjects.linkedin;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LinkedInResultsPage extends Page<LinkedInResultsPage> {
    protected static final Duration primaryFiltersTimeout = Duration.ofSeconds(15);
    protected static final Duration linksTimeout = Duration.ofSeconds(15);
    protected static final Pattern NUMBER_OF_RESULTS_PATTERN = Pattern.compile("([0-9,.]+)");
    protected static final String URL_PREFIX = "https://www.linkedin.com/search/results/";

    protected final String searchTerm;

    @FindBy(css = ".search-reusables__primary-filter")
    protected List<WebElement> primaryFilters;

    @FindBy(className = "search-results-container")
    protected WebElement numberOfResults;

    @FindBy(css = ".entity-result .entity-result__title-text a.app-aware-link")
    protected List<WebElement> links;

    public LinkedInResultsPage(WebDriver driver, String searchTerm) {
        super(driver);
        this.searchTerm = searchTerm;
    }

    @Override
    public boolean assertCurrentPage() {
        Wait<WebDriver> wait = new WebDriverWait(driver, primaryFiltersTimeout);
        wait.until(ExpectedConditions.visibilityOfAllElements(primaryFilters));
        return false;
    }

    @Override
    public LinkedInResultsPage go() {
        throw new RuntimeException("Not implemented yet. No, it is not a design error, it is only a time-constrained implementation");
    }

    public Map<String, URL> getLinks() {
        Wait<WebDriver> wait = new WebDriverWait(this.driver, linksTimeout);
        wait.until(ExpectedConditions.visibilityOfAllElements(links));
        Map<String, URL> results = new HashMap<>();
        for (WebElement link : links) {
            try {
                results.put(link.getText(), new URL(link.getAttribute("href")));
            } catch (MalformedURLException ignored) {
            }
        }
        return results;
    }

    public Integer getNumberOfResults() {
        // TODO: Use Locale, if page not locale-unfriendly.
        return Integer.valueOf(NUMBER_OF_RESULTS_PATTERN.matcher(
                numberOfResults.getText()).group(1).replaceAll("[,.]", ""));
    }

    public boolean isNumberOfResultsExact() {
        return numberOfResults.getText().matches("^[0-9]");
    }
}
