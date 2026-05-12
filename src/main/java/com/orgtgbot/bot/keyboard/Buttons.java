package com.orgtgbot.bot.keyboard;

import lombok.Getter;

@Getter
public enum Buttons {
    MAIN_MENU("Главное меню"),
    PROBEG_MENU("Меню пробега"),
    PROBEG_MONDAY("Понедельник"),
    GET_REPORT("Получить отчёт"),
    SET_MORNING_KM("Утро"),
    SET_EVENING_KM("Вечер"),
    BACK("Назад");

    private final String name;

    Buttons(String name) {
        this.name = name;
    }
}
