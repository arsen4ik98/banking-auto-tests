package com.bank.qa.ui;

import com.bank.qa.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FirstUITest extends BaseTest{

    @Test(description = "Проверка доступности главной страницы")
    public void checkMainPageTitle() {
        page.navigate(config.uiBaseUrl());
        String title = page.title();
        Assert.assertEquals(title, "Swag Labs", "Заголовок страницы не совпадает");
    }
}
