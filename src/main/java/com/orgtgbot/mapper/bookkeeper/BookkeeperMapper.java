package com.orgtgbot.mapper.bookkeeper;

import com.orgtgbot.dto.bookkeeper.ReceiptItemDto;
import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BookkeeperMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telegramChatId", source = "chatId")
    @Mapping(target = "itemName", source = "dto.item")
    @Mapping(target = "price", source = "dto.price")
    @Mapping(target = "purchaseDate", source = "date")
    @Mapping(target = "createdAt", ignore = true)
    BookkeeperRecord toEntity(ReceiptItemDto dto, Long chatId, LocalDate date);
}
