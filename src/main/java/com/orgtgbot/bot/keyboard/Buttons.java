package com.orgtgbot.bot.keyboard;

import lombok.Getter;

@Getter
public enum Buttons {
    MAIN_MENU("main_menu"),
    PROBEG_MENU("probeg_menu"),
    PROBEG_MONDAY("probeg_monday"),
    GET_REPORT("get_report");

    private final String name;

    Buttons(String name) {
        this.name = name;
    }
}
