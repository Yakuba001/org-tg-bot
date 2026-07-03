package com.orgtgbot.service.services.reminder;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.exception.exceptions.service.reminder.SendRemindException;
import com.orgtgbot.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderSchedulerService {

    private final ReminderRepository reminderRepository;
    private final TelegramSender telegramSender;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
        List<ReminderEntity> activeReminders = reminderRepository.findAllByIsSentFalseAndTargetTimeLessThanEqual(now);
        if (activeReminders.isEmpty())
            return;

        for (ReminderEntity reminder : activeReminders) {
            try {
                String messageText = "⏰ **Напоминание!**\n\n" + reminder.getText();
                telegramSender.sendMessage(reminder.getTelegramChatId(), messageText);
                reminder.setSent(true);
            } catch (Exception e) {
                throw new SendRemindException("Failed send remind with id: " + reminder.getId(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void cleanOldReminders() {
        LocalDateTime timeAgo = LocalDateTime.now(ZoneId.of("Europe/Kiev")).minusDays(1);
        reminderRepository.deleteOldSentReminders(timeAgo);
    }
}
