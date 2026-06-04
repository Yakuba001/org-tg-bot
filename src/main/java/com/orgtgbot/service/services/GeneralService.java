package com.orgtgbot.service.services;

import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.repository.GeneralEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralService {

    private final GeneralEntryRepository generalEntryRepository;

    @Transactional
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

    public List<GeneralEntry> getAll() {
        return generalEntryRepository.findAllByOrderByIdAsc();
    }

    public GeneralEntry getSingleEntry() {
        return getAll().getFirst();
    }
}
