package com.orgtgbot.mapper;

import com.orgtgbot.dto.DatesUpdateDto;
import com.orgtgbot.entity.DatesEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DateMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DatesUpdateDto dto, @MappingTarget DatesEntry entity);
}
