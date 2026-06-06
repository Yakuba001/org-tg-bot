package com.orgtgbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "telegram.bot")
public record TelegramBotProperties(
        @NotBlank String username,
        @NotBlank String token
) {
}
