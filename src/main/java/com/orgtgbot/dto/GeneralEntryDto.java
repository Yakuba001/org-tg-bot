package com.orgtgbot.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record GeneralEntryDto(
        String name,
        String date,
        String carModel,
        String carNumber,
        Integer startWeekProbeg,
        Integer endWeekProbeg,
        BigDecimal startBalanceLitres,
        BigDecimal endBalanceLitres,
        Integer totalWeekKm,
        BigDecimal fuelNorm,
        BigDecimal litresSpend,
        Integer fueling
) {
}
