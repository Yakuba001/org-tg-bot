package com.orgtgbot.mapper;

import com.orgtgbot.dto.GeneralUpdateDto;
import com.orgtgbot.entity.GeneralEntry;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GeneralMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startBalanceLitres", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "endBalanceLitres", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "fuelNorm", qualifiedByName = "stringToBigDecimal")
    @Mapping(target = "litresSpend", qualifiedByName = "stringToBigDecimal")
    void updateEntityFromDto(GeneralUpdateDto dto, @MappingTarget GeneralEntry entity);

    @SuppressWarnings("unused")
    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value == null || value.isBlank() ? null : new BigDecimal(value.replace(",", "."));
    }
}
