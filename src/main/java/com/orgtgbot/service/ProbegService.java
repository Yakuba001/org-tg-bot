package com.orgtgbot.service;

import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProbegService {

    private final ReportEntryRepository repository;

    @Transactional
    public void firstStart() {
        if (getAll() != null && getAll().size() < 5) {
            for (int i = 0; i < 5; i++) {
                repository.save(ReportEntry.builder()
                        .dayNumber(i + 1)
                        .route(" ")
                        .morningKm(0)
                        .eveningKm(0)
                        .totalKm(0)
                        .build());
            }
        }
    }

    @Transactional
    public String changeMonday(List<Integer> kilometers) {
        int km1 = kilometers.getFirst();
        getAll().getFirst().setTotalKm(km1);
        String listReport = getAll().stream()
                .map(e -> e.getId() + "id/row: " + e.getDayNumber())
                .collect(Collectors.joining("\n"));
        return "Monday: " + km1 + " km.\n" + listReport;
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByDayNumberAsc();
    }
}
