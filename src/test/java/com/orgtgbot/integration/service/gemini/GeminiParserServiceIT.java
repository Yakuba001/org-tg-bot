package com.orgtgbot.integration.service.gemini;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.exception.exceptions.service.http.SendHttpRequestToGeminiException;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class GeminiParserServiceIT extends BaseIntegrationTest {

    private static final WireMockServer wireMockServer = new WireMockServer(8089);

    @Autowired
    private GeminiParserService geminiParserService;

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @AfterEach
    void resetStubs() {
        wireMockServer.resetAll();
    }

    @Test
    void parseReminder_shouldReturnValidReminderDto_whenGeminiResponds200() {
        String rawText = "Напомни созвониться по работе через час";
        LocalDateTime userTime = LocalDateTime.of(2026, 7, 13, 15, 0, 0);
        String innerJson = "{\"text\": \"Созвониться по работе\", \"targetTime\": \"2026-07-13T16:00:00\"}";
        String mockResponseBody = buildGeminiResponseWrapper(innerJson);

        stubFor(post(urlEqualTo("/v1beta/models/gemini-2.5-flash:generateContent"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        ReminderDto result = geminiParserService.parseReminder(rawText, userTime);

        assertNotNull(result);
        assertEquals("Созвониться по работе", result.text());
        assertEquals("2026-07-13T16:00", result.targetTime().toString());
    }

    @Test
    void parseReceiptImage_shouldReturnReceiptItemList_whenGeminiResponds200() {
        byte[] fakeImageBytes = new byte[]{1, 2, 3};
        String mimeType = "image/png";
        String innerJsonArray = "[{\"item\": \"Кофе\", \"price\": 150.0}, {\"item\": \"Пончик\", \"price\": 90.0}]";
        String mockResponseBody = buildGeminiResponseWrapper(innerJsonArray);

        stubFor(post(urlEqualTo("/v1beta/models/gemini-2.5-flash:generateContent"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        List<ReceiptItemDto> result = geminiParserService.parseReceiptImage(fakeImageBytes, mimeType);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Кофе", result.getFirst().item());
        assertEquals(new BigDecimal("150.0"), result.getFirst().price());
    }

    @Test
    void parseReminder_shouldThrowSendHttpRequestToGeminiException_whenGeminiReturns500() {
        stubFor(post(urlEqualTo("/v1beta/models/gemini-2.5-flash:generateContent"))
                .willReturn(aResponse()
                        .withStatus(500)));

        assertThrows(SendHttpRequestToGeminiException.class, () ->
                geminiParserService.parseReminder("Купить молока", LocalDateTime.now()));
    }

    private String buildGeminiResponseWrapper(String innerJson) {
        return "{\n" +
                "  \"candidates\": [\n" +
                "    {\n" +
                "      \"content\": {\n" +
                "        \"parts\": [\n" +
                "          {\n" +
                "            \"text\": \"" + innerJson.replace("\"", "\\\"") + "\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
