package com.qualitestgroup.dwt.probono.selenium.pageobjects;

import com.qualitestgroup.dwt.probono.selenium.exceptions.InvalidPageException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Page Object representing a full Page.
 * @param <P> Same type, so that the return type will be set to the corresponding page.
 */
public abstract class Page<P extends Page<P>> {
    /**
     * The Web Driver that has or will navigate here.
     */
    protected WebDriver driver;

    /**
     * Asserts that the current page in the driver matches the expected URL.
     * @param driver             A valid WebDriver instance (usually found in the page's instance).
     * @param expectedURLPrefix  The URL prefix (usually the full URL without its query part).
     * @param expectedExampleURL Example of the expected full URL, merely informative, not to be checked against.
     * @return True if current page corresponds to this instance, throw an exception if not.
     * @throws InvalidPageException In case the current driver's page does not correspond to this instance.
     */
    public static boolean assertCurrentPage(WebDriver driver, String expectedURLPrefix, String expectedExampleURL) {
        String currentURL = driver.getCurrentUrl().split("\\?")[0];
        if (!currentURL.startsWith(expectedURLPrefix)
                || currentURL.length() > expectedURLPrefix.length() + 1
                || (currentURL.length() == expectedURLPrefix.length() + 1 && !currentURL.endsWith("/"))) {
            throw new InvalidPageException("Should be at \"" + expectedExampleURL + "\", but it is at " +
                    driver.getCurrentUrl());
        }
        return true;
    }

    /**
     * @param driver Web Driver to be used to navigate here and to initialise elements.
     */
    public Page(WebDriver driver) {
        this.driver = driver;
        this.init();
    }

    /**
     * Asserts that the current page in the driver is this very same page.
     * @return True if current page corresponds to this instance, throw an exception if not.
     * @throws InvalidPageException In case the current driver's page does not correspond to this instance.
     */
    @SuppressWarnings("UnusedReturnValue")
    public abstract boolean assertCurrentPage();

    /**
     * Go here, i.e., make the Web Driver open this page in the current browser window/tab.
     * @return This instance.
     */
    @SuppressWarnings("UnusedReturnValue")
    public abstract P go();

    /**
     * Initialise page elements.
     * @return This instance.
     */
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    protected P init() {
        PageFactory.initElements(this.driver, (P) this);
        return (P) this;
    }
}
