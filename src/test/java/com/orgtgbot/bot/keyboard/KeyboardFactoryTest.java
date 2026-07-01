package com.orgtgbot.bot.keyboard;

import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyboardFactoryTest {

    private static final GeneralFields INPUT_FIELD = GeneralFields.SET_MORNING_MONDAY_KM;
    private static final GeneralFields GROUP_FIELD = GeneralFields.PROBEG_MONDAY;
    private static final GeneralFields MAIN_MENU_FIELD = GeneralFields.MAIN_MENU;

    @Test
    void buildMenuForGroup_WhenParentIsNotNull_ShouldBuildMenuWithBackButton() {
        InlineKeyboardMarkup result = KeyboardFactory.buildMenuForGroup(GROUP_FIELD);
        List<InlineKeyboardRow> rows = result.getKeyboard();

        assertThat(rows).isNotEmpty();

        boolean hasBackButton = rows.stream()
                .flatMap(InlineKeyboardRow::stream)
                .anyMatch(button -> "Назад".equals(button.getText())
                        && GROUP_FIELD.getParent().name().equals(button.getCallbackData()));

        assertThat(hasBackButton).isTrue();

        boolean hasGroupButton = rows.stream()
                .flatMap(InlineKeyboardRow::stream)
                .anyMatch(button -> INPUT_FIELD.getDescription().equals(button.getText())
                        && INPUT_FIELD.name().equals(button.getCallbackData()));

        assertThat(hasGroupButton).isTrue();
    }

    @Test
    void buildMenuForGroup_WhenParentIsNull_ShouldNotAddBackButton() {
        InlineKeyboardMarkup result = KeyboardFactory.buildMenuForGroup(MAIN_MENU_FIELD);
        List<InlineKeyboardRow> rows = result.getKeyboard();

        assertThat(rows).isNotEmpty();

        boolean hasBackButton = rows.stream()
                .flatMap(InlineKeyboardRow::stream)
                .anyMatch(button -> "Назад".equals(button.getText()));

        assertThat(hasBackButton).isFalse();
    }

    @Test
    void dynamicBack_ShouldCreateSingleBackButtonWithCorrectRoute() {
        InlineKeyboardMarkup result = KeyboardFactory.dynamicBack(INPUT_FIELD);
        List<InlineKeyboardRow> rows = result.getKeyboard();

        assertThat(rows).hasSize(1);

        InlineKeyboardButton resultButton = rows.getFirst().getFirst();

        assertThat(resultButton.getText()).isEqualTo("Назад");
        assertThat(resultButton.getCallbackData()).isEqualTo(GROUP_FIELD.name());
    }
}
