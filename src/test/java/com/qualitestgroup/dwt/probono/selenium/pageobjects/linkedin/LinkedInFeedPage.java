package com.qualitestgroup.dwt.probono.selenium.pageobjects.linkedin;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LinkedInFeedPage extends Page<LinkedInFeedPage> {
    protected static final String URL = "https://www.linkedin.com/feed";

    @FindBy(className="search-global-typeahead__input")
    protected WebElement searchInput;

    public LinkedInFeedPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean assertCurrentPage() {
        return Page.assertCurrentPage(driver, URL, URL);
    }

    @Override
    public LinkedInFeedPage go() {
        driver.get(URL);
        return this;
    }

    public LinkedInResultsPage search(String term) {
        searchInput.sendKeys(term);
        searchInput.sendKeys(Keys.ENTER);
        return new LinkedInResultsPage(driver, term);
    }
}
