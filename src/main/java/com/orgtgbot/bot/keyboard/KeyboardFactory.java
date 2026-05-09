package com.orgtgbot.bot.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public class KeyboardFactory {

    public static final String MAIN_MENU       = "main_menu";
    public static final String PROBEG_MENU     = "probeg_menu";
    public static final String GET_REPORT      = "get_report";

    public static final String PROBEG_MONDAY  = "probeg_monday";

    public static InlineKeyboardMarkup mainMenu() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                button(" Пробег", PROBEG_MENU)
                        )
                ))
                .build();
    }

    public static InlineKeyboardMarkup probegMenu() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                button(" Понедельник", PROBEG_MONDAY)
                        ),
                        new InlineKeyboardRow(
                                button(" Получить отчёт", GET_REPORT)
                        ),
                        new InlineKeyboardRow(
                                button("◀ Назад", MAIN_MENU)
                        )
                ))
                .build();
    }

    public static InlineKeyboardMarkup probegMonday() {
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        new InlineKeyboardRow(
                                button("◀ Назад", PROBEG_MENU)
                        )
                ))
                .build();
    }

    private static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }
}
