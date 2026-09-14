package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import com.bank.qa.ci.AiBugReporter;
import com.bank.qa.pages.LoginPage;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URI;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginTest extends BaseTest {

    // Переменная для нашего агента
    private AiBugReporter.BugAnalyzerAgent aiAgent;

    @BeforeClass
    public void setUpAi() {
        // Инициализируем модель один раз перед запуском тестов в классе
        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey("ТВОЙ_КЛЮЧ_ОТ_GEMINI")
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();
        aiAgent = AiServices.create(AiBugReporter.BugAnalyzerAgent.class, model);
    }

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

        String inventoryUrl = URI.create(config.uiBaseUrl()).resolve("/.inventory.html").toString();
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
        return new Object[][]{
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

    @AfterMethod
    public void attachAiReportOnFailure(ITestResult result) {
        // Проверяем, что тест упал и есть лог ошибки
        if (result.getStatus() == ITestResult.FAILURE && result.getThrowable() != null) {
            System.out.println("--- 🤖 Запрашиваем AI анализ падения ---");

            String stacktrace = result.getThrowable().toString();

            try {
                // Получаем ответ от Gemini
                AiBugReporter.JiraTicket ticket = aiAgent.analyzeError(stacktrace);

                String aiReport = String.format(
                        "Summary: %s\nPriority: %s\n\nDescription:\n%s",
                        ticket.summary(), ticket.priority(), ticket.description()
                );

                // Прикрепляем в Allure (теперь контекст точно жив!)
                Allure.addAttachment("🧠 AI Анализ дефекта", aiReport);

            } catch (Exception e) {
                System.err.println("Не удалось получить ответ от AI: " + e.getMessage());
            }
        }
    }
}
