package com.orgtgbot.service.filehandler.image;

import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.exception.exceptions.service.image.FailedHandleImageException;
import com.orgtgbot.service.filehandler.TelegramFileDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class ImageChecker {

    private final TelegramClient telegramClient;
    private final TelegramFileDownloader fileDownloader;
    private final TelegramBotProperties telegramBotProperties;

    public byte[] downloadImageBytes(String fileId) {
        try {
            File file = telegramClient.execute(new GetFile(fileId));
            String downloadUrl = "https://api.telegram.org/file/bot" + telegramBotProperties.token() + "/" + file.getFilePath();
            return fileDownloader.downloadAsBytes(downloadUrl);
        } catch (Exception e) {
            throw new FailedHandleImageException("Failed to download image bytes from Telegram API", e);
        }
    }

    public String getMimeType(String fileId) {
        try {
            File file = telegramClient.execute(new GetFile(fileId));
            return file.getFilePath().endsWith(".png") ? "image/png" : "image/jpeg";
        } catch (Exception e) {
            return "image/jpeg";
        }
    }
}
