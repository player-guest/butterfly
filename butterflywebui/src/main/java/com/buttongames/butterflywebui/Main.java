package com.buttongames.butterflywebui;

import com.buttongames.butterflywebui.http.WebUiHttpServer;
import com.buttongames.butterflywebui.spring.configuration.ApplicationConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    private static AnnotationConfigApplicationContext applicationContext;
    public static void main(String[] args) {

        applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        final WebUiHttpServer webUiHttpServer = applicationContext.getBean(WebUiHttpServer.class);

        webUiHttpServer.startServer();
    }
}
