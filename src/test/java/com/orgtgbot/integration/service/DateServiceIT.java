package com.orgtgbot.integration.service;

import com.orgtgbot.dto.DatesEntryDto;
import com.orgtgbot.integration.BaseIntegrationTest;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.user.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateServiceIT extends BaseIntegrationTest {

    @Autowired
    private DateService dateService;

    @Autowired
    private UserWorkspaceRepository userWorkspaceRepository;

    @Autowired
    private RegistrationService registrationService;

    private static final Long CHAT_ID = 123L;
    private static final Integer DAY_NUMBER = 1;
    private DatesEntryDto datesEntryDto;

    @BeforeEach
    void setUp() {
        userWorkspaceRepository.deleteAll();
        registrationService.registerNewDriver(CHAT_ID, "Ivan");
        datesEntryDto = DatesEntryDto.builder()
                .date("01.01.2022")
                .build();
    }

    @Test
    void setDate_shouldSetNewDate() {
        dateService.setDate(CHAT_ID, DAY_NUMBER, datesEntryDto);

        DatesEntryDto result = dateService.getDatesDto(CHAT_ID, DAY_NUMBER);

        assertEquals(datesEntryDto.date(), result.date());
    }

    @Test
    void getAllDates_shouldReturnListWithFiveElements() {
        List<DatesEntryDto> result = dateService.getAllDto(CHAT_ID);

        assertEquals(5, result.size());
    }
}
