package com.orgtgbot.service.user;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.repository.StateManagerRepository;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStateServiceTest {

    @Mock
    private StateManagerRepository stateManagerRepository;

    @InjectMocks
    private UserStateService userStateService;

    private StateManager example;

    @BeforeEach
    void setUp() {
        example = StateManager.builder()
                .telegramChatId(1L)
                .currentField(GeneralFields.NONE)
                .lastBotMenuId(null)
                .build();
    }

    @Test
    void setState_shouldSaveState() {
        when(stateManagerRepository.findById(anyLong())).thenReturn(Optional.of(example));

        userStateService.setState(1L, GeneralFields.MAIN_MENU);

        assertThat(example.getCurrentField()).isEqualTo(GeneralFields.MAIN_MENU);
        verify(stateManagerRepository, times(1)).findById(anyLong());
    }

    @Test
    void getState_shouldReturnState() {
        when(stateManagerRepository.findById(anyLong())).thenReturn(Optional.of(example));

        assertThat(userStateService.getState(1L)).isEqualTo(example.getCurrentField());
        verify(stateManagerRepository, times(1)).findById(anyLong());
    }

    @Test
    void setMessageId_shouldSaveMessageId() {
        when(stateManagerRepository.findById(anyLong())).thenReturn(Optional.of(example));

        userStateService.setMessageId(anyLong(), 999);

        assertThat(example.getLastBotMenuId()).isEqualTo(999);
        verify(stateManagerRepository, times(1)).findById(anyLong());
    }

    @Test
    void getMessageId_shouldReturnMessageId() {
        when(stateManagerRepository.findById(anyLong())).thenReturn(Optional.of(example));

        assertThat(userStateService.getMessageId(1L)).isEqualTo(example.getLastBotMenuId());
        verify(stateManagerRepository, times(1)).findById(anyLong());
    }

    @Test
    void clearState_shouldClearState() {
        example.setCurrentField(GeneralFields.MAIN_MENU);
        example.setLastBotMenuId(123);
        when(stateManagerRepository.findById(anyLong())).thenReturn(Optional.of(example));

        userStateService.clearState(1L);

        verify(stateManagerRepository, times(1)).findById(anyLong());
        assertThat(example.getCurrentField()).isEqualTo(GeneralFields.NONE);
        assertThat(example.getLastBotMenuId()).isNull();
    }
}
