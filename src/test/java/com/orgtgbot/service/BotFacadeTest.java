package com.orgtgbot.service;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.dto.ReportEntryDto;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import com.orgtgbot.service.services.reminder.ReminderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BotFacadeTest {

    @Mock private GeneralService generalService;
    @Mock private ProbegService probegService;
    @Mock private DateService dateService;
    @Mock private ReminderService reminderService;
    @Mock private BookkeeperService bookkeeperService;

    @Mock private ReportEntryDto reportEntryDto;

    private BotFacade botFacade;

    private final Long chatId = 123L;

    @BeforeEach
    void setUp() {
        botFacade = new BotFacade(generalService, probegService, dateService, reminderService, bookkeeperService);
    }

    @Test
    void getAmount_ShouldReturnMorningKmFromProbegService() {
        int dayNumber = 1;
        int expectedKm = 150;
        when(probegService.getReportEntryDto(chatId, dayNumber)).thenReturn(reportEntryDto);
        when(reportEntryDto.morningKm()).thenReturn(expectedKm);

        String result = botFacade.getAmount(GeneralFields.SET_MORNING_MONDAY_KM, chatId);

        assertEquals(String.valueOf(expectedKm), result);
    }

    @Test
    void setAmount_ShouldCallUpdateProbegInfoWithCorrectDto() {
        String amountToSet = "220";
        int dayNumber = 1;

        botFacade.setAmount(GeneralFields.SET_MORNING_MONDAY_KM, amountToSet, chatId);

        ArgumentCaptor<ReportEntryDto> dtoCaptor = ArgumentCaptor.forClass(ReportEntryDto.class);

        verify(probegService).updateProbegInfo(eq(chatId), eq(dayNumber), dtoCaptor.capture());
        ReportEntryDto capturedDto = dtoCaptor.getValue();
        assertEquals(220, capturedDto.morningKm());
    }
}
