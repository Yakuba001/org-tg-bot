package com.orgtgbot.bot.command;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetReportCommand implements CommandHandler {

    private final ExcelService excelService;
    private final TelegramSender sender;

    @Override
    public String name() { return "get_report"; }

    @Override
    public String execute(Update update) {
        try {
            byte[] file = excelService.generateReport();
            sender.sendDocument(update.getMessage().getChatId(), file, "probeg.xlsx");
            return "";
        } catch (Exception e) {
            log.error("Ошибка генерации отчёта", e);
            return "❌ Ошибка генерации файла.";
        }
    }
}
