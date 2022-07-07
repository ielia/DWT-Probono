package com.qualitestgroup.dwt.probono.selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleSearchResultsPage extends Page<GoogleSearchResultsPage> {
    protected static final String URL_PREFIX = "https://www.google.com/search?q=";
    protected static final Pattern RESULTS_PATTERN = Pattern.compile(".* ([\\d,]+) .* \\(.*");

    protected String term;

    @FindBy(id = "result-stats")
    protected WebElement numberOfResults;

    public GoogleSearchResultsPage(WebDriver driver, String term) {
        super(driver);
        this.term = term;
    }

    public GoogleSearchResultsPage go() {
        try {
            this.driver.get(URL_PREFIX + URLEncoder.encode(this.term, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public BigInteger getNumberOfResults() {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(this.numberOfResults));
        String nResStr = this.numberOfResults.getText();
        Matcher matcher = RESULTS_PATTERN.matcher(nResStr);
        if (matcher.find()) {
            return new BigInteger(matcher.group(1).replaceAll(",", ""));
        } else {
            throw new RuntimeException("These are not the number of results you are looking for.");
        }
    }
}
