package com.orgtgbot.bot.state;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum UserState {
    NONE(null),

    PROBEG_MORNING_MONDAY(1),
    PROBEG_EVENING_MONDAY(1),
    PROBEG_TOTAL_MONDAY(1),
    ROUTE_MONDAY(1),
    PROBEG_MORNING_TUESDAY(2),
    PROBEG_EVENING_TUESDAY(2),
    PROBEG_TOTAL_TUESDAY(2),
    ROUTE_TUESDAY(2),
    PROBEG_MORNING_WEDNESDAY(3),
    PROBEG_EVENING_WEDNESDAY(3),
    PROBEG_TOTAL_WEDNESDAY(3),
    ROUTE_WEDNESDAY(3),
    PROBEG_MORNING_THURSDAY(4),
    PROBEG_EVENING_THURSDAY(4),
    PROBEG_TOTAL_THURSDAY(4),
    ROUTE_THURSDAY(4),
    PROBEG_MORNING_FRIDAY(5),
    PROBEG_EVENING_FRIDAY(5),
    PROBEG_TOTAL_FRIDAY(5),
    ROUTE_FRIDAY(5),
    DATE_MONDAY(0),
    DATE_TUESDAY(1),
    DATE_WEDNESDAY(2),
    DATE_THURSDAY(3),
    DATE_FRIDAY(4),
    DRIVER(null),
    DATE(null),
    MODEL_AUTO(null),
    NUMBER_AUTO(null),
    START_WEEK_PROBEG(null),
    END_WEEK_PROBEG(null),
    START_BALANCE_LITRES(null),
    END_BALANCE_LITRES(null),
    TOTAL_WEEK_KM(null),
    FUEL_NORM(null),
    LITRES_SPEND(null),
    FUELING(null);

    private final Integer dayNumber;

    UserState(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Optional<Integer> getDayNumber() {
        return Optional.ofNullable(dayNumber);
    }
}
