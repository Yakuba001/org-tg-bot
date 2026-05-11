package com.orgtgbot.bot.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStateService {

    private final Map<Long, UserState> states = new ConcurrentHashMap<>();
    private final Map<Long, Integer> lastMessageIds = new ConcurrentHashMap<>();

    public void setState(Long chatId, UserState state) {
        states.put(chatId, state);
    }

    public UserState getState(Long chatId) {
        return states.getOrDefault(chatId, UserState.NONE);
    }

    public void removeState(Long chatId) {
        states.remove(chatId);
    }

    public void setMessageId(Long chatId, Integer messageId) {
        lastMessageIds.put(chatId, messageId);
    }

    public Integer getMessageId(Long chatId) {
        return lastMessageIds.get(chatId);
    }
}
