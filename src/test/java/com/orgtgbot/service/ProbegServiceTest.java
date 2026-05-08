package com.orgtgbot.service;

import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.repository.ReportEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProbegServiceTest {

    @Mock
    private ReportEntryRepository repository;

    @InjectMocks
    private ProbegService service;

    @Test
    void changeMonday_returnsCorrectResults() {
        List<ReportEntry> kilometers = List.of(ReportEntry.builder().kilometers(228).build());
        when(repository.findAllByOrderByRowNumberAsc()).thenReturn(kilometers);

        service.changeMonday(Collections.singletonList(kilometers.getFirst().getKilometers()));
        List<ReportEntry> result = service.getAll();

        assertThat(kilometers.getFirst().getKilometers()).isEqualTo(result.getFirst().getKilometers());
    }
}
