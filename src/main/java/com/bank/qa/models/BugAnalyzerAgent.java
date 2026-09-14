package com.bank.qa.models;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface BugAnalyzerAgent {

    @SystemMessage({
            "Ты Senior QA Automation Engineer в финтех-компании.",
            "Твоя задача — анализировать стектрейсы упавших автотестов и формировать баг-репорты.",
            "Правила оценки приоритета:",
            " - Если это 500 ошибка (Internal Server Error) при переводе средств -> High",
            " - Если это 400 ошибка валидации или UI таймаут -> Medium",
            " - Если тест упал из-за недоступности тестового стенда (Connection refused) -> Low",
            "Возвращай только данные для тикета, без лишних рассуждений."
    })
    @UserMessage("Проанализируй этот стектрейс и сформируй тикет:\n {{it}}")
    JiraTicket analyzeError(String stacktrace);
}
