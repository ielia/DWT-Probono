package com.qualitestgroup.dwt.probono.selenium.pageobjects.googlesearch;

import com.qualitestgroup.dwt.probono.selenium.exceptions.InvalidPageException;
import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import org.apache.commons.lang3.LocaleUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleSearchResultsPage extends Page<GoogleSearchResultsPage> {
    protected static final int RESULTS_GROUP_INDEX = 1;
    protected static final By HTML_LOCATOR = new By.ByTagName("html");
    protected static final Pattern RESULTS_PATTERN = Pattern.compile(".* ([\\d,]+) .* \\(.*");
    protected static final String HTML_LOCALE_ATTRIBUTE = "lang";
    protected static final String URL_WO_QUERY = "https://www.google.com/search";
    protected static final String URL_PREFIX = URL_WO_QUERY + "?q=";

    protected String term;

    @FindBy(id = "result-stats")
    protected WebElement numberOfResults;

    public GoogleSearchResultsPage(WebDriver driver, String term) {
        super(driver);
        term = term;
    }

    protected String getEncodedTerm() {
        try {
            return URLEncoder.encode(term, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getEffectiveURL() {
        return URL_PREFIX + getEncodedTerm();
    }

    @Override
    public boolean assertCurrentPage() {
        String currentURLStr = driver.getCurrentUrl();
        try {
            URL currentURL = new URL(currentURLStr);
            if (!currentURLStr.startsWith(URL_WO_QUERY + "?")
                    || !currentURL.getQuery().matches("(^|[&;])q=" + getEncodedTerm() + "([&;]|$)")) {
                throw new InvalidPageException("Should be at \"" + getEffectiveURL() +
                        "\", but it is at " + driver.getCurrentUrl());
            }
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
        return true;
    }

    @Override
    public GoogleSearchResultsPage go() {
        driver.get(getEffectiveURL());
        return this;
    }

    public BigInteger getNumberOfResults() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(numberOfResults));
        String nResStr = numberOfResults.getText();
        Matcher matcher = RESULTS_PATTERN.matcher(nResStr);
        if (matcher.find()) {
            return parseInteger(matcher.group(RESULTS_GROUP_INDEX));
        } else {
            throw new RuntimeException("These are not the number of results you are looking for.");
        }
    }

    protected NumberFormat getNumberFormat() {
        WebDriverWait htmlWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        WebElement html = htmlWait.until(ExpectedConditions.presenceOfElementLocated(HTML_LOCATOR));
        Locale pageLocale = LocaleUtils.toLocale(html.getAttribute(HTML_LOCALE_ATTRIBUTE).replace("-", "_"));
        DecimalFormat numberFormat = (DecimalFormat) DecimalFormat.getIntegerInstance(pageLocale);
        numberFormat.setParseBigDecimal(true);
        return numberFormat;
    }

    @SuppressWarnings("CommentedOutCode")
    protected BigInteger parseInteger(String numberStr) {
        // try {
        //     BigDecimal numberOfResults = (BigDecimal) getNumberFormat().parse(numberStr);
        //     return numberOfResults.toBigInteger();
        // } catch (ParseException parseEx) {
        //     throw new RuntimeException("There was a problem parsing the number", parseEx);
        // }
        return new BigInteger(numberStr.replaceAll("[,.]", "")); // Google has a bug in their locale treatment.
    }
}
