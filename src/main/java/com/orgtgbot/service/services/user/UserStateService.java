package com.orgtgbot.service.services.user;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.repository.UserEntryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStateService {

    private final StateManagerRepository stateManagerRepository;
    private final UserEntryRepository userEntryRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void dropConstraintOnStart() {
        try {
            entityManager.createNativeQuery(
                    "ALTER TABLE state_manager DROP CONSTRAINT IF EXISTS fkkjfh7xql4lhy4xa9ipqi4lmmo"
            ).executeUpdate();
        } catch (Exception e) {

        }
    }

    @Transactional
    public void setState(Long chatId, GeneralFields field) {
        getCurrentState(chatId).setCurrentField(field);
    }

    public GeneralFields getState(Long chatId) {
        return getCurrentState(chatId).getCurrentField();
    }

    @Transactional
    public void setMessageId(Long chatId, Integer messageId) {
        getCurrentState(chatId).setLastBotMenuId(messageId);
    }

    public Integer getMessageId(Long chatId) {
        return getCurrentState(chatId).getLastBotMenuId();
    }

    @Transactional
    public void clearState(Long chatId) {
        StateManager result = getCurrentState(chatId);
        result.setCurrentField(GeneralFields.NONE);
        result.setLastBotMenuId(null);
    }

    @Transactional
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
}
