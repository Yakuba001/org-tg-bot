package com.orgtgbot.repository;

import com.orgtgbot.bot.message.MessageType;
import com.orgtgbot.entity.message.MessageLogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageLogRepository extends JpaRepository<MessageLogEntry, Long> {

    List<MessageLogEntry> findByTelegramChatIdAndMessageType(Long telegramChatId, MessageType messageType);

    void deleteByTelegramChatIdAndMessageId(Long telegramChatId, Integer messageId);

    void deleteByTelegramChatIdAndMessageType(Long telegramChatId, MessageType messageType);
}
