package com.orgtgbot.service;

import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.orgtgbot.bot.keyboard.Buttons.*;
import static com.orgtgbot.bot.state.UserState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProbegServiceTest {

    @Mock
    private ReportEntryRepository reportEntryRepository;

    @InjectMocks
    private ProbegService probegService;

    List<ReportEntry> reportEntries;

    @BeforeEach
    void setUp() {
        reportEntries = new ArrayList<>(List.of(
                ReportEntry.builder().dayNumber(1).morningKm(100).eveningKm(200).totalKm(300).route("route69").build(),
                ReportEntry.builder().dayNumber(2).morningKm(101).eveningKm(201).totalKm(301).build(),
                ReportEntry.builder().dayNumber(3).morningKm(0).eveningKm(0).totalKm(0).build(),
                ReportEntry.builder().dayNumber(4).morningKm(103).eveningKm(203).totalKm(303).build(),
                ReportEntry.builder().dayNumber(5).morningKm(104).eveningKm(204).totalKm(304).build()));
    }

    @Test
    void firstStart_returnCorrectResult() {
        when(reportEntryRepository.findAllByOrderByDayNumberAsc()).thenReturn(List.of());
        when(reportEntryRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        probegService.firstStart();

        verify(reportEntryRepository, times(1)).findAllByOrderByDayNumberAsc();
        verify(reportEntryRepository, times(1)).saveAll(argThat(collection -> {
            List<ReportEntry> savedList = (List<ReportEntry>) collection;
            if (savedList.size() != 5) return false;
            for (int i = 0; i < 5; i++) {
                if (savedList.get(i).getDayNumber() != (i + 1)) {
                    return false;
                }
            }
            return true;
        }));
    }

    @Test
    void firstStart_returnNothingWithNonEmptyCollection() {
        when(reportEntryRepository.findAllByOrderByDayNumberAsc()).thenReturn(List.of(new ReportEntry()));

        probegService.firstStart();

        verify(reportEntryRepository, times(1)).findAllByOrderByDayNumberAsc();
        verify(reportEntryRepository, never()).saveAll(anyList());
    }

    @Test
    void clearAll_returnCorrectResults() {
        when(reportEntryRepository.findAllByOrderByDayNumberAsc()).thenReturn(reportEntries);

        probegService.clearAll();
        boolean allFieldsAreZero = reportEntries.stream()
                        .allMatch(e -> e.getMorningKm() == 0
                                && e.getEveningKm() == 0
                                && e.getTotalKm() == 0);

        verify(reportEntryRepository, times(1)).findAllByOrderByDayNumberAsc();
        assertTrue(allFieldsAreZero, "All fields should be zero");
    }

    @Test
    void setAmounts_returnCorrectResults_andCalculateTotalKmWithTwoNonZeroAmounts() {
        when(reportEntryRepository.findAllByOrderByDayNumberAsc()).thenReturn(reportEntries);

        probegService.setAmounts(PROBEG_MORNING_MONDAY, 228, null);
        probegService.setAmounts(PROBEG_EVENING_MONDAY, 229, null);
        probegService.setAmounts(PROBEG_TOTAL_MONDAY, 230, null);
        probegService.setAmounts(ROUTE_MONDAY, null, "route69");

        probegService.setAmounts(PROBEG_MORNING_TUESDAY, 228, null);
        probegService.setAmounts(PROBEG_EVENING_TUESDAY, 500, null);

        probegService.setAmounts(PROBEG_EVENING_WEDNESDAY, 228, null);

        assertThat(reportEntries.getFirst().getMorningKm()).isEqualTo(228);
        assertThat(reportEntries.getFirst().getEveningKm()).isEqualTo(229);
        assertThat(reportEntries.getFirst().getTotalKm()).isEqualTo(230);
        assertThat(reportEntries.getFirst().getRoute()).isEqualTo("route69");
        assertThat(reportEntries.get(1).getTotalKm()).isEqualTo(500 - 228);

        assertThat(reportEntries.get(2).getTotalKm()).isEqualTo(0);
    }

    @Test
    void getAmounts_returnCorrectResults() {
        when(reportEntryRepository.findAllByOrderByDayNumberAsc()).thenReturn(reportEntries);

        probegService.getAmounts(SET_MORNING_MONDAY_KM);
        probegService.getAmounts(SET_EVENING_MONDAY_KM);
        probegService.getAmounts(SET_TOTAL_MONDAY_KM);
        probegService.getAmounts(SET_MONDAY_ROUTE);

        assertThat(probegService.getAmounts(SET_MORNING_MONDAY_KM)).isEqualTo("100");
        assertThat(probegService.getAmounts(SET_EVENING_MONDAY_KM)).isEqualTo("200");
        assertThat(probegService.getAmounts(SET_TOTAL_MONDAY_KM)).isEqualTo("300");
        assertThat(probegService.getAmounts(SET_MONDAY_ROUTE)).isEqualTo("route69");
    }
}
