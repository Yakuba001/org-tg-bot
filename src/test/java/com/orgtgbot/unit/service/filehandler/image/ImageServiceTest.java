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

import static org.mockito.ArgumentMatchers.*;
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
    void handleImageAsync_shouldSendMessageToUser_andInvokeBookkeeperServiceAddReceiptMethod() {
        Long chatId = 12345L;
        String fileId = "file_0.png";
        Integer botMenuId = 999;
        GeneralFields currentField = GeneralFields.MAIN_RECEIPT;

        imageService.handleImageAsync(chatId, fileId, botMenuId, currentField);

        verify(telegramSender).editMarkup(eq(chatId), eq(botMenuId), anyString(), any());
        verify(bookkeeperService).addReceipt(chatId, fileId);
    }
}
