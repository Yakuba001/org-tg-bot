package com.orgtgbot.service.filehandler.image;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.exception.exceptions.service.image.FailedHandleImageException;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final BookkeeperService bookkeeperService;
    private final TelegramSender sender;

    public void handleSingleImage(Long chatId,
                                  Integer botMenuId,
                                  GeneralFields currentField,
                                  byte[] imageBytes,
                                  String mimeType) {
        try {
            sender.successUpdateText(chatId, botMenuId, currentField,
                    "Чек принят на обработку, данные заносятся в базу!\n");
            bookkeeperService.addReceipt(chatId, imageBytes, mimeType);
            sender.successUpdateText(chatId, botMenuId, currentField, "Успешно обновлено");
        } catch (Exception e) {
            throw new FailedHandleImageException(chatId, "Failed handle receipt image async", e);
        }
    }

    public void handleMediaGroup(Long chatId,
                                 List<byte[]> images,
                                 String mimeType,
                                 Integer botMenuId,
                                 GeneralFields currentField) {
        try {
            sender.successUpdateText(chatId, botMenuId, currentField,
                    "Альбом чеков (" + images.size() + " шт.) принят на обработку!\n");
            bookkeeperService.addMultipleReceipts(chatId, images, mimeType);
            sender.successUpdateText(chatId, botMenuId, currentField, "Успешно обновлено");
        } catch (Exception e) {
            throw new FailedHandleImageException(chatId, "Failed handle media group receipts", e);
        }
    }
}
