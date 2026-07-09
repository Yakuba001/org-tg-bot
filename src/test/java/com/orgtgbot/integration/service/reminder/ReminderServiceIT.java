package com.orgtgbot.integration.service.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.entity.user.StateManager;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.service.services.gemini.GeminiParserService;
import com.orgtgbot.service.services.reminder.ReminderService;
import com.orgtgbot.service.services.user.UserStateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ReminderServiceIT extends BaseIntegrationTest {

    @Autowired
    private ReminderService reminderService;
    @Autowired
    private ReminderRepository reminderRepository;
    @MockBean
    private GeminiParserService geminiParserService;
    @MockBean
    private UserStateService userStateService;

    private static final Long CHAT_ID = 123L;
    private static final String RAW_TEXT = "Buy milk";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private List<ReminderEntity> reminders;
    private ReminderDto exampleReminderDto;
    private final LocalDateTime targetTime = LocalDateTime.now();
    private StateManager stateManager;

    @BeforeEach
    void setUp() {
        reminderRepository.deleteAll();
        reminders = List.of(
                ReminderEntity.builder()
                        .telegramChatId(CHAT_ID)
                        .text("Buy milk")
                        .targetTime(targetTime)
                        .isSent(false)
                        .build(),
                ReminderEntity.builder()
                        .telegramChatId(CHAT_ID)
                        .text("Buy eggs")
                        .targetTime(targetTime)
                        .isSent(false)
                        .build());
        exampleReminderDto = new ReminderDto(RAW_TEXT, targetTime);
        stateManager = StateManager.builder().userLastActivityTime(targetTime).build();
    }

    @Test
    void addRemind_correctlySavesReminder() {
        when(userStateService.getCurrentState(CHAT_ID)).thenReturn(stateManager);
        when(geminiParserService.parseReminder(anyString(), eq(targetTime))).thenReturn(exampleReminderDto);

        reminderService.addRemind(CHAT_ID, RAW_TEXT);

        List<ReminderEntity> savedReminders = reminderRepository.findAll();
        assertEquals(1, savedReminders.size());
        assertEquals(RAW_TEXT, savedReminders.getFirst().getText());
    }

    @Test
    void addVoiceRemind_correctlySavesReminder() {
        byte[] exampleBytes = new byte[]{1, 2, 3, 4};
        when(userStateService.getCurrentState(CHAT_ID)).thenReturn(stateManager);
        when(geminiParserService.parseVoiceReminder(exampleBytes, targetTime)).thenReturn(exampleReminderDto);

        reminderService.addVoiceRemind(CHAT_ID, exampleBytes);

        List<ReminderEntity> savedReminders = reminderRepository.findAll();
        assertEquals(1, savedReminders.size());
        assertEquals(RAW_TEXT, savedReminders.getFirst().getText());
    }

    @Test
    void getAllRemindersFormatted_returnsCorrectMessage_ifUserDoesNotHaveAnyReminders() {
        String result = reminderService.getAllRemindersFormatted(CHAT_ID);

        assertEquals("\nУ вас пока нет активных напоминаний.", result);
    }

    @Test
    void getAllRemindersFormatted_returnsCorrectMessage_ifUserHasReminders() {
        reminderRepository.saveAll(reminders);

        String answer = reminderService.getAllRemindersFormatted(CHAT_ID);
        String result = "\nВаши напоминания:\n" + reminders.stream()
                .map(r -> "🔹 " + r.getTargetTime().format(FORMATTER) + " — " + r.getText())
                .collect(Collectors.joining("\n"));

        assertEquals(result, answer);
    }
}
