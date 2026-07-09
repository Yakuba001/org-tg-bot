package com.orgtgbot.unit.service.reminder;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.repository.ReminderRepository;
import com.orgtgbot.service.services.reminder.ReminderSchedulerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReminderSchedulerServiceTest {

    @Mock
    private ReminderRepository reminderRepository;

    @Mock
    private TelegramSender telegramSender;

    @InjectMocks
    private ReminderSchedulerService reminderSchedulerService;

    private final LocalDateTime testTime =
            LocalDateTime.of(2026, 6, 29, 13, 0, 0);

    @Test
    void checkAndSendReminders() {
        List<ReminderEntity> exampleList = List.of(
                ReminderEntity.builder().telegramChatId(1L).targetTime(testTime).isSent(false).build());
        when(reminderRepository.findAllByIsSentFalseAndTargetTimeLessThanEqual(any(LocalDateTime.class)))
                .thenReturn(exampleList);
        doNothing().when(telegramSender).sendMessage(anyLong(), anyString());

        reminderSchedulerService.checkAndSendReminders();

        assertThat(exampleList.getFirst().isSent()).isTrue();
    }

    @Test
    void cleanOldReminders() {
        reminderSchedulerService.cleanOldReminders();

        verify(reminderRepository, times(1)).deleteOldSentReminders(any(LocalDateTime.class));
    }
}
