package com.orgtgbot.service.services.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.cache.user.UserCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserStateService {

    private final UserCache userCache;
    private final StateManagerRepository stateManagerRepository;

    @Transactional
    public void setState(Long chatId, GeneralFields field) {
        StateManager state = userCache.getCurrentState(chatId);
        state.setCurrentField(field);
        saveAndRefreshCache(chatId, state);
    }

    @Transactional
    public void setStateAndMessageId(Long chatId, GeneralFields field, Integer messageId) {
        StateManager state = userCache.getCurrentState(chatId);
        state.setCurrentField(field);
        state.setLastBotMenuId(messageId);
        saveAndRefreshCache(chatId, state);
    }

    @Transactional
    public void updateLastActivityTime(Long chatId, LocalDateTime activityTime) {
        StateManager state = userCache.getCurrentState(chatId);
        state.setUserLastActivityTime(activityTime);
        saveAndRefreshCache(chatId, state);
    }

    @Transactional
    public void clearState(Long chatId) {
        StateManager state = userCache.getCurrentState(chatId);
        state.setCurrentField(GeneralFields.NONE);
        state.setLastBotMenuId(null);
        saveAndRefreshCache(chatId, state);
    }

    public GeneralFields getState(Long chatId) {
        return userCache.getCurrentState(chatId).getCurrentField();
    }

    public Integer getMessageId(Long chatId) {
        return userCache.getCurrentState(chatId).getLastBotMenuId();
    }

    public LocalDateTime getUserLastActivityTime(Long chatId) {
        StateManager stateManager = userCache.getCurrentState(chatId);
        return stateManager.getUserLastActivityTime() == null ?
                LocalDateTime.now() : stateManager.getUserLastActivityTime();
    }

    private void saveAndRefreshCache(Long chatId, StateManager state) {
        StateManager savedState = stateManagerRepository.save(state);
        userCache.updateCache(chatId, savedState);
    }
}
