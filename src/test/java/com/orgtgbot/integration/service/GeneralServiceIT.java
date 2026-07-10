package com.orgtgbot.integration.service;

import com.orgtgbot.dto.GeneralEntryDto;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneralServiceIT extends BaseIntegrationTest {

    @Autowired
    private GeneralService generalService;

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    private RegistrationService registrationService;

    private static final Long CHAT_ID = 123L;
    private GeneralEntryDto generalEntryDto;

    @BeforeEach
    void setUp() {
        userWorkspaceRepository.deleteAll();
        registrationService.registerNewDriver(CHAT_ID, "Ivan");
        generalEntryDto = GeneralEntryDto.builder()
                .name("Ivan")
                .date("01.01.2022")
                .carModel("Mercedes")
                .carNumber("123")
                .startWeekProbeg(10000)
                .endWeekProbeg(10500)
                .startBalanceLitres(new BigDecimal("100.00"))
                .endBalanceLitres(new BigDecimal("50.00"))
                .totalWeekKm(500)
                .fuelNorm(new BigDecimal("8.50"))
                .litresSpend(new BigDecimal("50.00"))
                .fueling(50)
                .build();
    }

    @Test
    void updateGeneralInfo_shouldUpdateEntityInDB() {
        generalService.updateGeneralInfo(CHAT_ID, generalEntryDto);

        GeneralEntryDto result = generalService.getSingleDto(CHAT_ID);

        assertEquals(generalEntryDto.name(), result.name());
        assertEquals(generalEntryDto.date(), result.date());
        assertEquals(generalEntryDto.carModel(), result.carModel());
        assertEquals(generalEntryDto.carNumber(), result.carNumber());
        assertEquals(generalEntryDto.startWeekProbeg(), result.startWeekProbeg());
        assertEquals(generalEntryDto.endWeekProbeg(), result.endWeekProbeg());
        assertEquals(generalEntryDto.startBalanceLitres(), result.startBalanceLitres());
        assertEquals(generalEntryDto.endBalanceLitres(), result.endBalanceLitres());
        assertEquals(generalEntryDto.totalWeekKm(), result.totalWeekKm());
        assertEquals(generalEntryDto.fuelNorm(), result.fuelNorm());
        assertEquals(generalEntryDto.litresSpend(), result.litresSpend());
        assertEquals(generalEntryDto.fueling(), result.fueling());
    }
}
