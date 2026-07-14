package com.orgtgbot.unit.service.filehandler.image;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.filehandler.image.ImageService;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock private BookkeeperService bookkeeperService;
    @Mock private TelegramSender telegramSender;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(
                bookkeeperService,
                telegramSender);
    }

    @Test
    void handleSingleImage_shouldSendMessageToUser_andInvokeBookkeeperServiceAddReceiptMethod() {
        Long chatId = 12345L;
        Integer botMenuId = 999;
        GeneralFields currentField = GeneralFields.MAIN_RECEIPT;
        byte[] imageBytes = new byte[]{1, 2, 3};
        String mimeType = "image/jpeg";

        imageService.handleSingleImage(chatId, botMenuId, currentField, imageBytes, mimeType);

        verify(telegramSender, times(2))
                .successUpdateText(eq(chatId), eq(botMenuId), eq(currentField), anyString());
        verify(bookkeeperService).addReceipt(chatId, imageBytes, mimeType);
    }

    @Test
    void handleMediaGroup_shouldSendMessageToUser_andInvokeBookkeeperServiceAddMultipleMethod() {
        Long chatId = 12345L;
        Integer botMenuId = 999;
        GeneralFields currentField = GeneralFields.MAIN_RECEIPT;
        List<byte[]> imageBytes = List.of(new byte[]{1, 2, 3});
        String mimeType = "image/jpeg";

        imageService.handleMediaGroup(chatId, imageBytes, mimeType, botMenuId, currentField);

        verify(telegramSender, times(2))
                .successUpdateText(eq(chatId), eq(botMenuId), eq(currentField), anyString());
        verify(bookkeeperService).addMultipleReceipts(chatId, imageBytes, mimeType);
    }
}
