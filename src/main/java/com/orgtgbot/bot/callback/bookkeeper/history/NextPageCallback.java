package com.orgtgbot.bot.callback.bookkeeper.history;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.BotFacade;
import org.springframework.stereotype.Component;

@Component
public class NextPageCallback extends AbstractHistoryRouter {

    public NextPageCallback(BotFacade botFacade, TelegramSender sender) {
        super(botFacade, sender);
    }

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.ALL_NEXT_PAGE;
    }

    @Override
    protected String getter(Long chatId, int page) {
        return botFacade.getAmountForPage(GeneralFields.ALL_NEXT_PAGE, chatId, page);
    }
}
