package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import com.bank.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URI;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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

        String inventoryUrl = URI.create(config.uiBaseUrl()).resolve("/inventory.html").toString();
        assertThat(page).hasURL(inventoryUrl);
    }

    @Test(description = "Проверка появления ошибки при неверном пароле")
    public void testInvalidLogin() {
        page.navigate(config.uiBaseUrl());
        loginPage.login(config.uiTestUsername(), "wrong_password");

        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Текст ошибки не совпадает");
    }

    // 1. Создаем матрицу данных: Логин, Пароль, Ожидаемый текст ошибки
    @DataProvider(name = "negativeLoginData")
    public Object[][] negativeLoginData() {
        return new Object[][] {
                {"locked_out_user", true, "Epic sadface: Sorry, this user has been locked out."},
                {config.uiTestUsername(), false, "Epic sadface: Username and password do not match"},
                {"", true, "Epic sadface: Username is required"}
        };
    }

    // 2. Связываем тест с провайдером и передаем переменные в аргументы метода
    @Test(description = "Проверка негативных сценариев авторизации", dataProvider = "negativeLoginData")
    public void testNegativeLoginScenarios(String username, boolean useValidPassword, String expectedError) {
        page.navigate(config.uiBaseUrl());
        // Не передаем пароль как параметр теста: TestNG и Allure сохраняют параметры в отчетах.
        loginPage.login(username, useValidPassword ? config.uiTestPassword() : "wrong_pass");

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
                String.format("Ожидалась ошибка '%s', но получили '%s'", expectedError, actualError));
    }
}
