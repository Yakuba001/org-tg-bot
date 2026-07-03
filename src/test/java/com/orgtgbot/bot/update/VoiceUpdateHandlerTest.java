package com.orgtgbot.bot.update;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.services.user.UserStateService;
import com.orgtgbot.service.services.voice.VoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoiceUpdateHandlerTest {

    @Mock private VoiceService voiceService;
    @Mock private UserStateService userStateService;
    @Mock private TelegramSender telegramSender;
    @Mock private Update update;
    @Mock private Message message;
    @Mock private Voice voice;

    private VoiceUpdateHandler voiceUpdateHandler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final Integer BOT_MENU_ID = 11111;

    @BeforeEach
    void setUp() {
        voiceUpdateHandler = new VoiceUpdateHandler(voiceService, userStateService, telegramSender);
    }

    @Test
    void canHandle_WithValidTextMessageAndRegisteredUser_ShouldReturnTrue() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasVoice()).thenReturn(true);

        boolean result = voiceUpdateHandler.canHandle(update, true);

        assertTrue(result);
    }

    @Test
    void canHandle_WithUnregisteredUser_ShouldReturnFalse() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.hasVoice()).thenReturn(true);

        boolean result = voiceUpdateHandler.canHandle(update, false);

        assertFalse(result);
    }

    @Test
    void handle_WhenVoiceMessageIsReceived_andStateIsMainReminder_ShouldHandling() {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(message.getVoice()).thenReturn(voice);
        when(userStateService.getState(CHAT_ID)).thenReturn(GeneralFields.MAIN_REMINDER);
        when(userStateService.getMessageId(CHAT_ID)).thenReturn(BOT_MENU_ID);

        voiceUpdateHandler.handle(update, CHAT_ID);

        verify(voiceService).handleVoiceAsync(CHAT_ID, voice, BOT_MENU_ID, GeneralFields.MAIN_REMINDER);
        verify(telegramSender).deleteMessage(CHAT_ID, MESSAGE_ID);
    }

    @Test
    void handle_WhenVoiceMessageIsReceived_andStateIsNotMainReminder_doNothingAndSendErrorMessage() {
        when(update.getMessage()).thenReturn(message);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);
        when(userStateService.getState(CHAT_ID)).thenReturn(GeneralFields.MAIN_MENU);

        voiceUpdateHandler.handle(update, CHAT_ID);

        verifyNoInteractions(voiceService);
        verify(telegramSender).sendMessage(CHAT_ID, "❌ В этом разделе голосовые сообщения не поддерживаются.");
        verify(telegramSender).deleteMessage(CHAT_ID, MESSAGE_ID);
    }
}
