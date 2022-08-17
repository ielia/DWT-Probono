package com.qualitestgroup.dwt.probono.selenium.pageobjects.linkedin;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LinkedInGuestHomePage extends Page<LinkedInGuestHomePage> {
    protected static final String URL = "https://www.linkedin.com/";

    @FindBy(css="[data-tracking-control-name='guest_homepage-basic_nav-header-signin']")
    protected WebElement signInButton;

    public LinkedInGuestHomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean assertCurrentPage() {
        return Page.assertCurrentPage(driver, URL, URL);
    }

    @Override
    public LinkedInGuestHomePage go() {
        driver.get(URL);
        return this;
    }

    public LinkedInLoginPage goSignIn() {
        signInButton.click();
        return new LinkedInLoginPage(driver);
    }
}
