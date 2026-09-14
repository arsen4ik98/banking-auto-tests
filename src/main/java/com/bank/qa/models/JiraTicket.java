package com.bank.qa.models;

public record JiraTicket(
        String summary,       // Короткий заголовок бага
        String description,   // Подробное описание и шаги воспроизведения
        String priority       // "High", "Medium" или "Low"
) {}