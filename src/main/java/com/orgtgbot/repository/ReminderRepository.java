package com.orgtgbot.repository;

import com.orgtgbot.entity.reminder.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    List<ReminderEntity> findAllByTelegramChatId(Long chatId);

    List<ReminderEntity> findAllByIsSentFalseAndTargetTimeLessThanEqual(LocalDateTime now);

    @Modifying
    @Query("DELETE FROM ReminderEntity r WHERE r.isSent = true AND r.targetTime < :timeAgo")
    void deleteOldSentReminders(@Param("timeAgo") LocalDateTime timeAgo);
}
