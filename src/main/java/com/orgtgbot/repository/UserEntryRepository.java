package com.orgtgbot.repository;

import com.orgtgbot.entity.user.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntryRepository extends JpaRepository<UserEntry, Long> {

    Optional<UserEntry> findByTelegramChatId(Long telegramChatId);
    boolean existsByTelegramChatId(Long telegramChatId);
}
