package com.orgtgbot.bot.update;

import com.orgtgbot.aggregator.MediaGroupAggregator;
import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.filehandler.image.ImageChecker;
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
    private final ImageChecker imageChecker;
    private final MediaGroupAggregator aggregator;

    @Override
    public boolean canHandle(Update update, boolean isUserRegistered) {
        return update.hasMessage() && update.getMessage().hasPhoto() && isUserRegistered;
    }

    @Override
    public void handle(Update update, Long chatId) {
        Message message = update.getMessage();
        Integer messageId = message.getMessageId();
        GeneralFields currentField = userStateService.getState(chatId);
        Integer botMenuId = userStateService.getMessageId(chatId);
        sender.deleteMessage(chatId, messageId);
        if (currentField != GeneralFields.MAIN_RECEIPT) {
            sender.successUpdateText(chatId, botMenuId, currentField,
                    "❌ В этом разделе отправка изображений не поддерживается.");
            return;
        }
        List<PhotoSize> photoSizes = message.getPhoto();
        PhotoSize biggestPhoto = photoSizes.getLast();
        String fileId = biggestPhoto.getFileId();
        String mediaGroupId = message.getMediaGroupId();
        byte[] imageBytes = imageChecker.downloadImageBytes(fileId);
        String mimeType = imageChecker.getMimeType(fileId);
        if (mediaGroupId == null) {
            imageService.handleSingleImage(chatId, botMenuId, currentField, imageBytes, mimeType);
            return;
        }
        aggregator.aggregate(mediaGroupId, imageBytes, (collectedImages) ->
            imageService.handleMediaGroup(chatId, collectedImages, mimeType, botMenuId, currentField));
    }
}
