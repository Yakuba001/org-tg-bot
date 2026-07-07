package com.orgtgbot.mapper;

import com.orgtgbot.dto.DatesEntryDto;
import com.orgtgbot.entity.DatesEntry;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DateMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DatesEntryDto dto, @MappingTarget DatesEntry entity);

    DatesEntryDto toDto(DatesEntry entity);

    @Mapping(target = "id", ignore = true)
    List<DatesEntryDto> toDtoList(List<DatesEntry> entities);
}
