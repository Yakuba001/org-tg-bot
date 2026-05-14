package com.orgtgbot.repository;

import com.orgtgbot.entity.DatesEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateEntryRepository extends JpaRepository<DatesEntry, Long> {
}
