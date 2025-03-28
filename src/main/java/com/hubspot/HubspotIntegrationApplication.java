package com.hubspot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class HubspotIntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(HubspotIntegrationApplication.class, args);
    }
}
