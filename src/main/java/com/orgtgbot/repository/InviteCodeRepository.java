package com.orgtgbot.repository;

import com.orgtgbot.entity.user.InviteCodeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCodeEntry, Long> {

    boolean existsByCode(String code);
}
