package com.orgtgbot.mapper;

import com.orgtgbot.dto.DatesUpdateDto;
import com.orgtgbot.entity.DatesEntry;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DateMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DatesUpdateDto dto, @MappingTarget DatesEntry entity);
}
