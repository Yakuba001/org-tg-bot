package com.orgtgbot.service.services;

import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.mapper.ProbegMapper;
import com.orgtgbot.repository.ReportEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProbegService {

    private final ReportEntryRepository reportEntryRepository;
    private final ProbegMapper probegMapper;

    public void firstStart() {
        if (getAll().isEmpty()) {
            List<ReportEntry> result = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                result.add(ReportEntry.builder()
                        .dayNumber(i + 1)
                        .route(" ")
                        .morningKm(0)
                        .eveningKm(0)
                        .totalKm(0)
                        .build());
            }
            reportEntryRepository.saveAll(result);
        }
    }

    public void clearAll() {
        getAll().forEach(entry -> {
            entry.setMorningKm(0);
            entry.setEveningKm(0);
            entry.setTotalKm(0);
            entry.setRoute(" ");
        });
    }

    public void updateProbegInfo(int dayNumber, ProbegUpdateDto dto) {
        ReportEntry entry = getReportEntry(dayNumber);
        probegMapper.updateEntityFromDto(dto, entry);
        recalculateTotalKm(entry);
    }

    @Transactional(readOnly = true)
    public ReportEntry getReportEntry(int dayNumber) {
        return reportEntryRepository.findAllByOrderByDayNumberAsc().stream()
                .filter(e -> e.getDayNumber() == dayNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Day not found: " + dayNumber));
    }

    @Transactional(readOnly = true)
    public List<ReportEntry> getAll() {
        return reportEntryRepository.findAllByOrderByDayNumberAsc();
    }

    private void recalculateTotalKm(ReportEntry entry) {
        Integer morning = entry.getMorningKm();
        Integer evening = entry.getEveningKm();
        if (morning != null && morning != 0 && evening != null && evening != 0) {
            int range = 1000;
            int result = (evening - morning + range) % range;
            entry.setTotalKm(result);
        }
    }
}
