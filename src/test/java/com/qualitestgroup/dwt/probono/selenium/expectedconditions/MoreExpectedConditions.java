package com.qualitestgroup.dwt.probono.selenium.expectedconditions;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;

/**
 * Some more WebDriver expected conditions not found in {@link org.openqa.selenium.support.ui.ExpectedConditions}.
 */
public class MoreExpectedConditions {
    /**
     * @see #anyElementToBeClickable(List)
     * @param elements Web elements to check.
     * @return The first web element found to be clickable.
     */
    public static ExpectedCondition<WebElement> anyElementToBeClickable(final WebElement... elements) {
        return MoreExpectedConditions.anyElementToBeClickable(Arrays.asList(elements));
    }

    /**
     * @param elements The list of elements to check.
     * @return The first element from the list found to be clickable.
     */
    public static ExpectedCondition<WebElement> anyElementToBeClickable(final List<WebElement> elements) {
        return new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver driver) {
                for (WebElement element : elements) {
                    try {
                        WebElement visibleElement = ExpectedConditions.visibilityOf(element).apply(driver);
                        if (visibleElement != null && visibleElement.isEnabled()) {
                            return visibleElement;
                        }
                    } catch (StaleElementReferenceException ignored) {
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
