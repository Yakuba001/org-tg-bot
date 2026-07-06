package com.orgtgbot.service.filehandler.image;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.service.filehandler.TelegramFileDownloader;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock private TelegramClient telegramClient;
    @Mock private TelegramBotProperties telegramBotProperties;
    @Mock private BookkeeperService bookkeeperService;
    @Mock private TelegramSender telegramSender;
    @Mock private TelegramFileDownloader fileDownloader;
    @Mock private File telegramFile;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(
                telegramClient,
                telegramBotProperties,
                bookkeeperService,
                telegramSender,
                fileDownloader);
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

    @Test
    void downloadImageBytes_returnBytes() throws Exception {
        byte[] bytes = new byte[]{1, 2, 3};
        when(telegramClient.execute(any(GetFile.class))).thenReturn(telegramFile);
        when(fileDownloader.downloadAsBytes(anyString())).thenReturn(bytes);

        byte[] result = imageService.downloadImageBytes("file_0.png");

        assertEquals(bytes, result);
        verify(telegramClient).execute(any(GetFile.class));
        verify(fileDownloader).downloadAsBytes(anyString());
    }

    @Test
    void getMimeType_returnMimeType() throws Exception {
        when(telegramClient.execute(any(GetFile.class))).thenReturn(telegramFile);
        when(telegramFile.getFilePath()).thenReturn("photos/file_0.png");

        String mimeType = imageService.getMimeType("file_0.png");

        assertEquals("image/png", mimeType);
        verify(telegramClient).execute(any(GetFile.class));
    }
}
