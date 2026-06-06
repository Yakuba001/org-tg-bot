package com.orgtgbot.repository;

import com.orgtgbot.entity.user.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserEntryRepository extends JpaRepository<UserEntry, Long> {

    Optional<UserEntry> findByTelegramChatId(Long telegramChatId);
    boolean existsByTelegramChatId(Long telegramChatId);
}
