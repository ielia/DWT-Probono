package com.qualitestgroup.dwt.probono.selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class Page<P extends Page<P>> {
    protected WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
        this.init();
    }

    public abstract P go();

    @SuppressWarnings("unchecked")
    protected P init() {
        PageFactory.initElements(this.driver, (P) this);
        return (P) this;
    }
}
