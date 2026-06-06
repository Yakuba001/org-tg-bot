package com.orgtgbot.repository;

import com.orgtgbot.entity.user.InviteCodeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteCodeRepository extends JpaRepository<InviteCodeEntry, Long> {

    boolean existsByCode(String code);
}
