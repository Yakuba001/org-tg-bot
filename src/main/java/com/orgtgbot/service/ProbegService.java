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
    public String changeMonday(List<Integer> kilometers) {
        int km1 = kilometers.getFirst();
//        getAll().getFirst().setEveningKm();
        return "Monday: " + km1 + " km.\n";
    }

    public List<ReportEntry> getAll() {
        return repository.findAllByOrderByRowNumberAsc();
    }
}
