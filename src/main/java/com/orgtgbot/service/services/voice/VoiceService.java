package com.orgtgbot.service.services.voice;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.exception.exceptions.voice.FailedHandleVoiceException;
import com.orgtgbot.service.services.reminder.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class VoiceService {

    private final TelegramClient telegramClient;
    private final TelegramBotProperties telegramBotProperties;
    private final ReminderService reminderService;
    private final TelegramSender sender;
    private final TelegramFileDownloader fileDownloader;

    @Async
    public void handleVoiceAsync(Long chatId, Voice voice, Integer botMenuId, GeneralFields currentField) {
        try {
            GetFile getFileMethod = new GetFile(voice.getFileId());
            File file = telegramClient.execute(getFileMethod);
            String downloadUrl = "https://api.telegram.org/file/bot" + telegramBotProperties.token() + "/" + file.getFilePath();

            byte[] audioBytes = fileDownloader.downloadAsBytes(downloadUrl);
            reminderService.addVoiceRemind(chatId, audioBytes);
            sender.editMarkup(
                    chatId,
                    botMenuId,
                    "Данные приняты из голосового сообщения!\n",
                    KeyboardFactory.buildMenuForGroup(currentField));
        } catch (Exception e) {
            throw new FailedHandleVoiceException(chatId, "Failed handle voice",  e);
        }
    }
}
