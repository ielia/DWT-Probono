package com.qualitestgroup.dwt.probono.selenium.expectedconditions;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

public class MoreExpectedConditions {
    public MoreExpectedConditions() {
    }

    public static ExpectedCondition<WebElement> anyElementToBeClickable(final WebElement... elements) {
        return MoreExpectedConditions.anyElementToBeClickable(Arrays.asList(elements));
    }

    public static ExpectedCondition<WebElement> anyElementToBeClickable(final List<WebElement> elements) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                for (WebElement element : elements) {
                    try {
                        WebElement visibleElement = ExpectedConditions.visibilityOf(element).apply(driver);
                        if (visibleElement != null && visibleElement.isEnabled()) {
                            return visibleElement;
                        }
                    } catch (StaleElementReferenceException doNotCheck) {
                        // continue
                    }
                }
                return null;
            }

            public String toString() {
                return "any element to be clickable: " + elements;
            }
        };
    }
}
