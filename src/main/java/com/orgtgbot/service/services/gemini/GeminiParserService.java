package com.orgtgbot.service.services.gemini;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiParserService {

    private final GeminiProperties geminiProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient geminiHttpClient;

    public ReminderDto parseReminder(String rawText, LocalDateTime userTime) {
        try {
            ObjectNode rootNode = createBaseGeminiRequest(userTime);
            ArrayNode partsArray = (ArrayNode) rootNode.path("contents").path("parts");
            partsArray.addObject().put("text", rawText);
            String jsonBody = objectMapper.writeValueAsString(rootNode);
            String rawResponse = sendRawHttpRequestToGemini(jsonBody);

            return objectMapper.readValue(rawResponse, ReminderDto.class);
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
            String rawResponse = sendRawHttpRequestToGemini(jsonBody);

            return objectMapper.readValue(rawResponse, ReminderDto.class);
        } catch (JsonProcessingException e) {
            throw new GeminiParseVoiceException("Failed to build JSON request for GEMINI API from voice", e);
        }
    }

    public List<ReceiptItemDto> parseReceiptImage(byte[] imageBytes, String mimeType) {
        try {
            ObjectNode rootNode = createBaseGeminiRequest(null);
            ArrayNode partsArray = (ArrayNode) rootNode.path("contents").path("parts");
            partsArray.addObject().put("text", "Прочитай этот чек и извлеки товары с ценами.");

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            partsArray.addObject()
                    .putObject("inlineData")
                    .put("mimeType", mimeType)
                    .put("data", base64Image);

            String jsonBody = objectMapper.writeValueAsString(rootNode);

            String rawResponse = sendRawHttpRequestToGemini(jsonBody);

            return deserializeReceiptResponse(rawResponse);
        } catch (JsonProcessingException e) {
            throw new GeminiParseTextException("Failed to build JSON request for GEMINI API from image", e);
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
        return userTime != null
                ?
                "Ты — системный backend-модуль. Твоя единственная задача — прослушать голосовое сообщение или прочитать текст и распарсить напоминание от пользователя. " +
                "Ты должен вернуть JSON-объект с двумя полями: " +
                "1. 'text' (строка, суть того, о чем напомнить, без лишних слов вежливости). " +
                "2. 'targetTime' (строка в формате ISO-8601 'YYYY-MM-DDTHH:mm:ss', рассчитанная на основе текущего времени). " +
                "Текущее время сервера: " + userTime + ". " +
                "Если пользователь не указал конкретное время, выстави targetTime на 1 час вперед от текущего."
                :
                "Ты — системный бухгалтерский модуль. Твоя единственная задача — проанализировать картинку чека, найти все купленные товары и их финальные стоимости. " +
                "Ты должен вернуть результат СТРОГО в формате JSON-массива. Каждая запись внутри массива — это объект с двумя полями: " +
                "1. 'item' (строка, название товара/услуги). " +
                "2. 'price' (число с плавающей точкой, финальная стоимость этого товара). " +
                "Пример ответа: [{\"item\": \"Молоко\", \"price\": 45.50}, {\"item\": \"Хлеб\", \"price\": 18.00}]. " +
                "Не пиши никаких вступлений, markdown-разметки или объяснений. Только чистый массив.";
    }

    private String sendRawHttpRequestToGemini(String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(geminiProperties.apiUrl()))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", geminiProperties.apiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = geminiHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                String aiGeneratedText = rootNode.path("candidates").get(0)
                        .path("content").path("parts").get(0).path("text").asText();
                return aiGeneratedText
                        .replaceAll("```json", "").replaceAll("```", "").trim();
            }
            throw new SendHttpRequestToGeminiException("Gemini returned non-200 status code: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) Thread.currentThread().interrupt();
            throw new SendHttpRequestToGeminiException("Failed try to call gemini.", e);
        }
    }

    private List<ReceiptItemDto> deserializeReceiptResponse(String cleanJson) {
        try {
            return objectMapper.readValue(cleanJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new DeserializeGeminiResponse("Failed to deserialize GEMINI bookkeeper response", e);
        }
    }
}
