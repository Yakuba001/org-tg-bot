package com.orgtgbot.repository;

import com.orgtgbot.entity.GeneralEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneralEntryRepository extends JpaRepository<GeneralEntry, Long> {
}
