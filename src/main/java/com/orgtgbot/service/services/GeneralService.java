package com.orgtgbot.service.services;

import com.orgtgbot.dto.GeneralUpdateDto;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.mapper.GeneralMapper;
import com.orgtgbot.repository.GeneralEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneralService {

    private final GeneralEntryRepository generalEntryRepository;
    private final GeneralMapper generalMapper;

    public void firstStart() {
        if (getAll().isEmpty()) {
            generalEntryRepository.save(GeneralEntry.builder()
                    .name(" ")
                    .date(" ")
                    .carModel(" ")
                    .carNumber(" ")
                    .startWeekProbeg(0)
                    .endWeekProbeg(0)
                    .startBalanceLitres(new BigDecimal("0.0"))
                    .endBalanceLitres(new BigDecimal("0.0"))
                    .totalWeekKm(0)
                    .fuelNorm(new BigDecimal("0.0"))
                    .litresSpend(new BigDecimal("0.0"))
                    .fueling(0)
                    .build());
        }
    }

    public void updateGeneralInfo(GeneralUpdateDto updateDto) {
        GeneralEntry entry = getSingleEntry();
        generalMapper.updateEntityFromDto(updateDto, entry);
    }

    @Transactional(readOnly = true)
    public List<GeneralEntry> getAll() {
        return generalEntryRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public GeneralEntry getSingleEntry() {
        return getAll().getFirst();
    }
}
