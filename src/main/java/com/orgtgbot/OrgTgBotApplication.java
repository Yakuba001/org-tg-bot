package com.orgtgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OrgTgBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrgTgBotApplication.class, args);
    }

}
