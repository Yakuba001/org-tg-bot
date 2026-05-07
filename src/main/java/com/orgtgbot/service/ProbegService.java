package com.orgtgbot.service;

import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProbegService {

    private final ReportEntryRepository repository;

    @Transactional
    public void saveMileage(List<Integer> kilometers) {
        repository.deleteAll();
        for (int i = 0; i < kilometers.size(); i++) {
            repository.save(ReportEntry.builder()
                    .rowNumber(i + 1)
                    .kilometers(kilometers.get(i))
                    .build());
        }
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByRowNumberAsc();
    }
}
