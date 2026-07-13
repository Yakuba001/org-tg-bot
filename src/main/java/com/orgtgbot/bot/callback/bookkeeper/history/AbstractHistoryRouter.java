package com.orgtgbot.bot.callback.bookkeeper.history;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.BotFacade;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public abstract class AbstractHistoryRouter implements ClickableHandler {

    protected final BotFacade botFacade;
    protected final TelegramSender sender;

    public AbstractHistoryRouter(BotFacade botFacade, TelegramSender sender) {
        this.botFacade = botFacade;
        this.sender = sender;
    }

    protected abstract String getter(Long chatId, int page);

    @Override
    public final void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int page = extractPage(callbackQuery.getData());
        String historyText = getter(chatId, page);
        Integer messageId = callbackQuery.getMessage().getMessageId();
        sender.editMarkup(
                chatId,
                messageId,
                historyText,
                KeyboardFactory.buildMenuForGroupPaged(GeneralFields.ALL_RECEIPTS, page)
        );
    }

    private int extractPage(String rawData) {
        return (rawData != null && rawData.contains(":")) ? Integer.parseInt(rawData.split(":")[1]) : 0;
    }
}
