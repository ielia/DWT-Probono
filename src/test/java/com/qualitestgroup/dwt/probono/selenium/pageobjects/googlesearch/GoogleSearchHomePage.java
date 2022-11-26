package com.qualitestgroup.dwt.probono.selenium.pageobjects.googlesearch;

import com.qualitestgroup.dwt.probono.selenium.expectedconditions.MoreExpectedConditions;
import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GoogleSearchHomePage extends Page<GoogleSearchHomePage> {
    protected static int MAX_RETRIES = 3;
    protected static final Duration PAGE_LOADING_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration AUTOCOMPLETE_CLOSE_TIMEOUT = Duration.ofSeconds(2);
    protected static final Duration AUTOCOMPLETE_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration CLICKABLE_SEARCH_TIMEOUT = Duration.ofSeconds(2);
    public static final String URL = "https://www.google.com";

    @FindBy(name = "btnI")
    protected List<WebElement> feelingLuckyButtons;
    @FindBy(name = "btnK")
    protected List<WebElement> searchButtons;
    @FindBy(name = "q")
    protected WebElement searchInput;
    @FindBy(css = "[role='listbox']")
    protected WebElement autocompleteList;

    public GoogleSearchHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean assertCurrentPage() {
        return Page.assertCurrentPage(driver, URL, URL);
    }

    @Override
    public GoogleSearchHomePage go() {
        driver.get(URL);
        return this;
    }

    public void feelingLucky(String term) {
        prepareSearch(term);
        new WebDriverWait(driver, CLICKABLE_SEARCH_TIMEOUT)
            .until(MoreExpectedConditions.anyElementToBeClickable(feelingLuckyButtons)).click();
    }

    @SuppressWarnings("UnusedReturnValue")
    public GoogleSearchHomePage prepareSearch(String term) {
        new WebDriverWait(driver, PAGE_LOADING_TIMEOUT).until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.sendKeys(term);
        Wait<WebDriver> waitOpen = new WebDriverWait(driver, AUTOCOMPLETE_TIMEOUT);
        waitOpen.until(ExpectedConditions.visibilityOf(autocompleteList));
        boolean done = false;
        int tries = 0;
        Wait<WebDriver> waitClose = new WebDriverWait(driver, AUTOCOMPLETE_CLOSE_TIMEOUT);
        while (!done && tries++ <= MAX_RETRIES) {
            try {
                searchInput.sendKeys(Keys.ESCAPE);
                waitClose.until(ExpectedConditions.invisibilityOf(autocompleteList));
                done = true;
            } catch (TimeoutException exception) {
                if (tries > MAX_RETRIES) {
                    throw exception;
                }
            }
        }
        return this;
    }

    public GoogleSearchResultsPage search(String term) {
        prepareSearch(term);
        new WebDriverWait(driver, CLICKABLE_SEARCH_TIMEOUT)
                .until(MoreExpectedConditions.anyElementToBeClickable(searchButtons)).click();
        return new GoogleSearchResultsPage(driver, term);
    }
}
