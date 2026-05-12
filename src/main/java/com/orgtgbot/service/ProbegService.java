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
    public String changeMonday(List<Integer> kilometers) {
        int km1 = kilometers.getFirst();
        getAll().getFirst().setKilometers(km1);
        String listReport = getAll().stream()
                .map(e -> e.getId() + "id/row: " + e.getRowNumber() + ": " + e.getKilometers() + " km.")
                .collect(Collectors.joining("\n"));
        return "Monday: " + km1 + " km.\n" + listReport;
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByRowNumberAsc();
    }
}
