package com.orgtgbot.repository;

import com.orgtgbot.entity.GeneralEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralEntryRepository extends JpaRepository<GeneralEntry, Long> {

    List<GeneralEntry> findAllByOrderByIdAsc();
}
