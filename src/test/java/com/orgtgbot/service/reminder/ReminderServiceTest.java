package com.orgtgbot.service.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.mapper.reminder.ReminderMapper;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import com.orgtgbot.service.services.reminder.ReminderService;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReminderServiceTest {

    @Mock private ReminderRepository reminderRepository;
    @Mock private GeminiParserService geminiParserService;
    @Mock private UserStateService userStateService;

    @Captor
    private ArgumentCaptor<ReminderEntity> reminderEntityCaptor;

    private ReminderService reminderService;

    @Spy
    private ReminderMapper reminderMapper = Mappers.getMapper(ReminderMapper.class);

    private StateManager exampleState;
    private ReminderDto exampleReminder;
    private LocalDateTime testTime;
    private String testText;

    @BeforeEach
    void setUp() {
        reminderService = new ReminderService(reminderRepository, geminiParserService, userStateService, reminderMapper);
        testTime = LocalDateTime.of(2026, 6, 29, 13, 0, 0);
        testText = "Купить молоко";
        exampleState = StateManager.builder()
                .userLastActivityTime(testTime)
                .build();
        exampleReminder = new ReminderDto(testText, testTime);
    }

    @Test
    void addRemind() {
        when(userStateService.getCurrentState(anyLong())).thenReturn(exampleState);
        when(geminiParserService.parseReminder(any(), any())).thenReturn(exampleReminder);

        reminderService.addRemind(1L, testText);

        verify(reminderRepository).save(reminderEntityCaptor.capture());
        ReminderEntity savedEntity = reminderEntityCaptor.getValue();

        assertNotNull(savedEntity);
        assertThat(savedEntity.getText()).isEqualTo(testText);
        assertThat(savedEntity.getTargetTime()).isEqualTo(testTime);
        assertThat(savedEntity.getTelegramChatId()).isEqualTo(1L);
    }

    @Test
    void addVoiceRemind() {
        when(userStateService.getCurrentState(anyLong())).thenReturn(exampleState);
        when(geminiParserService.parseVoiceReminder(any(), any())).thenReturn(exampleReminder);

        reminderService.addVoiceRemind(1L, new byte[]{1, 2, 3, 4});

        verify(reminderRepository).save(reminderEntityCaptor.capture());
        ReminderEntity savedEntity = reminderEntityCaptor.getValue();

        assertNotNull(savedEntity);
        assertThat(savedEntity.getText()).isEqualTo(testText);
        assertThat(savedEntity.getTargetTime()).isEqualTo(testTime);
        assertThat(savedEntity.getTelegramChatId()).isEqualTo(1L);
    }

    @Test
    void getAllReminders_returnCorrectString() {
        List<ReminderEntity> exampleList = List.of(
                ReminderEntity.builder().telegramChatId(1L).targetTime(testTime).text(testText).build(),
                ReminderEntity.builder().telegramChatId(1L).targetTime(testTime).text(testText).build());
        when(reminderRepository.findAllByTelegramChatId(anyLong())).thenReturn(exampleList);

        String result = reminderService.getAllRemindersFormatted(1L);

        assertThat(result).contains(testText);
        assertThat(result).contains("29.06.2026 13:00");
    }

    @Test
    void getAllReminders_returnStringWithRule() {
        List<ReminderEntity> exampleList = List.of();
        when(reminderRepository.findAllByTelegramChatId(anyLong())).thenReturn(exampleList);

        String result = reminderService.getAllRemindersFormatted(1L);

        assertThat(result).contains("\nУ вас пока нет активных напоминаний.");
    }
}
