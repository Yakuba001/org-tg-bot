package com.orgtgbot.bot.keyboard;

import lombok.Getter;

@Getter
public enum Buttons {
    MAIN_MENU("Главное меню"),
    PROBEG_MENU("Меню пробега"),
    PROBEG_MONDAY("Понедельник"),
    GET_REPORT("Получить отчёт");

    private final String name;

    Buttons(String name) {
        this.name = name;
    }
}
