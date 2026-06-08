package com.orgtgbot.service;

import com.orgtgbot.dto.ProbegUpdateDto;
import com.orgtgbot.entity.ReportEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.mapper.ProbegMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.ProbegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProbegServiceTest {

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Spy
    private ProbegMapper probegMapper = Mappers.getMapper(ProbegMapper.class);

    @InjectMocks
    private ProbegService probegService;

    private UserWorkspace example;

    @BeforeEach
    void setUp() {
        example = UserWorkspace.builder()
                .reportEntries(List.of(
                                ReportEntry.builder()
                                        .dayNumber(1)
                                        .morningKm(1)
                                        .eveningKm(2)
                                        .totalKm(1)
                                        .route("One")
                                        .build(),
                                ReportEntry.builder()
                                        .dayNumber(2)
                                        .morningKm(2)
                                        .eveningKm(3)
                                        .totalKm(1)
                                        .route("Two")
                                        .build(),
                                ReportEntry.builder()
                                        .dayNumber(3)
                                        .morningKm(2)
                                        .eveningKm(3)
                                        .totalKm(1)
                                        .route("Three")
                                        .build(),
                                ReportEntry.builder()
                                        .dayNumber(4)
                                        .morningKm(4)
                                        .eveningKm(5)
                                        .totalKm(1)
                                        .route("Four")
                                        .build(),
                                ReportEntry.builder()
                                        .dayNumber(5)
                                        .morningKm(5)
                                        .eveningKm(6)
                                        .totalKm(1)
                                        .route("Five")
                                        .build()
                        )
                )
                .build();
    }

    @Test
    void getAll_returnCorrectResult() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        List<ReportEntry> result = probegService.getAll(anyLong());

        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
        assertThat(result).hasSize(5);
    }

    @Test
    void getReportEntry_returnCorrectResult() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        ReportEntry result = probegService.getReportEntry(anyLong(), 1);

        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
        assertThat(result).isEqualTo(example.getReportEntries().getFirst());
    }

    @Test
    void clearAll_returnCorrectEmptyFields() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        probegService.clearAll(anyLong());

        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
        assertThat(example.getReportEntries().stream().allMatch(e -> e.getTotalKm() == 0 &&
                e.getMorningKm() == 0 && e.getEveningKm() == 0 && e.getRoute().isBlank())).isTrue();
    }

    @Test
    void updateProbegInfo_returnCorrectTotal() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));
        ProbegUpdateDto dto = ProbegUpdateDto.builder().morningKm(10).eveningKm(20).route("DTO").build();

        probegService.updateProbegInfo(anyLong(), 1, dto);

        verify(probegMapper, times(1))
                .updateEntityFromDto(dto, example.getReportEntries().getFirst());
        assertThat(example.getReportEntries().getFirst().getTotalKm()).isEqualTo(10);
    }
}
