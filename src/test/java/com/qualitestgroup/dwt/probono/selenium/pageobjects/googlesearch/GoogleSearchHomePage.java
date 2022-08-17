package com.qualitestgroup.dwt.probono.selenium.pageobjects.googlesearch;

import com.qualitestgroup.dwt.probono.selenium.expectedconditions.MoreExpectedConditions;
import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GoogleSearchHomePage extends Page<GoogleSearchHomePage> {
    protected static final Duration PAGE_LOADING_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration AUTOCOMPLETE_TIMEOUT = Duration.ofSeconds(10);
    protected static final Duration CLICKABLE_SEARCH_TIMEOUT = Duration.ofSeconds(2);
    public static final String URL = "https://www.google.com";

    @FindBy(name = "btnI")
    protected List<WebElement> feelingLuckyButtons;
    @FindBy(name = "btnK")
    protected List<WebElement> searchButtons;
    @FindBy(name = "q")
    protected WebElement searchInput;
    @FindBy(css = "[role='listbox'] [role='listbox']")
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
        Wait<WebDriver> wait = new WebDriverWait(driver, AUTOCOMPLETE_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOf(autocompleteList));
        searchInput.sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOf(autocompleteList));
        return this;
    }

    public GoogleSearchResultsPage search(String term) {
        prepareSearch(term);
        new WebDriverWait(driver, CLICKABLE_SEARCH_TIMEOUT)
                .until(MoreExpectedConditions.anyElementToBeClickable(searchButtons)).click();
        return new GoogleSearchResultsPage(driver, term);
    }
}
