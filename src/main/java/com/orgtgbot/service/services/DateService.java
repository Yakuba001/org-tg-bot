package com.orgtgbot.service.services;

import com.orgtgbot.dto.DatesUpdateDto;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.mapper.DateMapper;
import com.orgtgbot.repository.DateEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DateService {

    private final DateEntryRepository dateEntryRepository;
    private final DateMapper dateMapper;

    public void firstStart() {
        if (getAll().isEmpty()) {
            List<DatesEntry> result = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                result.add(DatesEntry.builder()
                        .date(" ")
                        .build());
            }
            dateEntryRepository.saveAll(result);
        }
    }

    public void setDate(int dayNumber, DatesUpdateDto dto) {
        DatesEntry date = getDatesEntry(dayNumber);
        dateMapper.updateEntityFromDto(dto, date);
    }

    @Transactional(readOnly = true)
    public List<DatesEntry> getAll() {
        return dateEntryRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public DatesEntry getDatesEntry(int dayNumber) {
        return dateEntryRepository.findById((long) dayNumber)
                .orElseThrow(() -> new IllegalStateException("Day not found: " + dayNumber));
    }
}
