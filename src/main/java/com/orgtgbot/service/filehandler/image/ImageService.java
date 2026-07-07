package com.orgtgbot.service.filehandler.image;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.exception.exceptions.service.image.FailedHandleImageException;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final BookkeeperService bookkeeperService;
    private final TelegramSender sender;

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
}
