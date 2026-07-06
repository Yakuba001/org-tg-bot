package com.orgtgbot.service.filehandler.image;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.exception.exceptions.service.image.FailedHandleImageException;
import com.orgtgbot.service.filehandler.TelegramFileDownloader;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final TelegramClient telegramClient;
    private final TelegramBotProperties telegramBotProperties;
    private final BookkeeperService bookkeeperService;
    private final TelegramSender sender;
    private final TelegramFileDownloader fileDownloader;

    @Async
    public void handleImageAsync(Long chatId, String fileId, Integer botMenuId, GeneralFields currentField) {
        try {
            sender.editMarkup(
                    chatId,
                    botMenuId,
                    "Чек принят на обработку, данные заносятся в базу!\n",
                    KeyboardFactory.buildMenuForGroup(currentField));
            bookkeeperService.addReceipt(chatId, fileId);
        } catch (Exception e) {
            throw new FailedHandleImageException(chatId, "Failed handle receipt image async", e);
        }
    }

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
