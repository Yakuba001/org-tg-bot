package com.orgtgbot.integration.service;

import com.orgtgbot.dto.DatesEntryDto;
import com.orgtgbot.dto.GeneralEntryDto;
import com.orgtgbot.dto.ReportEntryDto;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.ExcelService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcelServiceIT extends BaseIntegrationTest {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private DateService dateService;
    @Autowired
    private ProbegService probegService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    private static final Long CHAT_ID = 999888L;

    @BeforeEach
    void setUp() {
        userWorkspaceRepository.deleteAll();
        registrationService.registerNewDriver(CHAT_ID, "Ivan");
        dateService.setDate(CHAT_ID, 1, DatesEntryDto.builder().date("10.07.2026").build());
        probegService.updateProbegInfo(CHAT_ID, 1, ReportEntryDto.builder()
                .dayNumber(1)
                .totalKm(150)
                .route("По городу")
                .build());
        generalService.updateGeneralInfo(CHAT_ID, GeneralEntryDto.builder()
                .name("Ivan")
                .carModel("Skoda Octavia")
                .carNumber("AA1111BB")
                .date("Июль 2026")
                .startWeekProbeg(100000)
                .endWeekProbeg(100150)
                .startBalanceLitres(BigDecimal.valueOf(50.0))
                .endBalanceLitres(BigDecimal.valueOf(38.0))
                .fuelNorm(BigDecimal.valueOf(8.0))
                .fueling(0)
                .litresSpend(BigDecimal.valueOf(12.0))
                .totalWeekKm(150)
                .build());
    }

    @Test
    void generateReport_shouldReturnNonEmptyByteArray() {
        byte[] excelBytes = excelService.generateReport(CHAT_ID);

        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);
    }
}
