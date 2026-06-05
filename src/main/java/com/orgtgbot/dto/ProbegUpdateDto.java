package com.orgtgbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProbegUpdateDto {

    private Integer dayNumber;
    private String route;
    private Integer morningKm;
    private Integer eveningKm;
    private Integer totalKm;
}
