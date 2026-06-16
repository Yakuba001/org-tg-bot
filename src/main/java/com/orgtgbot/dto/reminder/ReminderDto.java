package com.orgtgbot.dto.reminder;

import java.time.LocalDateTime;

public record ReminderDto(
        String text,
        LocalDateTime targetTime
) {
}
