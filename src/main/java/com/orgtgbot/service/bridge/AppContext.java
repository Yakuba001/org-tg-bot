package com.orgtgbot.service.bridge;

import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import com.orgtgbot.service.services.reminder.ReminderService;

public record AppContext(
        GeneralService generalService,
        ProbegService probegService,
        DateService dateService,
        ReminderService reminderService,
        Long chatId
) {}
