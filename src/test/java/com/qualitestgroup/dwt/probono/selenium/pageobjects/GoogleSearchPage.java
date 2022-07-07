package com.qualitestgroup.dwt.probono.selenium.pageobjects;

import com.qualitestgroup.dwt.probono.selenium.expectedconditions.MoreExpectedConditions;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GoogleSearchPage extends Page<GoogleSearchPage> {
    public static final String URL = "https://www.google.com";

    @FindBy(name = "btnK")
    protected List<WebElement> searchButtons;
    @FindBy(name = "q")
    protected WebElement searchInput;
    @FindBy(css = "[role='listbox'] [role='listbox']")
    protected WebElement autocompleteList;

    public GoogleSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public GoogleSearchPage go() {
        this.driver.get(URL);
        // return this.init();
        return this;
    }

    public GoogleSearchResultsPage search(String term) {
        Wait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(this.searchInput));
        this.searchInput.sendKeys(term);
        wait.until(ExpectedConditions.visibilityOf(this.autocompleteList));
        this.searchInput.sendKeys(Keys.ESCAPE);
        wait.until(ExpectedConditions.invisibilityOf(this.autocompleteList));
        wait.until(MoreExpectedConditions.anyElementToBeClickable(this.searchButtons)).click();
        return new GoogleSearchResultsPage(this.driver, term);
    }
}
