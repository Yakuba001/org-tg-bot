package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.CallbackRegistry;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.command.StartCommand;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisteredTextUpdateHandlerTest {

    @Mock private UserStateService userStateService;
    @Mock private StartCommand startCommand;
    @Mock private CallbackRegistry callbackRegistry;
    @Mock private TelegramSender sender;
    @Mock private Update update;
    @Mock private Message message;

    private RegisteredTextUpdateHandler registeredTextUpdateHandler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final Integer BOT_MENU_ID = 11111;

    @BeforeEach
    void setUp() {
        registeredTextUpdateHandler =
                new RegisteredTextUpdateHandler(userStateService, startCommand, callbackRegistry, sender);
    }

    @Test
    void canHandle_WithValidTextMessageAndRegisteredUser_ShouldReturnTrue() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);

        boolean result = registeredTextUpdateHandler.canHandle(update, true);

        assertTrue(result);
    }

    @Test
    void canHandle_WithUnregisteredUser_ShouldReturnFalse() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasText()).thenReturn(true);

        boolean result = registeredTextUpdateHandler.canHandle(update, false);

        assertFalse(result);
    }

    @Test
    void handle_WhenTextIsStartCommand_ShouldExecuteStartCommand() throws Exception {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(message.getText()).thenReturn("/start");
        when(userStateService.getState(CHAT_ID)).thenReturn(GeneralFields.SET_MORNING_MONDAY_KM);

        registeredTextUpdateHandler.handle(update, CHAT_ID);

        verify(sender).deleteMessage(CHAT_ID, MESSAGE_ID);
        verify(startCommand).execute(update);
        verifyNoInteractions(callbackRegistry);
    }

    @Test
    void handle_startCommandIfUserDoesNotHaveState_andInvokeStartCommand() throws Exception {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(message.getText()).thenReturn("Start for unregistered user");
        when(userStateService.getState(CHAT_ID)).thenReturn(null);

        registeredTextUpdateHandler.handle(update, CHAT_ID);

        verify(sender).deleteMessage(CHAT_ID, MESSAGE_ID);
        verify(startCommand).execute(update);
        verifyNoInteractions(callbackRegistry);
    }

    @Test
    void handle_WithActiveState_ShouldTrimTextAndDelegateToCallbackRegistry() throws Exception {
        String userInput = "   150   ";
        String expectedTrimmedInput = "150";
        GeneralFields activeState = GeneralFields.SET_MORNING_MONDAY_KM;

        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(message.getText()).thenReturn(userInput);
        when(userStateService.getState(CHAT_ID)).thenReturn(activeState);
        when(userStateService.getMessageId(CHAT_ID)).thenReturn(BOT_MENU_ID);

        registeredTextUpdateHandler.handle(update, CHAT_ID);

        verify(sender).deleteMessage(CHAT_ID, MESSAGE_ID);
        verify(callbackRegistry).handle(activeState, CHAT_ID, expectedTrimmedInput, BOT_MENU_ID);
        verifyNoInteractions(startCommand);
    }
}
