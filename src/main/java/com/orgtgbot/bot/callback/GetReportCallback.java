package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserStateService;
import com.orgtgbot.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GetReportCallback implements CallbackHandler {

    private final ExcelService excelService;
    private final TelegramSender sender;
    private final UserStateService userStateService;

    @Override
    public String callbackData() {
        return Buttons.GET_REPORT.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) throws Exception {
        userStateService.removeState(callbackQuery.getMessage().getChatId());

        Long chatId = callbackQuery.getMessage().getChatId();
        byte[] file = excelService.generateReport();
        sender.sendDocument(chatId, file, "probeg.xlsx");
    }
}
