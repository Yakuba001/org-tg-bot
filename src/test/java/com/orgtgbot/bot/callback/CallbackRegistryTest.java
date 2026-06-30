package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.TextAnswerableHandler;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CallbackRegistryTest {

    @Mock private TelegramSender telegramSender;
    @Mock private UserStateService userStateService;
    @Mock private ClickableHandler clickableHandler;
    @Mock private TextAnswerableHandler textAnswerableHandler;
    @Mock private CallbackQuery callbackQuery;

    private CallbackRegistry callbackRegistry;

    private final GeneralFields clickableField = GeneralFields.SET_MORNING_MONDAY_KM;
    private final GeneralFields textAnswerableField = GeneralFields.SET_EVENING_MONDAY_KM;

    private final String queryId = "query_123";
    private final Long chatId = 111L;
    private final Integer messageId = 222;
    private final Integer botMenuId = 333;

    @BeforeEach
    void setUp() {
        when(clickableHandler.callbackData()).thenReturn(clickableField);
        when(textAnswerableHandler.callbackData()).thenReturn(textAnswerableField);

        callbackRegistry = new CallbackRegistry(
                List.of(clickableHandler, textAnswerableHandler),
                telegramSender,
                userStateService
        );
    }

    @Test
    void dispatch_WithValidClickableHandler_ShouldExecuteSuccessfully() throws Exception {
        when(callbackQuery.getId()).thenReturn(queryId);

        callbackRegistry.dispatch(clickableField, callbackQuery, chatId, messageId, botMenuId);

        verify(telegramSender).answerCallback(queryId);
        verify(clickableHandler).handle(callbackQuery);
        verify(userStateService).setStateAndMessageId(chatId, clickableField, messageId);
        verifyNoMoreInteractions(telegramSender);
    }

    @Test
    void dispatch_WithNotClickableHandler_ShouldNotExecuteAndDoNotSetState() throws Exception {
        when(callbackQuery.getId()).thenReturn(queryId);

        callbackRegistry.dispatch(textAnswerableField, callbackQuery, chatId, messageId, botMenuId);

        verify(telegramSender).answerCallback(queryId);
        verify(clickableHandler, never()).handle(callbackQuery);
        verify(userStateService, never()).setStateAndMessageId(anyLong(), any(GeneralFields.class), anyInt());
        verify(telegramSender, times(1)).editMarkup(anyLong(), anyInt(), anyString(), any());
    }

    @Test
    void dispatch_justSendAnMessage_IfHandlerNotFoundInGeneralHandlerMap() throws Exception {
        when(callbackQuery.getId()).thenReturn(queryId);

        callbackRegistry.dispatch(GeneralFields.NONE, callbackQuery, chatId, messageId, botMenuId);

        verify(telegramSender).answerCallback(queryId);
        verify(telegramSender, times(1)).editMarkup(anyLong(), anyInt(), anyString(), any());
    }

    @Test
    void handle_WithValidHandler_ShouldExecuteSuccessfully() throws Exception {
        callbackRegistry.handle(textAnswerableField, chatId, queryId, messageId);

        verify(textAnswerableHandler, times(1)).handle(anyLong(), anyString(), anyInt(), any());
        verify(userStateService, times(1)).clearState(anyLong());
        verifyNoMoreInteractions(telegramSender);
    }

    @Test
    void handle_WithNotTextHandler_ShouldNotExecute() throws Exception {
        callbackRegistry.handle(clickableField, chatId, queryId, messageId);

        verify(textAnswerableHandler, never()).handle(anyLong(), anyString(), anyInt(), any());
        verify(userStateService, times(1)).clearState(anyLong());
        verify(telegramSender, times(1)).editMarkup(anyLong(), anyInt(), anyString(), any());
    }

    @Test
    void handle_justSendAnMessage_IfHandlerNotFoundInGeneralHandlerMap() throws Exception {
        callbackRegistry.handle(GeneralFields.NONE, chatId, queryId, messageId);

        verify(telegramSender, times(1)).editMarkup(anyLong(), anyInt(), anyString(), any());
    }
}
