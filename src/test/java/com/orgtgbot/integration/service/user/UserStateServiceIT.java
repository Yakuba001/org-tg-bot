package com.orgtgbot.integration.service.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserStateServiceIT extends BaseIntegrationTest {

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private StateManagerRepository stateManagerRepository;
    @Autowired
    private UserStateService userStateService;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final GeneralFields TEST_FIELD = GeneralFields.MAIN_MENU;
    private static final LocalDateTime ACTIVITY_TIME = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

    @BeforeEach
    void setUp() {
        stateManagerRepository.deleteAll();
        Objects.requireNonNull(cacheManager.getCache("states_cache")).clear();
    }

    @Test
    void setState_shouldSaveToDatabaseAndCache() {
        userStateService.setState(CHAT_ID, TEST_FIELD);

        Optional<StateManager> fromDb = stateManagerRepository.findById(CHAT_ID);
        assertTrue(fromDb.isPresent());
        assertEquals(TEST_FIELD, fromDb.get().getCurrentField());

        StateManager fromCache =
                Objects.requireNonNull(cacheManager.getCache("states_cache")).get(CHAT_ID, StateManager.class);
        assertNotNull(fromCache);
        assertEquals(GeneralFields.MAIN_MENU, fromCache.getCurrentField());
    }

    @Test
    void setStateAndMessageId_shouldSaveToDatabaseStateAndMessageId() {
        userStateService.setStateAndMessageId(CHAT_ID, TEST_FIELD, MESSAGE_ID);

        Optional<StateManager> fromDb = stateManagerRepository.findById(CHAT_ID);
        assertTrue(fromDb.isPresent());
        assertEquals(TEST_FIELD, fromDb.get().getCurrentField());
        assertEquals(MESSAGE_ID, fromDb.get().getLastBotMenuId());
    }

    @Test
    void updateLastActivityTime_shouldSaveToDBNewLastActivityTime() {
        userStateService.updateLastActivityTime(CHAT_ID, ACTIVITY_TIME);

        Optional<StateManager> fromDb = stateManagerRepository.findById(CHAT_ID);
        assertTrue(fromDb.isPresent());
        assertEquals(ACTIVITY_TIME, fromDb.get().getUserLastActivityTime());
    }

    @Test
    void clearState_shouldClearStateAndLastBotMenuId() {
        userStateService.setState(CHAT_ID, TEST_FIELD);
        userStateService.clearState(CHAT_ID);

        Optional<StateManager> fromDb = stateManagerRepository.findById(CHAT_ID);
        assertTrue(fromDb.isPresent());
        assertEquals(GeneralFields.NONE, fromDb.get().getCurrentField());
        assertNull(fromDb.get().getLastBotMenuId());
    }

    @Test
    void getState_shouldReturnState() {
        userStateService.setState(CHAT_ID, TEST_FIELD);

        GeneralFields result = userStateService.getState(CHAT_ID);

        assertEquals(TEST_FIELD, result);
    }

    @Test
    void getMessageId_shouldReturnMessageId() {
        userStateService.setStateAndMessageId(CHAT_ID, TEST_FIELD, MESSAGE_ID);

        Integer result = userStateService.getMessageId(CHAT_ID);

        assertEquals(MESSAGE_ID, result);
    }

    @Test
    void getUserLastActivityTime_shouldReturnLastActivityTime() {
        userStateService.updateLastActivityTime(CHAT_ID, ACTIVITY_TIME);

        LocalDateTime result = userStateService.getUserLastActivityTime(CHAT_ID);

        assertEquals(ACTIVITY_TIME, result);
    }
}
