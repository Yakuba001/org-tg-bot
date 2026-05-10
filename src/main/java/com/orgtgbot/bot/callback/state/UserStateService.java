package com.orgtgbot.bot.callback.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStateService {

    private final Map<Long, String> states = new ConcurrentHashMap<>();
    private final Map<Long, Integer> lastMessageIds = new ConcurrentHashMap<>();

    public void setState(Long chatId, String state) {
        states.put(chatId, state);
    }

    public String getState(Long chatId) {
        return states.getOrDefault(chatId, "");
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
