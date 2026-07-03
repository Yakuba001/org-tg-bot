package com.orgtgbot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration;

@SpringBootTest
@EnableAutoConfiguration(exclude = {TelegramBotStarterConfiguration.class})
class OrgTgBotApplicationTests {

    @Test
    void contextLoads() {
    }

}
