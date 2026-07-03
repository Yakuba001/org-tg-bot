package com.orgtgbot.service.services.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.exception.exceptions.service.gemini.DeserializeGeminiResponse;
import com.orgtgbot.exception.exceptions.service.gemini.GeminiParseTextException;
import com.orgtgbot.exception.exceptions.service.gemini.GeminiParseVoiceException;
import com.orgtgbot.exception.exceptions.service.http.SendHttpRequestToGeminiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GeminiParserService {

    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient geminiHttpClient;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public ReminderDto parseReminder(String rawText, LocalDateTime userTime) {
        try {
            ObjectNode rootNode = createBaseGeminiRequest(userTime);
            ArrayNode partsArray = (ArrayNode) rootNode.path("contents").path("parts");
            partsArray.addObject().put("text", rawText);
            String jsonBody = objectMapper.writeValueAsString(rootNode);
            return sendHttpRequestToGemini(jsonBody);
        } catch (JsonProcessingException e) {
            throw new GeminiParseTextException("Failed to build JSON request for GEMINI API from text", e);
        }
    }

    public ReminderDto parseVoiceReminder(byte[] audioBytes, LocalDateTime userTime) {
        try {
            ObjectNode rootNode = createBaseGeminiRequest(userTime);
            ArrayNode partsArray = (ArrayNode) rootNode.path("contents").path("parts");

            partsArray.addObject().put("text", "Распарси это голосовое сообщение и верни строго JSON.");

            String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
            partsArray.addObject()
                    .putObject("inlineData")
                    .put("mimeType", "audio/ogg")
                    .put("data", base64Audio);

            String jsonBody = objectMapper.writeValueAsString(rootNode);
            return sendHttpRequestToGemini(jsonBody);
        } catch (JsonProcessingException e) {
            throw new GeminiParseVoiceException("Failed to build JSON request for GEMINI API from voice", e);
        }
    }

    private ObjectNode createBaseGeminiRequest(LocalDateTime userTime) {
        ObjectNode rootNode = objectMapper.createObjectNode();

        rootNode.putObject("systemInstruction")
                .putArray("parts")
                .addObject()
                .put("text", buildSystemInstruction(userTime));

        rootNode.putObject("contents")
                .putArray("parts");

        rootNode.putObject("generationConfig")
                .put("responseMimeType", "application/json");

        return rootNode;
    }

    private String buildSystemInstruction(LocalDateTime userTime) {
        return "Ты — системный backend-модуль. Твоя единственная задача — прослушать голосовое сообщение или прочитать текст и распарсить напоминание от пользователя. " +
                "Ты должен вернуть JSON-объект с двумя полями: " +
                "1. 'text' (строка, суть того, о чем напомнить, без лишних слов вежливости). " +
                "2. 'targetTime' (строка в формате ISO-8601 'YYYY-MM-DDTHH:mm:ss', рассчитанная на основе текущего времени). " +
                "Текущее время сервера: " + userTime.toString() + ". " +
                "Если пользователь не указал конкретное время, выстави targetTime на 1 час вперед от текущего.";
    }

    private ReminderDto sendHttpRequestToGemini(String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_API_URL))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", geminiProperties.apiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = geminiHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
                return deserializeGeminiResponse(response.body());

            throw new SendHttpRequestToGeminiException("Gemini returned non-200 status code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new SendHttpRequestToGeminiException("Failed try to call gemini.", e);
        }
    }

    private ReminderDto deserializeGeminiResponse(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            String aiGeneratedText = rootNode
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();
            String cleanJson = aiGeneratedText.replaceAll("```json", "")
                    .replaceAll("```", "").trim();
            return objectMapper.readValue(cleanJson, ReminderDto.class);
        } catch (JsonProcessingException e) {
            throw new DeserializeGeminiResponse("Failed to deserialize GEMINI response", e);
        }
    }
}
