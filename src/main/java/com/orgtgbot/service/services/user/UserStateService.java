package com.orgtgbot.service.services.user;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.repository.UserEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserStateService {

    private final StateManagerRepository stateManagerRepository;
    private final UserEntryRepository userEntryRepository;

    @Transactional
    public void setState(Long chatId, GeneralFields field) {
        StateManager state = getCurrentState(chatId);
        state.setCurrentField(field);
        stateManagerRepository.save(state);
    }

    public GeneralFields getState(Long chatId) {
        return getCurrentState(chatId).getCurrentField();
    }

    @Transactional
    public void setMessageId(Long chatId, Integer messageId) {
        StateManager state = getCurrentState(chatId);
        state.setLastBotMenuId(messageId);
        stateManagerRepository.save(state);
    }

    public Integer getMessageId(Long chatId) {
        return getCurrentState(chatId).getLastBotMenuId();
    }

    @Transactional
    public void clearState(Long chatId) {
        StateManager state = getCurrentState(chatId);
        state.setCurrentField(GeneralFields.NONE);
        state.setLastBotMenuId(null);
        stateManagerRepository.save(state);
    }

    @Transactional
    public StateManager getCurrentState(Long chatId) {
        log.info("[SERVICE] Вызов getCurrentState для chatId: {}", chatId);

        return stateManagerRepository.findById(chatId).orElseGet(
                () -> {

                    log.warn("[DATABASE] Стейт не найден в БД! Создаю новый для chatId: {}", chatId);

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
}
