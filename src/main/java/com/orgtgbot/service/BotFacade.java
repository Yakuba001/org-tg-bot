package com.orgtgbot.service;

import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.service.bridge.AppContext;
import com.orgtgbot.service.services.DateService;
import com.orgtgbot.service.services.GeneralService;
import com.orgtgbot.service.services.ProbegService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BotFacade {

    private final GeneralService generalService;
    private final ProbegService probegService;
    private final DateService dateService;

    private AppContext getContext() {
        return new AppContext(generalService, probegService, dateService);
    }

    @Transactional(readOnly = true)
    public String getAmount(GeneralFields field) {
        return field.getValue(getContext());
    }

    @Transactional
    public void setAmount(GeneralFields field, String amount) {
        field.setValue(getContext(), amount);
    }
}
