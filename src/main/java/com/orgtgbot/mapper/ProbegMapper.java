package com.orgtgbot.mapper;

import com.orgtgbot.dto.ReportEntryDto;
import com.orgtgbot.entity.ReportEntry;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProbegMapper {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ReportEntryDto dto, @MappingTarget ReportEntry entity);

    @Mapping(target = "id", ignore = true)
    List<ReportEntryDto> toDtoList(List<ReportEntry> entities);

    ReportEntryDto toDto(ReportEntry entity);
}
