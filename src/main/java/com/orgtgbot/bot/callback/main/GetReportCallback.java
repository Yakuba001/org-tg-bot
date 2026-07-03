package com.orgtgbot.bot.callback.main;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.interactions.ClickableHandler;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.service.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GetReportCallback implements ClickableHandler {

    private final ExcelService excelService;
    private final TelegramSender sender;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.GET_REPORT;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        byte[] file = excelService.generateReport(chatId);
        sender.sendDocument(chatId, file, "probeg.xlsx");
    }
}
