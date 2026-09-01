package com.bank.qa.pages;

import com.microsoft.playwright.Page;

public class LoginPage {
    private final Page page;

    // Локаторы сохраняем как обычные строки (CSS-селекторы по атрибуту data-test)
    private final String usernameInput = "[data-test='username']";
    private final String passwordInput = "[data-test='password']";
    private final String loginButton = "[data-test='login-button']";
    private final String errorMessage = "[data-test='error']";

    public LoginPage(Page page) {
        this.page = page;
    }

    // Бизнес-шаг: авторизация
    public void login(String username, String password) {
        page.fill(usernameInput, username);
        page.fill(passwordInput, password);
        page.click(loginButton);
    }

    // Получение текста ошибки для проверок
    public String getErrorMessage() {
        return page.locator(errorMessage).textContent();
    }
}