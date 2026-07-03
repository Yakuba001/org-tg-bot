package com.orgtgbot.service.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.orgtgbot.config.GeminiProperties;
import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.exception.exceptions.service.http.SendHttpRequestToGeminiException;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeminiParserServiceTest {

    @Mock
    private GeminiProperties geminiProperties;

    @Mock
    private HttpClient geminiHttpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private GeminiParserService geminiParserService;

    private final LocalDateTime userTime =
            LocalDateTime.of(2026, 6, 29, 13, 0, 0);

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.geminiParserService = new GeminiParserService(geminiProperties, objectMapper, geminiHttpClient);
        when(geminiProperties.apiKey()).thenReturn("fake-api-key");
    }

    @Test
    void parseReminder_success_notThrow() throws Exception {
        String rawText = "Напомни купить молоко через час";
        String mockGeminiJsonResponse = """
                {
                  "candidates": [
                    {
                      "content": {
                        "parts": [
                          {
                            "text": "{\\"text\\":\\"Купить молоко\\",\\"targetTime\\":\\"2026-06-29T14:00:00\\"}"
                          }
                        ]
                      }
                    }
                  ]
                }""";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(mockGeminiJsonResponse);
        when(geminiHttpClient.<String>send(any(), any())).thenReturn(httpResponse);

        ReminderDto result = geminiParserService.parseReminder(rawText, userTime);

        assertNotNull(result);
        assertEquals("Купить молоко", result.text());
        assertEquals(LocalDateTime.of(2026, 6, 29, 14, 0, 0), result.targetTime());
    }

    @Test
    void parseReminder_fail_throwSendHttpRequestToGeminiException() throws Exception {
        String rawText = "Напомни позвонить маме";

        when(httpResponse.statusCode()).thenReturn(503);
        when(geminiHttpClient.<String>send(any(), any())).thenReturn(httpResponse);

        SendHttpRequestToGeminiException result = assertThrows(SendHttpRequestToGeminiException.class,
                () -> geminiParserService.parseReminder(rawText, userTime));

        assertThat(result.getMessage()).contains("Gemini returned non-200 status code");
    }

    @Test
    void parseVoiceReminder_success_notThrow() throws Exception {
        byte[] mockAudioBytes = new byte[]{1, 2, 3, 4};
        String mockGeminiJsonResponse = """
                {
                  "candidates": [
                    {
                      "content": {
                        "parts": [
                          {
                            "text": "{\\"text\\":\\"Голосовое напоминание\\",\\"targetTime\\":\\"2026-06-29T14:00:00\\"}"
                          }
                        ]
                      }
                    }
                  ]
                }""";

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(mockGeminiJsonResponse);
        when(geminiHttpClient.<String>send(any(), any())).thenReturn(httpResponse);

        ReminderDto result = geminiParserService.parseVoiceReminder(mockAudioBytes, userTime);

        assertNotNull(result);
        assertEquals("Голосовое напоминание", result.text());
        assertEquals(LocalDateTime.of(2026, 6, 29, 14, 0, 0), result.targetTime());
    }

    @Test
    void parseVoiceReminder_fail_throw() throws Exception {
        byte[] mockAudioBytes = new byte[]{1, 2, 3, 4};

        when(geminiHttpClient.<String>send(any(), any()))
                .thenThrow(new IOException("Connection reset"));

        SendHttpRequestToGeminiException result = assertThrows(SendHttpRequestToGeminiException.class,
                () -> geminiParserService.parseVoiceReminder(mockAudioBytes, userTime));

        assertThat(result.getMessage()).contains("Failed try to call gemini");
    }
}
