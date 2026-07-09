package com.orgtgbot.service.services.message;

import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.entity.message.MessageLogEntry;
import com.orgtgbot.repository.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageLogService {

    private final MessageLogRepository messageLogRepository;

    public void log(Long chatId, Integer messageId, MessageType type) {
        MessageLogEntry entry = MessageLogEntry.builder()
                .telegramChatId(chatId)
                .messageId(messageId)
                .messageType(type)
                .build();
        messageLogRepository.save(entry);
    }

    @Transactional
    public List<MessageLogEntry> deleteMessagesByType(Long chatId, MessageType type) {
        List<MessageLogEntry> logs = messageLogRepository.findByTelegramChatIdAndMessageType(chatId, type);
        messageLogRepository.deleteByTelegramChatIdAndMessageType(chatId, type);
        return logs;
    }

    @Transactional
    public void deleteMessageByMessageId(Long chatId, Integer messageId) {
        messageLogRepository.deleteByTelegramChatIdAndMessageId(chatId, messageId);
    }
}
