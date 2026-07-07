package com.orgtgbot.mapper.reminder;

import com.orgtgbot.dto.reminder.ReminderDto;
import com.orgtgbot.entity.reminder.ReminderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReminderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "telegramChatId", source = "chatId")
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "targetTime", source = "dto.targetTime")
    @Mapping(target = "isSent", ignore = true)
    ReminderEntity toEntity(ReminderDto dto, Long chatId);
}
