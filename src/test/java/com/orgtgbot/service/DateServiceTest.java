package com.orgtgbot.service;

import com.orgtgbot.bot.keyboard.Buttons;
import com.orgtgbot.bot.state.UserState;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.repository.DateEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DateServiceTest {

    @Mock
    private DateEntryRepository dateEntryRepository;

    @InjectMocks
    private DateService dateService;

    List<DatesEntry> result;

    @BeforeEach
    void setUp() {
        result = new ArrayList<>(List.of(
                DatesEntry.builder().id(0L).date("01.01.2026").build(),
                DatesEntry.builder().id(1L).date("02.01.2026").build(),
                DatesEntry.builder().id(2L).date("03.01.2026").build(),
                DatesEntry.builder().id(3L).date("04.01.2026").build(),
                DatesEntry.builder().id(4L).date("05.01.2026").build()
        ));
    }

    @Test
    void firstStart_returnsCorrectResult() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(List.of());
        when(dateEntryRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        dateService.firstStart();

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        verify(dateEntryRepository, times(1)).saveAll(argThat(collection -> {
            List<DatesEntry> savedList = (List<DatesEntry>) collection;
            if (savedList.size() != 5) return false;
            for (int i = 0; i < 5; i++) {
                if (!savedList.get(i).getDate().equals(" ")) return false;
            }
            return true;
        }));
    }

    @Test
    void firstStart_returnsNothingWithNonEmptyCollection() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(result);

        dateService.firstStart();

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        verify(dateEntryRepository, never()).saveAll(anyList());
    }

    @Test
    void setDate_returnsCorrectResult() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(result);

        dateService.setDate(UserState.DATE_FRIDAY, "01.01.2030");

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        assertThat(result.getLast().getDate()).isEqualTo("01.01.2030");
    }

    @Test
    void setDate_handleMinusSign() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(result);

        dateService.setDate(UserState.DATE_FRIDAY, "-");

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        assertThat(result.getLast().getDate()).isEqualTo(" ");
    }

    @Test
    void getDate_returnsCorrectResult() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(result);

        String res = dateService.getDate(Buttons.FRIDAY_DATE);

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        assertThat(res).isEqualTo(result.getLast().getDate());
    }

    @Test
    void getAll_returnsCorrectResult() {
        when(dateEntryRepository.findAllByOrderByIdAsc()).thenReturn(result);

        List<DatesEntry> res = dateService.getAll();

        verify(dateEntryRepository, times(1)).findAllByOrderByIdAsc();
        assertThat(res).isEqualTo(result);
    }
}
