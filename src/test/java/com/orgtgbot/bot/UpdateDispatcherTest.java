package com.orgtgbot.bot;

import com.orgtgbot.bot.update.UpdateHandler;
import com.orgtgbot.repository.UserEntryRepository;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateDispatcherTest {

    @Mock private UserEntryRepository userEntryRepository;
    @Mock private UserStateService userStateService;
    @Mock private UpdateHandler updateHandler;
    @Mock private Update update;
    @Mock private Message message;
    @Mock private CallbackQuery callbackQuery;

    private UpdateDispatcher updateDispatcher;

    private final Long chatId = 777L;
    private final Integer dateSeconds = 1719756000;

    @BeforeEach
    void setUp() {
        updateDispatcher = new UpdateDispatcher(
                userEntryRepository,
                userStateService,
                List.of(updateHandler)
        );
    }

    @Test
    void dispatch_WithNoChatId_ShouldThrowIllegalStateException() {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                updateDispatcher.dispatch(update)
        );

        assertEquals("ChatId not found", exception.getMessage());
        verifyNoInteractions(userEntryRepository, userStateService, updateHandler);
    }

    @Test
    void dispatch_WithRegisteredUser_ShouldUpdateActivityAndCallHandler() throws Exception {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getDate()).thenReturn(dateSeconds);
        when(userEntryRepository.existsByTelegramChatId(chatId)).thenReturn(true);
        when(updateHandler.canHandle(update, true)).thenReturn(true);

        updateDispatcher.dispatch(update);

        verify(userStateService).updateLastActivityTime(eq(chatId), any(LocalDateTime.class));
        verify(updateHandler).handle(update, chatId);
    }

    @Test
    void dispatch_WithUnregisteredUser_ShouldNotUpdateActivityButCallHandler() throws Exception {
        when(update.hasMessage()).thenReturn(false);
        when(update.hasCallbackQuery()).thenReturn(true);
        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getDate()).thenReturn(dateSeconds);
        when(userEntryRepository.existsByTelegramChatId(chatId)).thenReturn(false);
        when(updateHandler.canHandle(update, false)).thenReturn(true);

        updateDispatcher.dispatch(update);

        verify(userStateService, never()).updateLastActivityTime(anyLong(), any());
        verify(updateHandler).handle(update, chatId);
    }

    @Test
    void dispatch_WhenNoHandlerCanHandle_ShouldThrowIllegalArgumentException() throws Exception {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);
        when(message.getDate()).thenReturn(dateSeconds);

        when(userEntryRepository.existsByTelegramChatId(chatId)).thenReturn(true);
        when(updateHandler.canHandle(update, true)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                updateDispatcher.dispatch(update)
        );

        assertEquals("Запрос не поддерживается системой", exception.getMessage());
        verify(updateHandler, never()).handle(any(), anyLong());
    }
}
