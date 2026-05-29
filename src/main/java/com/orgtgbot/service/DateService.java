package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.repository.DateEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DateService {

    private final DateEntryRepository dateEntryRepository;

    @Transactional
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

    @Transactional
    public void setDate(UserState state, String date) {
        if (date.equals("-")) date = " ";
        getDatesEntry(state).setDate(date);
    }

    @Transactional(readOnly = true)
    public String getDate(Buttons button) {
        return getDatesEntry(button).getDate();
    }

    public List<DatesEntry> getAll() {
        return dateEntryRepository.findAllByOrderByIdAsc();
    }

    private DatesEntry getDatesEntry(UserState state) {
        return getDatesEntry(state.getDayNumber().orElseThrow(() -> new IllegalStateException("Unexpected value")));
    }

    private DatesEntry getDatesEntry(Buttons button) {
        return getDatesEntry(button.getDayNumber().orElseThrow(() -> new IllegalStateException("Unexpected value")));
    }

    private DatesEntry getDatesEntry(int dayNumber) {
        List<DatesEntry> dates = getAll();
        return dates.stream()
                .filter(e -> e.getId() == dayNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("DatesEntry not found for dayNumber: " + dayNumber));
    }
}
