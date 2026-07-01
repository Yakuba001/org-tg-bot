package com.orgtgbot.service;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
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

    @Mock private ReportEntry reportEntry;

    private BotFacade botFacade;

    private final Long chatId = 123L;

    @BeforeEach
    void setUp() {
        botFacade = new BotFacade(generalService, probegService, dateService, reminderService);
    }

    @Test
    void getAmount_ShouldReturnMorningKmFromProbegService() {
        int dayNumber = 1;
        int expectedKm = 150;
        when(probegService.getReportEntry(chatId, dayNumber)).thenReturn(reportEntry);
        when(reportEntry.getMorningKm()).thenReturn(expectedKm);

        String result = botFacade.getAmount(GeneralFields.SET_MORNING_MONDAY_KM, chatId);

        assertEquals(String.valueOf(expectedKm), result);
    }

    @Test
    void setAmount_ShouldCallUpdateProbegInfoWithCorrectDto() {
        String amountToSet = "220";
        int dayNumber = 1;

        botFacade.setAmount(GeneralFields.SET_MORNING_MONDAY_KM, amountToSet, chatId);

        ArgumentCaptor<ProbegUpdateDto> dtoCaptor = ArgumentCaptor.forClass(ProbegUpdateDto.class);

        verify(probegService).updateProbegInfo(eq(chatId), eq(dayNumber), dtoCaptor.capture());
        ProbegUpdateDto capturedDto = dtoCaptor.getValue();
        assertEquals(220, capturedDto.morningKm());
    }
}
