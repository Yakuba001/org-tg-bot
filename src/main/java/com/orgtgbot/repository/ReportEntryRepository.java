package com.orgtgbot.repository;

import com.orgtgbot.entity.ReportEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportEntryRepository extends JpaRepository<ReportEntry, Long> {

    List<ReportEntry> findAllByOrderByDayNumberAsc();
}
