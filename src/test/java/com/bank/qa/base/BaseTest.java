package com.bank.qa.base;

import com.bank.qa.utils.ProjectConfig;
import com.microsoft.playwright.*;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.*;

public class BaseTest {

    protected static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeClass
    public void setUpClass() {


        playwright = Playwright.create();
        boolean isHeadless = System.getenv("CI") != null;
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
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
