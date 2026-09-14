package com.bank.qa.ci;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class AiBugReporter {

    // Record для DTO (Java 21)
    public record JiraTicket(String summary, String description, String priority) {}

    // Интерфейс агента
    public interface BugAnalyzerAgent {
        @SystemMessage({
                "Ты Senior QA Automation Engineer.",
                "Анализируй стектрейсы и формируй баг-репорты. Возвращай только данные для тикета.",
                "Если ошибка 500 -> приоритет High, если 400 -> Medium, иные -> Low."
        })
        @UserMessage("Проанализируй этот стектрейс и сформируй тикет:\n {{it}}")
        JiraTicket analyzeError(String stacktrace);
    }

    public static void main(String[] args) {
        // Подключение к OpenAI

        // Укажите тип прокси (HTTP или SOCKS) и порт вашего локального клиента.
// Обычно для локальных клиентов (Shadowsocks, v2ray, Outline) это 1080, 10808 или 8080.
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "10808");

        GoogleAiGeminiChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey("AIzaSyCvYmswd_Y4mNL2KO-ptrASjZeaEKYCo84")
                .modelName("gemini-2.5-flash")
                .temperature(0.0)
                .build();

        BugAnalyzerAgent agent = AiServices.create(BugAnalyzerAgent.class, model);

        String failedTestLog = """
            java.lang.AssertionError: 1 expectation failed.
            Expected status code <200> but was <500>.
            at io.restassured.internal.ValidatableResponseImpl.statusCode(ValidatableResponseImpl.groovy:142)
            """;

        JiraTicket ticket = agent.analyzeError(failedTestLog);

        System.out.println("Summary: " + ticket.summary());
        System.out.println("Priority: " + ticket.priority());
        System.out.println("Description:\n" + ticket.description());
    }
}