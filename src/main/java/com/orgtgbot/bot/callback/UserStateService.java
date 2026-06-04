package com.orgtgbot.bot.callback;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStateService {

    private final Map<Long, GeneralFields> userStates = new ConcurrentHashMap<>();
    private final Map<Long, Integer> messageIds = new ConcurrentHashMap<>();

    public void setState(Long chatId, GeneralFields field) {
        userStates.put(chatId, field);
    }

    public GeneralFields getState(Long chatId) {
        return userStates.getOrDefault(chatId, GeneralFields.NONE);
    }

    public void setMessageId(Long chatId, Integer messageId) {
        messageIds.put(chatId, messageId);
    }

    public Integer getMessageId(Long chatId) {
        return messageIds.get(chatId);
    }

    public void clearState(Long chatId) {
        userStates.remove(chatId);
        messageIds.remove(chatId);
    }
}
