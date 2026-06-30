package com.orgtgbot.bot.callback.registry.core.routers;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.interactions.TextAnswerableHandler;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.BotFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class AbstractRouteCallbackHandler implements ClickableHandler, TextAnswerableHandler {

    protected final TelegramSender sender;
    protected final BotFacade botFacade;

    protected AbstractRouteCallbackHandler(TelegramSender sender, BotFacade botFacade) {
        this.sender = sender;
        this.botFacade = botFacade;
    }

    protected abstract String getRouteAmount(Long chatId);
    protected abstract void setRouteAmount(Long chatId, String text);

    @Override
    public final void handle(CallbackQuery callbackQuery) throws Exception {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String amount = getRouteAmount(chatId);

        sender.editMarkup(
                chatId,
                messageId,
                callbackData().getDescription() + ": " + amount,
                KeyboardFactory.dynamicBack(callbackData())
        );
    }

    @Override
    public final void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        setRouteAmount(chatId, text.trim());
        sender.editMarkup(
                chatId,
                botMenuId,
                "Данные приняты!\n",
                KeyboardFactory.buildMenuForGroup(callbackData()));
    }
}
