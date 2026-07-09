package com.orgtgbot.repository;

import com.orgtgbot.entity.user.UserEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {

    Optional<UserWorkspace> findByUser(UserEntry user);
    Optional<UserWorkspace> findByUser_TelegramChatId(Long telegramChatId);
}
