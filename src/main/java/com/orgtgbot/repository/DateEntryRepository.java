package com.orgtgbot.repository;

import com.orgtgbot.entity.DatesEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateEntryRepository extends JpaRepository<DatesEntry, Long> {

    List<DatesEntry> findAllByOrderByIdAsc();
}
