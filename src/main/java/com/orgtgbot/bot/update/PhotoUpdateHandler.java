package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.filehandler.image.ImageService;
import com.orgtgbot.service.services.user.UserStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoUpdateHandler implements UpdateHandler {

    private final ImageService imageService;
    private final UserStateService userStateService;
    private final TelegramSender sender;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasPhoto() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) {
        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        GeneralFields currentField = userStateService.getState(chatId);

        if (currentField == GeneralFields.MAIN_RECEIPT) {
            Integer botMenuId = userStateService.getMessageId(chatId);
            List<PhotoSize> photoSizes = message.getPhoto();
            PhotoSize biggestPhoto = photoSizes.getLast();

            imageService.handleImageAsync(chatId, biggestPhoto.getFileId(), botMenuId, currentField);
        } else {
            sender.sendMessage(chatId, "❌ В этом разделе отправка изображений не поддерживается.");
        }
        sender.deleteMessage(chatId, messageId);
    }
}
