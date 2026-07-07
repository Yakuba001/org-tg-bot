package com.orgtgbot.dto;

import lombok.Builder;

@Builder
public record ReportEntryDto(
        Integer dayNumber,
        String route,
        Integer morningKm,
        Integer eveningKm,
        Integer totalKm
) {}
