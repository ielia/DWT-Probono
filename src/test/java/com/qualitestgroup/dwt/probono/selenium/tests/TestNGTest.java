package com.qualitestgroup.dwt.probono.selenium.tests;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestNGTest implements ITest {
    public static final String BROWSER_NAME_PROPERTY = "browser";

    protected ThreadLocal<Map<String, Page<?>>> currentPages = new ThreadLocal<>();
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    protected ThreadLocal<String> testName = new ThreadLocal<>();
    protected WebDriverManager webDriverManager;

    public TestNGTest() {
        currentPages.set(new HashMap<>(1));
        webDriverManager = WebDriverManager.getInstance(System.getProperty(BROWSER_NAME_PROPERTY));
        webDriverManager.setup();
    }

    public WebDriver getDriver() {
        return this.driver.get();
    }

    @SuppressWarnings("unchecked")
    public <P extends Page<?>> P getPage() {
        return (P) this.getPage(null);
    }

    @SuppressWarnings("unchecked")
    public <P extends Page<?>> P getPage(String name) {
        return (P) this.currentPages.get().get(name);
    }

    public void setPage(Page<?> page) {
        this.setPage(null, page);
    }

    public void setPage(String name, Page<?> page) {
        this.currentPages.get().put(name, page);
    }

    @BeforeMethod
    protected void beforeEach(Method method, ITestContext context, Object[] data) {
        String template = method.getAnnotation(Test.class).testName();
        this.testName.set(String.format(StringUtils.isBlank(template) ? method.getName() : template, data));
        WebDriver configuredDriver = webDriverManager.create();
        this.driver.set(configuredDriver);
    }

    @AfterMethod
    protected void afterEach() {
        this.getDriver().quit();
    }

    @Override
    public String getTestName() {
        return this.testName.get();
    }
}
