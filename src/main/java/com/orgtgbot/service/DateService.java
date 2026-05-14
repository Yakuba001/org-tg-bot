package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.repository.DateEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DateService {

    private final DateEntryRepository dateEntryRepository;

    @Transactional
    public void firstStart() {
        if (getAll() != null && getAll().size() < 5) {
            for (int i = 0; i < 5; i++) {
                dateEntryRepository.save(DatesEntry.builder()
                        .date(" ")
                        .build());
            }
        }
    }

    @Transactional
    public void setDate(UserState state, String date) {
        List<DatesEntry> dates = getAll();
        if (date.equals("-")) date = " ";
        switch (state) {
            case DATE_MONDAY -> dates.getFirst().setDate(date);
            case DATE_TUESDAY -> dates.get(1).setDate(date);
            case DATE_WEDNESDAY -> dates.get(2).setDate(date);
            case DATE_THURSDAY -> dates.get(3).setDate(date);
            case DATE_FRIDAY -> dates.get(4).setDate(date);
        }
    }

    @Transactional
    public String getDate(Buttons button) {
        List<DatesEntry> dates = getAll();
        switch (button) {
            case MONDAY_DATE -> {
                return dates.getFirst().getDate();
            }
            case TUESDAY_DATE -> {
                return dates.get(1).getDate();
            }
            case WEDNESDAY_DATE -> {
                return dates.get(2).getDate();
            }
            case THURSDAY_DATE -> {
                return dates.get(3).getDate();
            }
            case FRIDAY_DATE -> {
                return dates.get(4).getDate();
            }
            default -> {
                return " ";
            }
        }
    }

    public List<DatesEntry> getAll() {
        return dateEntryRepository.findAllByOrderByIdAsc();
    }
}
