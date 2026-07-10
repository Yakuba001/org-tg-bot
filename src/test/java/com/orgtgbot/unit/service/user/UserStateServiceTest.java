package com.orgtgbot.unit.service.user;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.cache.user.UserCache;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStateServiceTest {

    @Mock private StateManagerRepository stateManagerRepository;
    @Mock private UserCache userCache;

    private UserStateService userStateService;

    private StateManager example;
    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;

    @BeforeEach
    void setUp() {
        example = StateManager.builder()
                .telegramChatId(CHAT_ID)
                .currentField(GeneralFields.NONE)
                .lastBotMenuId(null)
                .build();
        userStateService = new UserStateService(userCache, stateManagerRepository);
    }

    @Test
    void setState_shouldSaveState() {
        when(userCache.getCurrentState(anyLong())).thenReturn(example);

        userStateService.setState(CHAT_ID, GeneralFields.MAIN_MENU);

        assertThat(example.getCurrentField()).isEqualTo(GeneralFields.MAIN_MENU);
        verify(stateManagerRepository).save(example);
    }

    @Test
    void getState_shouldReturnState() {
        when(userCache.getCurrentState(anyLong())).thenReturn(example);

        GeneralFields result = userStateService.getState(CHAT_ID);

        assertThat(result).isEqualTo(example.getCurrentField());
    }

    @Test
    void setStateAndMessageId_correctlySetStateAndMessageId() {
        when(userCache.getCurrentState(anyLong())).thenReturn(example);

        userStateService.setStateAndMessageId(CHAT_ID, GeneralFields.MAIN_MENU, MESSAGE_ID);

        assertThat(example.getLastBotMenuId()).isEqualTo(MESSAGE_ID);
        assertThat(example.getCurrentField()).isEqualTo(GeneralFields.MAIN_MENU);
        verify(stateManagerRepository).save(example);
    }

    @Test
    void getMessageId_shouldReturnMessageId() {
        when(userCache.getCurrentState(anyLong())).thenReturn(example);

        Integer result = userStateService.getMessageId(CHAT_ID);

        assertThat(result).isEqualTo(example.getLastBotMenuId());
    }

    @Test
    void clearState_shouldClearState() {
        example.setCurrentField(GeneralFields.MAIN_MENU);
        example.setLastBotMenuId(123);
        when(userCache.getCurrentState(anyLong())).thenReturn(example);

        userStateService.clearState(CHAT_ID);

        assertThat(example.getCurrentField()).isEqualTo(GeneralFields.NONE);
        assertThat(example.getLastBotMenuId()).isNull();
        verify(stateManagerRepository).save(example);
    }
}
