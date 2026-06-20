package com.orgtgbot.service.services.reminder;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.entity.reminder.ReminderEntity;
import com.orgtgbot.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReminderSchedulerService {

    private final ReminderRepository reminderRepository;
    private final TelegramSender telegramSender;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void checkAndSendReminders() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
        log.debug("[SCHEDULER] Проверка базы данных на наличие активных напоминаний для времени: {}", now);

        List<ReminderEntity> activeReminders = reminderRepository.findAllByIsSentFalseAndTargetTimeLessThanEqual(now);

        if (activeReminders.isEmpty()) {
            return;
        }

        log.info("[SCHEDULER] Найдено {} напоминаний, готовых к отправке!", activeReminders.size());

        for (ReminderEntity reminder : activeReminders) {
            try {
                String messageText = "⏰ **Напоминание!**\n\n" + reminder.getText();

                telegramSender.sendMessage(reminder.getTelegramChatId(), messageText);

                reminder.setSent(true);

                log.info("[SCHEDULER] Уведомление отправлено пользователю {} для напоминания ID: {}",
                        reminder.getTelegramChatId(), reminder.getId());

            } catch (Exception e) {
                log.error("[SCHEDULER-ERROR] Не удалось отправить напоминание ID: " + reminder.getId(), e);
            }
        }
        reminderRepository.saveAll(activeReminders);
    }
}
