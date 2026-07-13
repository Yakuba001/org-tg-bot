package com.orgtgbot.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "gemini")
public record GeminiProperties(
        @NotBlank String apiKey,
        @NotBlank String apiUrl
) {
}
