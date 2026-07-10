package com.orgtgbot.scheduler.reminder;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.exception.exceptions.service.reminder.SendRemindException;
import com.orgtgbot.service.services.reminder.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderService reminderService;
    private final TelegramSender telegramSender;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndSendReminders() {
        List<ReminderEntity> activeReminders = reminderService.getAllReminders();
        if (activeReminders.isEmpty()) return;
        activeReminders.forEach(reminder -> {
                    try {
                        String messageText = "⏰ **Напоминание!**\n\n" + reminder.getText();
                        telegramSender.sendMessage(reminder.getTelegramChatId(), messageText);
                        reminderService.markAsSent(reminder.getId());
                    } catch (Exception e) {
                        throw new SendRemindException("Failed send remind with id: " + reminder.getId(), e);
                    }
                });
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanOldReminders() {
        reminderService.cleanReminders();
    }
}
