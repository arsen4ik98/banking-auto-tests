package com.bank.qa.utils;

import com.bank.qa.base.BaseTest;
import com.microsoft.playwright.Page;
import io.qameta.allure.Attachment;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        // Получаем класс, в котором упал тест
        Object currentClass = result.getInstance();

        // Проверяем, что это UI-тест (наследуется от BaseTest)
        if (currentClass instanceof BaseTest) {
            Page page = ((BaseTest) currentClass).getPage();
            if (page != null) {
                attachScreenshotToAllure(page);
            }
        }
    }

    // Аннотация Allure автоматически прикрепит возвращаемый массив байтов как картинку
    @Attachment(value = "Скриншот экрана при падении", type = "image/png")
    public byte[] attachScreenshotToAllure(Page page) {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }
}