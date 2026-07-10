package com.orgtgbot.integration.service;

import com.orgtgbot.dto.ReportEntryDto;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.ProbegService;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProbegServiceIT extends BaseIntegrationTest {

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    private ProbegService probegService;

    @Autowired
    private RegistrationService registrationService;

    private ReportEntryDto reportEntryDto;
    private static final Long CHAT_ID = 123L;

    @BeforeEach
    void setUp() {
        userWorkspaceRepository.deleteAll();
        registrationService.registerNewDriver(CHAT_ID, "Ivan");
        reportEntryDto = ReportEntryDto.builder()
                .dayNumber(1)
                .route("Morning route")
                .morningKm(100)
                .eveningKm(120)
                .totalKm(20)
                .build();
    }

    @Test
    void updateProbegInfo_shouldUpdateCorrectly() {
        probegService.updateProbegInfo(CHAT_ID, 1, reportEntryDto);
        ReportEntryDto result = probegService.getReportEntryDto(CHAT_ID, 1);

        assertEquals(reportEntryDto.morningKm(), result.morningKm());
        assertEquals(reportEntryDto.eveningKm(), result.eveningKm());
        assertEquals(reportEntryDto.totalKm(), result.totalKm());
        assertEquals(reportEntryDto.route(), result.route());
    }

    @Test
    void clearAll_shouldSetToAllAmountsZero() {
        probegService.updateProbegInfo(CHAT_ID, 1, reportEntryDto);

        probegService.clearAll(CHAT_ID);

        ReportEntryDto result = probegService.getReportEntryDto(CHAT_ID, 1);

        assertEquals(0, result.morningKm());
        assertEquals(0, result.eveningKm());
        assertEquals(0, result.totalKm());
        assertEquals(" ", result.route());
    }

    @Test
    void getAllDto_shouldReturnListWithFiveElements() {
        List<ReportEntryDto> result = probegService.getAllDto(CHAT_ID);

        assertEquals(5, result.size());
    }
}
