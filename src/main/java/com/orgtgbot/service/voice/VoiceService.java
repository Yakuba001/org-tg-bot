package com.orgtgbot.service.voice;

import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.service.services.reminder.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoiceService {

    private final TelegramClient telegramClient;
    private final TelegramBotProperties telegramBotProperties;
    private final ReminderService reminderService;

    @Async
    public void handleVoice(Long chatId, Voice voice) {
        log.info("[VOICE-SERVICE] Фоновый поток [{}] начал обработку аудио", Thread.currentThread().getName());

        try {
            GetFile getFileMethod = new GetFile(voice.getFileId());
            File file = telegramClient.execute(getFileMethod);
            String filePath = file.getFilePath();

            String token = telegramBotProperties.token();
            String downloadUrl = "https://api.telegram.org/file/bot" + token + "/" + filePath;

            byte[] audioBytes;
            try (InputStream inputStream = URI.create(downloadUrl).toURL().openStream()) {
                audioBytes = inputStream.readAllBytes();
            }
            log.info("[VOICE-SERVICE] Файл скачан, передаем в ReminderService...");

            reminderService.addVoiceRemind(chatId, audioBytes);

        } catch (Exception e) {
            log.error("[VOICE-SERVICE-ERROR] Сбой асинхронной обработки голоса для чата " + chatId, e);
        }
    }
}
