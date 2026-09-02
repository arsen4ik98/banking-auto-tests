package com.bank.qa.api;

import com.bank.qa.models.AuthRequest;
import com.bank.qa.utils.ProjectConfig;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.aeonbits.owner.ConfigFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiAuthTest {

    @Test(description = "Получение токена авторизации API")
    public void testSuccessfulAuthentication() {
        ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

        // Устанавливаем базовый URL
        RestAssured.baseURI = config.apiBaseUrl();

        // Создаем тело запроса с помощью Lombok Builder

        AuthRequest requestBody = AuthRequest.builder()
                .username(config.apiTestUsername())
                .password(config.apiTestPassword())
                .build();

        String  password = requestBody.getPassword();

        // Отправляем POST-запрос и сохраняем ответ
        Response response = given()
                .spec(ApiClient.getBaseSpec())
                .body(requestBody) // Jackson автоматически превратит объект в JSON
                .when()
                .post("/auth")
                .then()
                .extract().response();

        // Проверки (Assertions)
        Assert.assertEquals(response.statusCode(), 200, "Ожидался статус 200 OK");

        // Извлекаем токен из JSON-ответа
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token, "Токен не должен быть пустым");

        System.out.println("Сгенерированный токен: " + token);
        System.out.println("Сгенерированный пароль: " + password);
    }
}