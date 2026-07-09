package com.orgtgbot.unit.service.message;

import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.entity.message.MessageLogEntry;
import com.orgtgbot.repository.MessageLogRepository;
import com.orgtgbot.service.services.message.MessageLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageLogServiceTest {

    @Mock
    private MessageLogRepository messageLogRepository;

    private MessageLogService messageLogService;

    private MessageLogEntry messageLogEntry;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final MessageType MESSAGE_TYPE = MessageType.USER_INPUT;

    @BeforeEach
    void setUp() {
        messageLogService = new MessageLogService(messageLogRepository);
        messageLogEntry = MessageLogEntry
                .builder()
                .telegramChatId(CHAT_ID)
                .messageId(MESSAGE_ID)
                .messageType(MESSAGE_TYPE)
                .build();
    }

    @Test
    void log_ShouldSaveMessageToDb() {
        messageLogService.log(CHAT_ID, MESSAGE_ID, MESSAGE_TYPE);


        ArgumentCaptor<MessageLogEntry> captor = ArgumentCaptor.forClass(MessageLogEntry.class);
        verify(messageLogRepository).save(captor.capture());
        MessageLogEntry capturedLogEntry = captor.getValue();

        assertEquals(CHAT_ID, capturedLogEntry.getTelegramChatId());
        assertEquals(MESSAGE_ID, capturedLogEntry.getMessageId());
        assertEquals(MESSAGE_TYPE, capturedLogEntry.getMessageType());
    }

    @Test
    void deleteMessagesByType_ShouldDeleteMessagesByType() {
        when(messageLogRepository.findByTelegramChatIdAndMessageType(CHAT_ID, MESSAGE_TYPE))
                .thenReturn(List.of(messageLogEntry));

        List<MessageLogEntry> result = messageLogService.deleteMessagesByType(CHAT_ID, MESSAGE_TYPE);

        verify(messageLogRepository).deleteByTelegramChatIdAndMessageType(CHAT_ID, MESSAGE_TYPE);
        assertEquals(1, result.size());
        assertEquals(messageLogEntry, result.getFirst());
    }

    @Test
    void deleteMessagesByType_ShouldReturnEmptyList_WhenNoMessagesFound() {
        when(messageLogRepository.findByTelegramChatIdAndMessageType(CHAT_ID, MESSAGE_TYPE))
                .thenReturn(List.of());

        List<MessageLogEntry> result = messageLogService.deleteMessagesByType(CHAT_ID, MESSAGE_TYPE);

        verify(messageLogRepository).deleteByTelegramChatIdAndMessageType(CHAT_ID, MESSAGE_TYPE);
        assertEquals(0, result.size());
    }

    @Test
    void deleteMessageByMessageId_ShouldDeleteMessageByMessageId() {
        messageLogService.deleteMessageByMessageId(CHAT_ID, MESSAGE_ID);

        verify(messageLogRepository).deleteByTelegramChatIdAndMessageId(CHAT_ID, MESSAGE_ID);
    }
}
