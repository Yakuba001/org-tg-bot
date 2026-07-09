package com.orgtgbot.integration.service.message;

import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.entity.message.MessageLogEntry;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.MessageLogRepository;
import com.orgtgbot.service.services.message.MessageLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageLogServiceIT extends BaseIntegrationTest {

    @Autowired
    private MessageLogService messageLogService;

    @Autowired
    private MessageLogRepository messageLogRepository;

    private static final Long CHAT_ID = 555444L;
    private static final Integer MSG_ID = 777;

    @BeforeEach
    void setUp() {
        messageLogRepository.deleteAll();
    }

    @Test
    void log_ShouldPhysicallySaveToPostgresContainer() {
        messageLogService.log(CHAT_ID, MSG_ID, MessageType.USER_INPUT);

        List<MessageLogEntry> actualLogs = messageLogRepository
                .findByTelegramChatIdAndMessageType(CHAT_ID, MessageType.USER_INPUT);

        assertEquals(1, actualLogs.size());

        MessageLogEntry saved = actualLogs.getFirst();

        assertNotNull(saved.getId());
        assertEquals(MSG_ID, saved.getMessageId());
    }

    @Test
    void deleteMessageByMessageId_ShouldPhysicallyDeleteFromPostgresContainer() {
        messageLogService.log(CHAT_ID, MSG_ID, MessageType.USER_INPUT);

        assertDoesNotThrow(() -> messageLogService.deleteMessageByMessageId(CHAT_ID, MSG_ID));

        List<MessageLogEntry> actualLogs = messageLogRepository
                .findByTelegramChatIdAndMessageType(CHAT_ID, MessageType.USER_INPUT);

        assertEquals(0, actualLogs.size());
    }

    @Test
    void deleteMessagesByType_ShouldPhysicallyDeleteFromPostgresContainer() {
        messageLogService.log(CHAT_ID, MSG_ID, MessageType.USER_INPUT);
        messageLogService.log(CHAT_ID, MSG_ID, MessageType.USER_INPUT);

        assertDoesNotThrow(() -> messageLogService.deleteMessagesByType(CHAT_ID, MessageType.USER_INPUT));

        List<MessageLogEntry> actualLogs = messageLogRepository
                .findByTelegramChatIdAndMessageType(CHAT_ID, MessageType.USER_INPUT);

        assertEquals(0, actualLogs.size());
    }
}
