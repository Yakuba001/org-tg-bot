package com.orgtgbot.service.services.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.reminder.ReminderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiParserService {

    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public ReminderDto parseReminder(String rawText, LocalDateTime userTime) {
        String apiKey = geminiProperties.apiKey();
        String systemInstruction = getSystemInstruction(userTime);

        String jsonBody = "{"
                + "\"systemInstruction\": {\"parts\": [{\"text\": \"" + systemInstruction.replace("\"", "\\\"") + "\"}]},"
                + "\"contents\": [{\"parts\": [{\"text\": \"" + rawText.replace("\"", "\\\"") + "\"}]}]"
                + ",\"generationConfig\": {\"responseMimeType\": \"application/json\"}"
                + "}";

        return sendHttpRequestToGemini(jsonBody, apiKey, rawText);
    }

    public ReminderDto parseVoiceReminder(byte[] audioBytes, LocalDateTime userTime) {
        String apiKey = geminiProperties.apiKey();
        String systemInstruction = getSystemInstruction(userTime);
        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);

        try {
            ObjectNode rootNode = objectMapper.createObjectNode();

            rootNode.putObject("systemInstruction")
                    .putArray("parts")
                    .addObject()
                    .put("text", systemInstruction);

            ArrayNode partsArray = rootNode.putObject("contents")
                    .putArray("parts");

            partsArray.addObject().put("text", "Распарси это голосовое сообщение и верни строго JSON.");

            partsArray.addObject()
                    .putObject("inlineData")
                    .put("mimeType", "audio/ogg")
                    .put("data", base64Audio);

            rootNode.putObject("generationConfig")
                    .put("responseMimeType", "application/json");

            String jsonBody = objectMapper.writeValueAsString(rootNode);

            return sendHttpRequestToGemini(jsonBody, apiKey, "Голосовое сообщение");

        } catch (Exception e) {
            log.error("[GEMINI-ERROR] Ошибка при сборке JSON для голосового сообщения", e);
            return new ReminderDto("Голосовое сообщение", LocalDateTime.now());
        }
    }

    private String getSystemInstruction(LocalDateTime userTime) {
        return "Ты — системный backend-модуль. Твоя единственная задача — прослушать голосовое сообщение или прочитать текст и распарсить напоминание от пользователя. " +
                "Ты должен вернуть JSON-объект с двумя полями: " +
                "1. 'text' (строка, суть того, о чем напомнить, без лишних слов вежливости). " +
                "2. 'targetTime' (строка в формате ISO-8601 'YYYY-MM-DDTHH:mm:ss', рассчитанная на основе текущего времени). " +
                "Текущее время сервера: " + userTime.toString() + ". " +
                "Если пользователь не указал конкретное время, выстави targetTime на 1 час вперед от текущего.";
    }

    private ReminderDto sendHttpRequestToGemini(String jsonBody, String apiKey, String defaultText) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());

                String aiGeneratedText = rootNode
                        .path("candidates").get(0)
                        .path("content")
                        .path("parts").get(0)
                        .path("text").asText();

                String cleanJson = aiGeneratedText.replaceAll("```json", "").replaceAll("```", "").trim();
                return objectMapper.readValue(cleanJson, ReminderDto.class);
            } else {
                log.error("[GEMINI-ERROR] Ошибка API. Статус код: {}, Ответ: {}", response.statusCode(), response.body());
            }

        } catch (Exception e) {
            log.error("[GEMINI-ERROR] Ошибка отправки запроса к ИИ", e);
        }
        return new ReminderDto(defaultText, LocalDateTime.now());
    }
}
