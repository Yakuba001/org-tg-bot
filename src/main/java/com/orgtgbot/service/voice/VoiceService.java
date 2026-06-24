package com.orgtgbot.service.voice;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
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
    private final TelegramSender sender;

    @Async
    public void handleVoiceAsync(Long chatId, Voice voice, Integer botMenuId, GeneralFields currentField) {
        log.info("[VOICE-SERVICE] Фоновый поток [{}] начал обработку аудио", Thread.currentThread().getName());

        try {
            GetFile getFileMethod = new GetFile(voice.getFileId());
            File file = telegramClient.execute(getFileMethod);
            String downloadUrl =
                    "https://api.telegram.org/file/bot" + telegramBotProperties.token() + "/" + file.getFilePath();

            byte[] audioBytes;
            try (InputStream inputStream = URI.create(downloadUrl).toURL().openStream()) {
                audioBytes = inputStream.readAllBytes();
            }
            log.info("[VOICE-SERVICE] Файл скачан в байты. Передаем в ReminderService...");

            reminderService.addVoiceRemind(chatId, audioBytes);
            log.info("[VOICE-SERVICE] Голосовое напоминание успешно сохранено в БД.");

            sender.editMarkup(
                    chatId,
                    botMenuId,
                    "Данные приняты из голосового сообщения!\n",
                    KeyboardFactory.buildMenuForGroup(currentField)
            );

        } catch (Exception e) {
            log.error("[VOICE-SERVICE-ERROR] Сбой асинхронной обработки голоса для чата " + chatId, e);
            sender.sendMessage(chatId, "❌ Не удалось распознать или сохранить голосовое напоминание.");
        }
    }
}
