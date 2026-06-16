package com.orgtgbot.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@EnableConfigurationProperties({TelegramBotProperties.class, GeminiProperties.class})
public class AppConfig {

    @Bean
    public TelegramClient telegramClient(TelegramBotProperties props) {
        return new OkHttpTelegramClient(props.token());
    }
}
