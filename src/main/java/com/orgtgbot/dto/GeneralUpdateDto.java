package com.orgtgbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeneralUpdateDto {

    private String name;
    private String date;
    private String carModel;
    private String carNumber;
    private Integer startWeekProbeg;
    private Integer endWeekProbeg;
    private String startBalanceLitres;
    private String endBalanceLitres;
    private Integer totalWeekKm;
    private String fuelNorm;
    private String litresSpend;
    private Integer fueling;
}
