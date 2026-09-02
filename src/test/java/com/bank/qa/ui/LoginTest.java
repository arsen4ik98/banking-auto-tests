package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import com.bank.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
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

        Assert.assertEquals(page.url(), config.uiBaseUrl() + "///inventory.html", "Авторизация не удалась");
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
                {"locked_out_user", config.uiTestPassword(), "Epic sadface: Sorry, this user has been locked out."},
                {config.uiTestUsername(), "wrong_pass", "Epic sadface: Username and password do not match"},
                {"", config.uiTestPassword(), "Epic sadface: Username is required"}
        };
    }

    // 2. Связываем тест с провайдером и передаем переменные в аргументы метода
    @Test(description = "Проверка негативных сценариев авторизации", dataProvider = "negativeLoginData")
    public void testNegativeLoginScenarios(String username, String password, String expectedError) {
        page.navigate(config.uiBaseUrl());
        loginPage.login(username, password);

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
                String.format("Ожидалась ошибка '%s', но получили '%s'", expectedError, actualError));
    }
}