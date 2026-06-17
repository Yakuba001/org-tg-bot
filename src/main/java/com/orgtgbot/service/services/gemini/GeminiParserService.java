package com.orgtgbot.service.services.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

        // Инструкция для ИИ
        String systemInstruction = "Ты — строгий парсер напоминаний. Твоя задача — извлечь суть напоминания и определить точное время его срабатывания.\n" +
                "Текущее время сервера: " + currentTime + ".\n" +
                "Ответь СТРОГО в формате JSON, используя схему:\n" +
                "{\n" +
                "  \"text\": \"очищенный текст напоминания (например, 'Купить молоко', а не 'Прив, через 10 минут напомни купить молоко')\",\n" +
                "  \"targetTime\": \"дата и время в формате YYYY-MM-DDTHH:mm:ss\"\n" +
                "}";

        // Собираем валидное JSON-дерево для Google API
        ObjectNode requestBody = objectMapper.createObjectNode();

        // 1. Текст пользователя в contents
        ObjectNode textNode = objectMapper.createObjectNode().put("text", rawText);
        ArrayNode partsArray = objectMapper.createArrayNode().add(textNode);
        ObjectNode contentNode = objectMapper.createObjectNode().set("parts", partsArray);
        requestBody.set("contents", objectMapper.createArrayNode().add(contentNode));

        // 2. Системный промпт в systemInstruction
        ObjectNode systemTextNode = objectMapper.createObjectNode().put("text", systemInstruction);
        ArrayNode systemPartsArray = objectMapper.createArrayNode().add(systemTextNode);
        ObjectNode systemInstructionNode = objectMapper.createObjectNode().set("parts", systemPartsArray);
        requestBody.set("systemInstruction", systemInstructionNode);

        // 3. Конфигурация для строгого возврата JSON
        ObjectNode configNode = objectMapper.createObjectNode().put("responseMimeType", "application/json");
        requestBody.set("generationConfig", configNode);

        try {
            log.info("[GEMINI] Отправка запроса к ИИ для парсинга: '{}'", rawText);

            String requestJsonString = objectMapper.writeValueAsString(requestBody);

            String responseJson = restClient.post()
                    .uri(GEMINI_API_URL + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestJsonString)
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

            // Чистим от возможных кавычек разметки markdown, если модель их добавит
            String cleanJson = aiJsonText.replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            log.info("[GEMINI] Извлеченный чистый JSON для Jackson: {}", cleanJson);

            return objectMapper.readValue(cleanJson, ReminderDto.class);

        } catch (Exception e) {
            log.error("[GEMINI-ERROR] Ошибка при парсинге ИИ, включается аварийный режим", e);
            // Если ИИ упадет, упадем в дефолт (как на твоем скриншоте)
            return new ReminderDto(rawText, LocalDateTime.now().plusMinutes(10));
        }
    }
}
