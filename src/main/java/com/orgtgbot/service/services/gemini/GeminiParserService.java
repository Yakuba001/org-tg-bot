package com.orgtgbot.service.services.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.reminder.ReminderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GeminiParserService {

    private final GeminiProperties geminiProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public GeminiParserService(GeminiProperties geminiProperties, RestClient.Builder restClient, ObjectMapper objectMapper) {
        this.geminiProperties = geminiProperties;
        this.restClient = restClient.build();
        this.objectMapper = objectMapper;
    }

    public ReminderDto parseReminder(String rawText) {
        String apiKey = geminiProperties.apiKey();
        String currentTime = LocalDateTime.now().toString();

        // Переносим инструкции прямо в текст запроса, чтобы не злить парсер Google сложными объектами
        String fullPrompt = "Ты — строгий парсер напоминаний. Твоя задача — извлечь суть напоминания и определить точное время его срабатывания.\n" +
                "Текущее время сервера: " + currentTime + ".\n" +
                "Ответь СТРОГО в формате JSON без какого-либо форматирования, markdown разметки или лишних символов (просто голый JSON-текст), используя схему:\n" +
                "{\n" +
                "  \"text\": \"очищенный текст напоминания (например, 'Купить яйца', а не 'Напомни мне купить яйца через 10 минут')\",\n" +
                "  \"targetTime\": \"дата и время в формате YYYY-MM-DDTHH:mm:ss\"\n" +
                "}\n\n" +
                "Текст пользователя для парсинга: \"" + rawText + "\"";

        // Стандартная структура для v1beta в camelCase, но БЕЗ systemInstruction
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", fullPrompt)
                        ))
                ),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json"
                )
        );

        try {
            log.info("[GEMINI] Отправка запроса к ИИ для парсинга: '{}'", rawText);

            String responseJson = restClient.post()
                    .uri(GEMINI_API_URL + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            log.info("[GEMINI] Чистый JSON от Google: {}", responseJson);

            var rootNode = objectMapper.readTree(responseJson);
            String aiJsonText = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // Очистка от возможных markdown-кавычек, если модель их проигнорирует
            String cleanJson = aiJsonText.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            log.info("[GEMINI] Извлеченный чистый JSON: {}", cleanJson);

            return objectMapper.readValue(cleanJson, ReminderDto.class);

        } catch (Exception e) {
            log.error("[GEMINI-ERROR] Ошибка при парсинге ИИ, включается аварийный режим", e);
            return new ReminderDto(rawText, LocalDateTime.now().plusHours(1));
        }
    }
}
