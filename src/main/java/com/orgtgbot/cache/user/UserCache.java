package com.orgtgbot.cache.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCache {

    private final StateManagerRepository stateManagerRepository;

    @Cacheable(cacheNames = "states_cache", key = "#chatId")
    public StateManager getCurrentState(Long chatId) {
        return findOrCreate(chatId);
    }

    @CachePut(cacheNames = "states_cache", key = "#chatId")
    public StateManager updateCache(@SuppressWarnings("unused") Long chatId, StateManager state) {
        return state;
    }

    private StateManager findOrCreate(Long chatId) {
        return stateManagerRepository.findById(chatId).orElseGet(
                () -> {
                    StateManager newState = StateManager.builder()
                            .telegramChatId(chatId)
                            .currentField(GeneralFields.NONE)
                            .lastBotMenuId(null)
                            .build();
                    return stateManagerRepository.save(newState);
                });
    }
}
