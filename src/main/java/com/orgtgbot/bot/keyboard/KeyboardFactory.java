package com.orgtgbot.bot.keyboard;

import com.orgtgbot.bot.callback.GeneralFields;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardFactory {

    public static InlineKeyboardMarkup buildMenuForGroup(GeneralFields currentMenu) {
        List<InlineKeyboardRow> rows = Arrays.stream(GeneralFields.values())
                .filter(field -> field.getGroup() == currentMenu)
                .map(field -> new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text(field.getDescription())
                                .callbackData(field.name())
                                .build()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        if (currentMenu.getParent() != null) {
            rows.add(new InlineKeyboardRow(InlineKeyboardButton.builder()
                    .text("Назад")
                    .callbackData(currentMenu.getParent().name())
                    .build()));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    public static InlineKeyboardMarkup dynamicBack(GeneralFields inputField) {
        GeneralFields backTarget = (inputField.getGroup() != null) ? inputField.getGroup() : GeneralFields.MAIN_MENU;
        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(new InlineKeyboardRow(
                        InlineKeyboardButton.builder()
                                .text("Назад")
                                .callbackData(backTarget.name())
                                .build()
                )))
                .build();
    }
}
