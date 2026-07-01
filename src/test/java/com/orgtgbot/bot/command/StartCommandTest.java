package com.orgtgbot.bot.command;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartCommandTest {

    @Mock private TelegramClient telegramClient;
    @Mock private Update update;
    @Mock private Message message;

    private StartCommand startCommand;

    private static final Long CHAT_ID = 12345L;

    @BeforeEach
    void setUp() {
        startCommand = new StartCommand(telegramClient);
    }

    @Test
    void execute_ShouldSendStartMessage() throws Exception {
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(CHAT_ID);

        startCommand.execute(update);

        ArgumentCaptor<SendMessage> sendMessageCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramClient).execute(sendMessageCaptor.capture());

        SendMessage resultMessage = sendMessageCaptor.getValue();
        assertEquals(String.valueOf(CHAT_ID), resultMessage.getChatId());
        assertEquals(GeneralFields.MAIN_MENU.getDescription(), resultMessage.getText());
        assertThat(resultMessage.getReplyMarkup()).isEqualTo(KeyboardFactory.buildMenuForGroup(GeneralFields.MAIN_MENU));
    }
}
