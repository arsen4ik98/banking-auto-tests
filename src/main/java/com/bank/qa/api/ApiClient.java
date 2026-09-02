package com.bank.qa.api;

import com.bank.qa.utils.ProjectConfig;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

public class ApiClient {
    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(config.apiBaseUrl())
                .setContentType(ContentType.JSON)
                // Автоматически логируем все API-запросы в консоль и прикрепляем в отчет Allure
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new AllureRestAssured())
                .build();
    }
}