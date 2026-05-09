package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.Arrays;

import static com.orgtgbot.bot.keyboard.Buttons.*;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        return keyboard(row(button(" Пробег", PROBEG_MENU.name())));
    }

    public static InlineKeyboardMarkup probegMenu() {
        return keyboard(row(
                button(" Понедельник", PROBEG_MONDAY.name()),
                button(" Получить отчёт", GET_REPORT.name()),
                button(" Назад", MAIN_MENU.name())
        ));
    }

    public static InlineKeyboardMarkup probegMonday() {
        return keyboard(row(button(" Назад", PROBEG_MENU.name())));
    }

    private static InlineKeyboardMarkup keyboard(InlineKeyboardRow... rows) {
        return InlineKeyboardMarkup.builder()
                .keyboard(Arrays.stream(rows).toList())
                .build();
    }

    private static InlineKeyboardRow row(InlineKeyboardButton... buttons) {
        return new InlineKeyboardRow(buttons);
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
