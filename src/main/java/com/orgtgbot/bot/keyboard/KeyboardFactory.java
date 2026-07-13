package com.orgtgbot.bot.keyboard;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardFactory {

    public static InlineKeyboardMarkup buildMenuForGroup(GeneralFields currentMenu) {
        return buildMenuForGroupPaged(currentMenu, 0);
    }

    public static InlineKeyboardMarkup buildMenuForGroupPaged(GeneralFields currentMenu, int currentPage) {
        List<InlineKeyboardRow> rows = Arrays.stream(GeneralFields.values())
                .filter(field -> field.getGroup() == currentMenu)
                .map(field -> {
                    String callbackValue = field.name();
                    if (field == GeneralFields.ALL_NEXT_PAGE) {
                        callbackValue = field.name() + ":" + (currentPage + 1);
                    } else if (field == GeneralFields.ALL_PREVIOUS_PAGE) {
                        callbackValue = field.name() + ":" + (currentPage - 1);
                    } else if (field == GeneralFields.ALL_RECEIPTS) {
                        callbackValue = field.name() + ":0";
                    }
                    return new InlineKeyboardRow(
                            InlineKeyboardButton.builder()
                                    .text(field.getDescription())
                                    .callbackData(callbackValue)
                                    .build()
                    );
                })
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
