package com.orgtgbot.bot.callback.bookkeeper.history;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.BotFacade;
import org.springframework.stereotype.Component;

@Component
public class HistoryCallback extends AbstractHistoryRouter {

    public HistoryCallback(BotFacade botFacade, TelegramSender sender) {
        super(botFacade, sender);
    }

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.ALL_RECEIPTS;
    }

    @Override
    protected String getter(Long chatId, int page) {
        return botFacade.getAmountForPage(GeneralFields.ALL_RECEIPTS, chatId, page);
    }
}
