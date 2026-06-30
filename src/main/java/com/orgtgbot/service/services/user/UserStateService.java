package com.orgtgbot.service.services.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStateService {

    private final StateManagerRepository stateManagerRepository;
    private final UserEntryRepository userEntryRepository;
    private UserStateService self;

    @Transactional
    public void setState(Long chatId, GeneralFields field) {
        StateManager state = self.getCurrentState(chatId);
        state.setCurrentField(field);
        stateManagerRepository.save(state);
        self.updateCache(chatId, state);
    }

    public GeneralFields getState(Long chatId) {
        return self.getCurrentState(chatId).getCurrentField();
    }

    @Transactional
    public void setStateAndMessageId(Long chatId, GeneralFields field, Integer messageId) {
        StateManager state = self.getCurrentState(chatId);
        state.setCurrentField(field);
        state.setLastBotMenuId(messageId);
        stateManagerRepository.save(state);
        self.updateCache(chatId, state);
    }

    public Integer getMessageId(Long chatId) {
        return self.getCurrentState(chatId).getLastBotMenuId();
    }

    @Transactional
    public void updateLastActivityTime(Long chatId, LocalDateTime activityTime) {
        StateManager state = self.getCurrentState(chatId);
        state.setUserLastActivityTime(activityTime);
        stateManagerRepository.save(state);
        self.updateCache(chatId, state);
    }

    @Transactional
    public void clearState(Long chatId) {
        StateManager state = self.getCurrentState(chatId);
        state.setCurrentField(GeneralFields.NONE);
        state.setLastBotMenuId(null);
        stateManagerRepository.save(state);
        self.updateCache(chatId, state);
    }

    @Cacheable(cacheNames = "states_cache", key = "#chatId")
    public StateManager getCurrentState(Long chatId) {
        return stateManagerRepository.findById(chatId).orElseGet(
                () -> {
                    userEntryRepository.findByTelegramChatId(chatId).orElseThrow(
                            () -> new IllegalStateException("User not found for chat: " + chatId));

                    StateManager newState = StateManager.builder()
                            .telegramChatId(chatId)
                            .currentField(GeneralFields.NONE)
                            .lastBotMenuId(null)
                            .build();
                    return stateManagerRepository.save(newState);
                });
    }

    @CachePut(cacheNames = "states_cache", key = "#chatId")
    public StateManager updateCache(@SuppressWarnings("unused") Long chatId, StateManager state) {
        return state;
    }

    @Autowired
    public void setSelf(@Lazy UserStateService self) {
        this.self = self;
    }
}
