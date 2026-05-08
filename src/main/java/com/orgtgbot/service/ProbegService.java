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
        List<ReportEntry> entries = getAll();
        for (int i = 0; i < kilometers.size(); i++) {
            if (i < entries.size())
                entries.get(i).setKilometers(kilometers.get(i));
            else
                repository.save(ReportEntry.builder()
                        .rowNumber(i + 1)
                        .kilometers(kilometers.get(i))
                        .build());
        }
    }

    @Transactional
    public void changeMonday(List<Integer> kilometers) {
        getAll().get(0).setKilometers(kilometers.get(0));
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByRowNumberAsc();
    }
}
