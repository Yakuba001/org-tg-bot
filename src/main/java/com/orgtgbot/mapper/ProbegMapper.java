package com.orgtgbot.mapper;

import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.entity.ReportEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProbegMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProbegUpdateDto dto, @MappingTarget ReportEntry entity);
}
