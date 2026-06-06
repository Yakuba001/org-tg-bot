package com.orgtgbot.dto;

import lombok.Builder;

@Builder
public record GeneralUpdateDto(
        String name,
        String date,
        String carModel,
        String carNumber,
        Integer startWeekProbeg,
        Integer endWeekProbeg,
        String startBalanceLitres,
        String endBalanceLitres,
        Integer totalWeekKm,
        String fuelNorm,
        String litresSpend,
        Integer fueling
) {
}
