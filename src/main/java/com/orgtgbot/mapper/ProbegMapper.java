package com.orgtgbot.mapper;

import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.entity.ReportEntry;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProbegMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProbegUpdateDto dto, @MappingTarget ReportEntry entity);
}
