package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import com.bank.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        // Инициализируем страницу. К этому моменту BaseTest уже создал 'page'
        loginPage = new LoginPage(page);
    }

    @Test(description = "Успешная авторизация валидным пользователем")
    public void testValidLogin() {
        page.navigate(config.uiBaseUrl());
        loginPage.login(config.uiTestUsername(), config.uiTestPassword());

        Assert.assertEquals(page.url(), config.uiBaseUrl() + "inventory.html", "Авторизация не удалась");
    }

    @Test(description = "Проверка появления ошибки при неверном пароле")
    public void testInvalidLogin() {
        page.navigate(config.uiBaseUrl());
        loginPage.login(config.uiTestUsername(), "wrong_password");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Текст ошибки не совпадает");
    }
}