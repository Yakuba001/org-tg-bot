package com.orgtgbot.unit.bot.update;

import com.orgtgbot.aggregator.MediaGroupAggregator;
import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.update.PhotoUpdateHandler;
import com.orgtgbot.service.filehandler.image.ImageChecker;
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
    @Mock private ImageChecker imageChecker;
    @Mock private MediaGroupAggregator aggregator;
    @Mock private Update update;
    @Mock private Message message;
    @Mock private PhotoSize lowResPhoto;
    @Mock private PhotoSize highResPhoto;

    private PhotoUpdateHandler handler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;

    @BeforeEach
    void setUp() {
        handler = new PhotoUpdateHandler(imageService, userStateService, telegramSender, imageChecker, aggregator);
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
    void handle_ShouldHandleSingleImage_WhenMediaGroupIdIsNull() {
        Long chatId = 123L;
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        String testFileId = "test_file_id_123";
        when(highResPhoto.getFileId()).thenReturn(testFileId);
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(1);
        when(message.getMediaGroupId()).thenReturn(null);
        when(message.getPhoto()).thenReturn(List.of(lowResPhoto, highResPhoto));
        when(userStateService.getState(chatId)).thenReturn(GeneralFields.MAIN_RECEIPT);
        when(imageChecker.downloadImageBytes(testFileId)).thenReturn(new byte[]{1});
        when(imageChecker.getMimeType(testFileId)).thenReturn("image/jpeg");

        handler.handle(update, chatId);

        verify(imageService).handleSingleImage(eq(chatId), any(), eq(GeneralFields.MAIN_RECEIPT), any(), eq("image/jpeg"));
        verify(aggregator, never()).aggregate(anyString(), any(), any());
    }

    @Test
    void handle_ShouldAggregateImages_WhenMediaGroupIdIsPresent() {
        Long chatId = 123L;
        String mediaGroupId = "group-1";
        String testFileId = "test_file_id_album";
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(highResPhoto.getFileId()).thenReturn(testFileId);
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(1);
        when(message.getMediaGroupId()).thenReturn(mediaGroupId);
        when(message.getPhoto()).thenReturn(List.of(lowResPhoto, highResPhoto));
        when(userStateService.getState(chatId)).thenReturn(GeneralFields.MAIN_RECEIPT);
        when(imageChecker.downloadImageBytes(testFileId)).thenReturn(new byte[]{1});
        when(imageChecker.getMimeType(testFileId)).thenReturn("image/jpeg");

        handler.handle(update, chatId);

        verify(aggregator).aggregate(eq(mediaGroupId), eq(new byte[]{1}), any());
        verify(imageService, never()).handleSingleImage(any(), any(), any(), any(), any());
    }

    @Test
    void handle_shouldDeleteMessage_andNotInvokeMethodFromImageService_justSendMessageToUser() {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(userStateService.getState(CHAT_ID)).thenReturn(GeneralFields.GENERAL);

        handler.handle(update, CHAT_ID);

        verifyNoInteractions(imageService);
        verify(telegramSender).deleteMessage(CHAT_ID, MESSAGE_ID);
    }
}
