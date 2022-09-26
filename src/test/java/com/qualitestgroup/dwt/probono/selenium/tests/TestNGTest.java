package com.qualitestgroup.dwt.probono.selenium.tests;

import com.qualitestgroup.dwt.probono.selenium.pageobjects.Page;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * TestNG Selenium Test parent class.
 * Takes care of setting the correct test names, instantiating web drivers and configuring logging.
 */
public class TestNGTest implements ITest {
    /**
     * Name of the System Property specifying browser type.
     */
    public static final String BROWSER_NAME_PROPERTY = "browser";
    /**
     * Name of the System Property specifying Selenium hub URL.
     */
    public static final String SELENIUM_HUB_URL_PROPERTY = "hub.url";

    /**
     * Browser name.
     */
    protected String browserName = System.getProperty(BROWSER_NAME_PROPERTY);
    /**
     * Selenium Hub URL, can be null.
     */
    protected String seleniumHubURL = System.getProperty(SELENIUM_HUB_URL_PROPERTY);
    /**
     * Current page per test thread.
     */
    protected ThreadLocal<Map<String, Page<?>>> currentPages = new ThreadLocal<>();
    /**
     * Current driver per test thread.
     */
    protected ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    /**
     * Current test name per test thread.
     */
    protected ThreadLocal<String> testName = new ThreadLocal<>();
    /**
     * Boni García's Web Driver Manager to be used to instantiate Selenium Web Drivers.
     */
    protected WebDriverManager webDriverManager;

    /**
     * <ul>
     *   <li>Sets up Web Driver Manager.</li>
     *   <li>Sets silent inputs for all browser types.</li>
     * </ul>
     */
    public TestNGTest() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        // TODO: Find a better way to do this
        System.setProperty("webdriver.chrome.silentOutput", "true");
        // System.setProperty("webdriver.edge.silentOutput", "true");
        System.setProperty("webdriver.firefox.logfile", "/dev/null");
        System.setProperty("webdriver.ie.driver.silent", "true");
        System.setProperty("webdriver.opera.silentOutput", "true");

        currentPages.set(new HashMap<>(1));
        if (seleniumHubURL == null) {
            webDriverManager = WebDriverManager.getInstance(browserName);
            webDriverManager.setup();
        }
    }

    /**
     * @return Current thread's web driver.
     */
    public WebDriver getDriver() {
        return this.driver.get();
    }

    /**
     * @return Current active page.
     * @param <P> Page type.
     */
    @SuppressWarnings("unchecked")
    public <P extends Page<?>> P getPage() {
        return (P) this.getPage(null);
    }

    /**
     * @param name Page name (not necessarily matching its type).
     * @param <P> Page type.
     * @return Current page for name.
     */
    @SuppressWarnings("unchecked")
    public <P extends Page<?>> P getPage(String name) {
        return (P) this.currentPages.get().get(name);
    }

    /**
     * Set current active page.
     * @param page Page object.
     */
    public void setPage(Page<?> page) {
        this.setPage(null, page);
    }

    /**
     * Set current active page for name.
     * @param name Name of the page.
     * @param page Page object.
     */
    public void setPage(String name, Page<?> page) {
        this.currentPages.get().put(name, page);
    }

    /**
     * Before each test:
     * <ul>
     *   <li>Set test name as the template interpolated with data or, if no template was provided, leave it as is.</li>
     *   <li>Instantiate Selenium Web Driver.</li>
     * </ul>
     * @param method Test method.
     * @param context Test context.
     * @param data Test data.
     */
    @BeforeMethod
    protected void beforeEach(Method method, ITestContext context, Object[] data) {
        String template = method.getAnnotation(Test.class).testName();
        this.testName.set(String.format(StringUtils.isBlank(template) ? method.getName() : template, data));

        WebDriver configuredDriver;
        if (seleniumHubURL == null) {
            configuredDriver = webDriverManager.create();
        } else {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setBrowserName(browserName);
            capabilities.setPlatform(Platform.WINDOWS);
            configuredDriver = new RemoteWebDriver(capabilities);
        }
        this.driver.set(configuredDriver);
    }

    /**
     * After each test:
     * <ul>
     *   <li>Exit Web Driver (i.e., close all windows and kill the driver process).</li>
     * </ul>
     */
    @AfterMethod
    protected void afterEach() {
        this.getDriver().quit();
    }

    /**
     * @return Current thread's test name.
     */
    @Override
    public String getTestName() {
        return this.testName.get();
    }
}
