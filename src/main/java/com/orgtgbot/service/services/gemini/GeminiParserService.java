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
            "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";

    public GeminiParserService(GeminiProperties geminiProperties, RestClient.Builder restClient, ObjectMapper objectMapper) {
        this.geminiProperties = geminiProperties;
        this.restClient = restClient.build();
        this.objectMapper = objectMapper;
    }

    public ReminderDto parseReminder(String rawText) {
        String apiKey = geminiProperties.apiKey();
        String currentTime = LocalDateTime.now().toString();

        String combinedPrompt = "Извлеки суть напоминания и точное время. " +
                "Текущее время: " + currentTime + ". " +
                "Ответь только JSON-строкой по схеме: " +
                "{\"text\":\"суть\",\"targetTime\":\"YYYY-MM-DDTHH:mm:ss\"}. " +
                "Текст пользователя: " + rawText;

        ObjectNode requestBody = objectMapper.createObjectNode();
        ObjectNode textNode = objectMapper.createObjectNode().put("text", combinedPrompt);
        ArrayNode partsArray = objectMapper.createArrayNode().add(textNode);
        ObjectNode contentNode = objectMapper.createObjectNode().set("parts", partsArray);
        requestBody.set("contents", objectMapper.createArrayNode().add(contentNode));

        ObjectNode configNode = objectMapper.createObjectNode().put("responseMimeType", "application/json");
        requestBody.set("generationConfig", configNode);

        try {
            log.info("[GEMINI] Запрос к ИИ: '{}'", rawText);
            String requestJsonString = objectMapper.writeValueAsString(requestBody);

            // Шлем запрос на стабильную v1
            String responseJson = restClient.post()
                    .uri(GEMINI_API_URL + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestJsonString)
                    .retrieve()
                    .body(String.class);

            log.info("[GEMINI] Ответ от Google: {}", responseJson);

            var rootNode = objectMapper.readTree(responseJson);
            String aiJsonText = rootNode.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText().trim();

            String cleanJson = aiJsonText.replaceAll("```json", "").replaceAll("```", "").trim();
            log.info("[GEMINI] Чистый JSON для Jackson: {}", cleanJson);

            return objectMapper.readValue(cleanJson, ReminderDto.class);

        } catch (Exception e) {
            log.error("[GEMINI-ERROR] Падение в аварийный режим", e);
            return new ReminderDto(rawText, LocalDateTime.now().plusMinutes(10));
        }
    }
}
