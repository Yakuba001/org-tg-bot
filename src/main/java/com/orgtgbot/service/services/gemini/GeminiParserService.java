package com.orgtgbot.service.services.gemini;

import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.reminder.ReminderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiParserService {

    private final GeminiProperties geminiProperties;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public ReminderDto parseReminder(String rawText) {
        String apiKey = geminiProperties.apiKey();
        log.info("[GEMINI] Ключ успешно взят из проперти: {}***", apiKey);

        String testJsonBody = "{\"contents\": [{\"parts\":[{\"text\": \"Привет\"}]}]}";

        try {
            log.info("[GEMINI-TEST] Пробуем связаться с Google. Ключ: {}***", apiKey.substring(0, Math.min(apiKey.length(), 5)));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey) // Твой AQ ключ передается в заголовке
                    .POST(HttpRequest.BodyPublishers.ofString(testJsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();
            log.info("[GEMINI-TEST] HTTP Status Code: {}", response.statusCode());
            log.info("[GEMINI-TEST] Raw Response Body: {}", response.body());

        } catch (Exception e) {
            log.error("[GEMINI-TEST-ERROR] Ошибка при отправке тестового запроса", e);
        }

        return new ReminderDto(rawText, LocalDateTime.now());
    }
}
