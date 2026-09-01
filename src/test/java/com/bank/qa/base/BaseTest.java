package com.bank.qa.base;

import com.microsoft.playwright.*;
import org.testng.annotations.*;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUpClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeMethod
    public void setUpMethod() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterMethod
    public void tearDownMethod() {
        context.close();
    }

    @AfterClass
    public void tearDownClass() {
        browser.close();
        playwright.close();
    }
}
