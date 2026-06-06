package com.orgtgbot.dto.user;

import lombok.Builder;

@Builder
public record UserDto(
        Long telegramChatId,
        String name,
        String role,
        String step
) {
}
