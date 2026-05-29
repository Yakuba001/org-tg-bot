package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProbegService {

    private final ReportEntryRepository reportEntryRepository;

    @Transactional
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

    @Transactional
    public void clearAll() {
        List<ReportEntry> reportEntries = getAll();
        reportEntries.forEach(reportEntry -> {
            reportEntry.setMorningKm(0);
            reportEntry.setEveningKm(0);
            reportEntry.setTotalKm(0);
        });
    }

    @Transactional
    public void setAmounts(UserState state, Integer km, String route) {
        ReportEntry entry = getReportEntry(state);
        if (state.name().contains("MORNING")) {
            entry.setMorningKm(km);
            recalculateTotalKm(entry);
        }
        else if (state.name().contains("EVENING")) {
            entry.setEveningKm(km);
            recalculateTotalKm(entry);
        }
        else if (state.name().contains("TOTAL")) entry.setTotalKm(km);
        else if (state.name().contains("ROUTE")) entry.setRoute(route != null && !route.equals("-") ? route : " ");
    }

    @Transactional(readOnly = true)
    public String getAmounts(Buttons button) {
        ReportEntry entry = getReportEntry(button);
        if (button.name().contains("MORNING")) return String.valueOf(entry.getMorningKm());
        else if (button.name().contains("EVENING")) return String.valueOf(entry.getEveningKm());
        else if (button.name().contains("TOTAL")) return String.valueOf(entry.getTotalKm());
        else if (button.name().contains("ROUTE")) return entry.getRoute();
        throw new IllegalArgumentException("Unexpected value: " + button);
    }

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

    private ReportEntry getReportEntry(UserState state) {
        return getReportEntry(state.getDayNumber().orElseThrow(() -> new IllegalStateException("Unexpected value")));
    }

    private ReportEntry getReportEntry(Buttons button) {
        return getReportEntry(button.getDayNumber().orElseThrow(() -> new IllegalStateException("Unexpected value")));
    }

    private ReportEntry getReportEntry(int dayNumber) {
        List<ReportEntry> reportEntries = getAll();
        return reportEntries.stream()
                .filter(e -> e.getDayNumber() == dayNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("ReportEntry not found for dayNumber: " + dayNumber));
    }
}
