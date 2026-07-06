package com.orgtgbot.dto.bookkeeper;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ReceiptItemDto(
        String item,
        BigDecimal price
) {
}
