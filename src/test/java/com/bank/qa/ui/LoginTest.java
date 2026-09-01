package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import com.bank.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Успешная авторизация валидным пользователем")
    public void testValidLogin() {
        page.navigate("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(page);

        // Используем стандартные учетные данные демо-сайта
        loginPage.login("standard_user", "secret_sauce");

        // Проверяем, что URL изменился на страницу каталога
        Assert.assertEquals(page.url(), "https://www.saucedemo.com/inventory.html", "Авторизация не удалась");
    }

    @Test(description = "Проверка появления ошибки при неверном пароле")
    public void testInvalidLogin() {
        page.navigate("https://www.saucedemo.com/");
        LoginPage loginPage = new LoginPage(page);

        loginPage.login("standard_user", "wrong_password");

        // Проверяем, что система не пустила пользователя и выдала правильный текст ошибки
        String error = loginPage.getErrorMessage();
        Assert.assertTrue(error.contains("Username and password do not match"), "Текст ошибки не совпадает");
    }
}