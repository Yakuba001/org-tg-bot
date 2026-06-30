package com.orgtgbot.bot.callback.general;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.callback.registry.core.routers.AbstractRouteCallbackHandler;
import com.orgtgbot.service.BotFacade;
import org.springframework.stereotype.Component;

@Component
public class NumberAutoCallback extends AbstractRouteCallbackHandler {

    public NumberAutoCallback(TelegramSender sender, BotFacade botFacade) {
        super(sender, botFacade);
    }

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.NUMBER_AUTO;
    }

    @Override
    protected String getRouteAmount(Long chatId) {
        return botFacade.getAmount(callbackData(), chatId);
    }

    @Override
    protected void setRouteAmount(Long chatId, String text) {
        botFacade.setAmount(callbackData(), text, chatId);
    }
}
