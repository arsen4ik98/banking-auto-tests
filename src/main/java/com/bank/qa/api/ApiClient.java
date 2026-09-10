package com.bank.qa.api;

import com.bank.qa.utils.ProjectConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

public class ApiClient {
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                // Тела запросов и ответов авторизации содержат пароль и токен.
                // Не записываем их в консоль или вложения Allure.
                .build();
    }
}
