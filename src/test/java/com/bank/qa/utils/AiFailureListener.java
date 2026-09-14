package com.bank.qa.utils;

import com.bank.qa.ci.AiBugReporter;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.Arrays;

public class AiFailureListener implements ITestListener {

    private final AiBugReporter.BugAnalyzerAgent agent;

    public AiFailureListener() {

        String apiKey = System.getenv("AIzaSyCvYmswd_Y4mNL2KO-ptrASjZeaEKYCo84");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API ключ не найден! Установите переменную окружения GEMINI_API_KEY");
        }
        // Если Gemini работает через прокси, раскомментируй эти 2 строки:
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10808");

        // Инициализируем модель один раз при старте тестов
        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();

        this.agent = AiServices.create(AiBugReporter.BugAnalyzerAgent.class, model);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable error = result.getThrowable();
        if (error != null) {
            System.out.println("--- 🤖 AI анализирует падение теста: " + result.getName() + " ---");

            // 1. Формируем текст ошибки
            String stacktrace = error.toString() + "\n" + Arrays.toString(error.getStackTrace());

            try {
                // 2. Отправляем в Gemini (занимает пару секунд)
                AiBugReporter.JiraTicket ticket = agent.analyzeError(stacktrace);

                // 3. Красиво форматируем ответ
                String aiReport = String.format(
                        "Summary: %s\nPriority: %s\n\nDescription:\n%s",
                        ticket.summary(), ticket.priority(), ticket.description()
                );

                // 4. Выводим в консоль
                System.out.println(aiReport);

                // 5. Прикрепляем прямо в отчет Allure!
                Allure.addAttachment("🧠 AI Анализ дефекта", aiReport);

            } catch (Exception e) {
                System.err.println("Не удалось получить ответ от AI: " + e.getMessage());
            }
        }
    }
}