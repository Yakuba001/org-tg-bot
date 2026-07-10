package com.orgtgbot.service;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.bridge.AppContext;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import com.orgtgbot.service.services.bookkeeper.BookkeeperService;
import com.orgtgbot.service.services.reminder.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotFacade {

    private final GeneralService generalService;
    private final ProbegService probegService;
    private final DateService dateService;
    private final ReminderService reminderService;
    private final BookkeeperService bookkeeperService;

    private AppContext getContext(Long chatId) {
        return new AppContext(generalService, probegService, dateService, reminderService, bookkeeperService, chatId);
    }

    public String getAmount(GeneralFields field, Long chatId) {
        return field.getValue(getContext(chatId));
    }

    public void setAmount(GeneralFields field, String amount, Long chatId) {
        field.setValue(getContext(chatId), amount);
    }
}
