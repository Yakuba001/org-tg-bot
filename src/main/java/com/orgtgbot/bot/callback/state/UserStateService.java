package com.orgtgbot.bot.callback.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStateService {

    private final Map<Long, String> states = new ConcurrentHashMap<>();

    public void setState(Long chatId, String state) {
        states.put(chatId, state);
    }

    public String getState(Long chatId) {
        return states.getOrDefault(chatId, "");
    }

    public void removeState(Long chatId) {
        states.remove(chatId);
    }
}
