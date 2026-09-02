package com.bank.qa.utils;

import org.aeonbits.owner.Config;

@Config.Sources({"classspath:application.properties"})
public interface ProjectConfig extends Config {
    @Key("ui.base.url")
    String uiBaseUrl();

    @Key("api.base.url")
    String apiBaseUrl();

    @Key("ui.test.username")
    String uiTestUsername();

    @Key("ui.test.password")
    String uiTestPassword();

    @Key("api.test.username")
    String apiTestUsername();

    @Key("api.test.password")
    String apiTestPassword();
}
