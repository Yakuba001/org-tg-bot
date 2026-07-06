package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.filehandler.image.ImageService;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhotoUpdateHandlerTest {

    @Mock private ImageService imageService;
    @Mock private UserStateService userStateService;
    @Mock private TelegramSender telegramSender;
    @Mock private Update update;
    @Mock private Message message;
    @Mock private PhotoSize lowResPhoto;
    @Mock private PhotoSize highResPhoto;

    private PhotoUpdateHandler handler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final Integer BOT_MENU_ID = 11111;
    private static final GeneralFields TEST_FIELD = GeneralFields.MAIN_RECEIPT;

    @BeforeEach
    void setUp() {
        handler = new PhotoUpdateHandler(imageService, userStateService, telegramSender);
    }

    @Test
    void canHandle_returnTrue() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasPhoto()).thenReturn(true);

        boolean result = handler.canHandle(update, true);

        assertTrue(result);
    }

    @Test
    void canHandle_returnFalse() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasPhoto()).thenReturn(true);

        boolean result = handler.canHandle(update, false);

        assertFalse(result);
    }

    @Test
    void handle_shouldDeleteMessage_andInvokeMethodFromImageService() {
        List<PhotoSize> mockPhotos = List.of(lowResPhoto, highResPhoto);
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(userStateService.getState(CHAT_ID)).thenReturn(TEST_FIELD);
        when(userStateService.getMessageId(CHAT_ID)).thenReturn(BOT_MENU_ID);
        when(message.getPhoto()).thenReturn(mockPhotos);
        when(highResPhoto.getFileId()).thenReturn("fake-telegram-file-id-12345");

        handler.handle(update, CHAT_ID);

        verify(imageService).handleImageAsync(CHAT_ID, highResPhoto.getFileId(), BOT_MENU_ID, TEST_FIELD);
        verify(telegramSender).deleteMessage(CHAT_ID, MESSAGE_ID);
    }

    @Test
    void handle_shouldDeleteMessage_andNotInvokeMethodFromImageService_justSendMessageToUser() {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(userStateService.getState(CHAT_ID)).thenReturn(GeneralFields.GENERAL);

        handler.handle(update, CHAT_ID);

        verifyNoInteractions(imageService);
        verify(telegramSender).sendMessage(CHAT_ID, "❌ В этом разделе отправка изображений не поддерживается.");
        verify(telegramSender).deleteMessage(CHAT_ID, MESSAGE_ID);
    }
}
