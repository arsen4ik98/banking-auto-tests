package com.bank.qa.base;

import com.bank.qa.utils.ProjectConfig;
import com.bank.qa.utils.TestListener;
import com.microsoft.playwright.*;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.*;

@Listeners(TestListener.class)
public class BaseTest {
    // Конфиг можно инициализировать сразу, так как он не зависит от браузера
    protected static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    // ТОЛЬКО ОБЪЯВЛЕНИЕ ПЕРЕМЕННЫХ
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
        // Создаем изолированный контекст и страницу перед КАЖДЫМ тестом
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

    public Page getPage() {
        return page;
    }
}