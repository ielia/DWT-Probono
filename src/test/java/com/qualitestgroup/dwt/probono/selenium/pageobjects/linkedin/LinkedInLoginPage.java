package com.qualitestgroup.dwt.probono.selenium.pageobjects.linkedin;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LinkedInLoginPage extends Page<LinkedInLoginPage> {
    protected static final String URL = "https://www.linkedin.com/login";

    @FindBy(id="username")
    protected WebElement usernameInput;

    @FindBy(id="password")
    protected WebElement passwordInput;

    @FindBy(css="button[type='submit']")
    protected WebElement submitButton;

    public LinkedInLoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean assertCurrentPage() {
        return Page.assertCurrentPage(driver, URL, URL);
    }

    @Override
    public LinkedInLoginPage go() {
        this.driver.get(URL);
        return this;
    }

    public LinkedInFeedPage login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        submitButton.click();
        LinkedInFeedPage feedPage = new LinkedInFeedPage(driver);
        feedPage.assertCurrentPage();
        return feedPage;
    }
}
