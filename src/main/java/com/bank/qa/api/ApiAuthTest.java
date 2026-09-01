package com.bank.qa.api;

import com.bank.qa.models.AuthRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiAuthTest {

    @Test(description = "Получение токена авторизации API")
    public void testSuccessfulAuthentication() {
        // Устанавливаем базовый URL
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Создаем тело запроса с помощью Lombok Builder

        AuthRequest requestBody = AuthRequest.builder()
                .username("admin")
                .password("password123")
                .build();

        String  password = requestBody.getPassword();

        // Отправляем POST-запрос и сохраняем ответ
        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody) // Jackson автоматически превратит объект в JSON
                .log().all() // Логируем запрос в консоль
                .when()
                .post("/auth")
                .then()
                .log().all() // Логируем ответ
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