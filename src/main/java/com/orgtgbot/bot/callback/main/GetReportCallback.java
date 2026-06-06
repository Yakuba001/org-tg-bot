package com.orgtgbot.bot.callback.main;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.CallbackHandler;
import com.orgtgbot.bot.callback.GeneralFields;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.services.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GetReportCallback implements CallbackHandler {

    private final ExcelService excelService;
    private final TelegramSender sender;

    @Override
    public GeneralFields callbackData() {
        return GeneralFields.GET_REPORT;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        Long chatId = callbackQuery.getMessage().getChatId();
        byte[] file = excelService.generateReport(chatId);
        sender.sendDocument(chatId, file, "probeg.xlsx");
    }

    @Override
    public void handle(Long chatId, String text, Integer botMenuId, TelegramSender sender) {
        sender.editMarkup(chatId, botMenuId, "Главное меню",
                KeyboardFactory.buildMenuForGroup(callbackData()));
    }
}
