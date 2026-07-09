package com.orgtgbot.unit.service.filehandler.image;

import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.service.filehandler.TelegramFileDownloader;
import com.orgtgbot.service.filehandler.image.ImageChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageCheckerTest {

    @Mock private TelegramClient telegramClient;
    @Mock private TelegramFileDownloader fileDownloader;
    @Mock private TelegramBotProperties telegramBotProperties;
    @Mock private File telegramFile;

    private ImageChecker imageChecker;

    @BeforeEach
    void setUp() {
        imageChecker = new ImageChecker(telegramClient, fileDownloader, telegramBotProperties);
    }

    @Test
    void downloadImageBytes_returnBytes() throws Exception {
        byte[] bytes = new byte[]{1, 2, 3};
        when(telegramClient.execute(any(GetFile.class))).thenReturn(telegramFile);
        when(fileDownloader.downloadAsBytes(anyString())).thenReturn(bytes);

        byte[] result = imageChecker.downloadImageBytes("file_0.png");

        assertEquals(bytes, result);
        verify(telegramClient).execute(any(GetFile.class));
        verify(fileDownloader).downloadAsBytes(anyString());
    }

    @Test
    void getMimeType_returnMimeType() throws Exception {
        when(telegramClient.execute(any(GetFile.class))).thenReturn(telegramFile);
        when(telegramFile.getFilePath()).thenReturn("photos/file_0.png");

        String mimeType = imageChecker.getMimeType("file_0.png");

        assertEquals("image/png", mimeType);
        verify(telegramClient).execute(any(GetFile.class));
    }
}
