package com.orgtgbot.bot.callback;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetReportCallback implements CallbackHandler {

    private final ExcelService excelService;
    private final TelegramSender sender;

    @Override
    public String callbackData() {
        return Buttons.GET_REPORT.name();
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        try {
            byte[] file = excelService.generateReport();
            sender.sendDocument(chatId, file, "probeg.xlsx");
        } catch (Exception e) {
            log.error("Ошибка генерации отчёта через кнопку", e);
            sender.sendText(chatId, "❌ Ошибка генерации файла.", KeyboardFactory.mainMenu());
        }
    }
}
