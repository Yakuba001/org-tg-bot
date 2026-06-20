package com.orgtgbot.repository;

import com.orgtgbot.entity.reminder.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    List<ReminderEntity> findAllByTelegramChatId(Long chatId);

    List<ReminderEntity> findAllByIsSentFalseAndTargetTimeLessThanEqual(LocalDateTime now);
}
