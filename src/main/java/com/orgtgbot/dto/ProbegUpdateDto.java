package com.orgtgbot.dto;

import lombok.Builder;

@Builder
public record ProbegUpdateDto(
        Integer dayNumber,
        String route,
        Integer morningKm,
        Integer eveningKm,
        Integer totalKm
) {}
