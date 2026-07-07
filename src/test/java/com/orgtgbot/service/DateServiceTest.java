package com.orgtgbot.service;

import com.orgtgbot.dto.DatesEntryDto;
import com.orgtgbot.entity.DatesEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.exception.exceptions.service.DateIndexOutOfBoundException;
import com.orgtgbot.mapper.DateMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.DateService;
import org.junit.jupiter.api.Assertions;
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
public class DateServiceTest {

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Spy
    private DateMapper dateMapper = Mappers.getMapper(DateMapper.class);

    @InjectMocks
    private DateService dateService;

    private UserWorkspace example;

    @BeforeEach
    void setUp() {
        example = UserWorkspace.builder()
                .datesEntries(List.of(
                        DatesEntry.builder().date("01.01.01").build(),
                        DatesEntry.builder().date("02.02.02").build(),
                        DatesEntry.builder().date("03.03.03").build(),
                        DatesEntry.builder().date("04.04.04").build(),
                        DatesEntry.builder().date("05.05.05").build()
                ))
                .build();
    }

    @Test
    void getAll_returnListWithFiveElements() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        List<DatesEntryDto> result = dateService.getAllDto(anyLong());

        assertThat(result).hasSize(5);
        assertThat(result)
                .extracting(DatesEntryDto::date)
                .containsExactly("01.01.01", "02.02.02", "03.03.03", "04.04.04", "05.05.05");
        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
    }

    @Test
    void getDate_returnCorrectDate_ifIndexIsInRange() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        DatesEntryDto result = dateService.getDatesDto(anyLong(), 2);

        assertThat(result.date()).isEqualTo(example.getDatesEntries().get(1).getDate());
        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
    }

    @Test
    void getDate_throwDateIndexOutOfBoundException_ifIndexIsOutOfRange() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        Assertions.assertThrows(DateIndexOutOfBoundException.class,
                () -> dateService.getDatesDto(anyLong(), 6));

        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
    }

    @Test
    void setDate_updateDate_andCorrectUseMapper() {
        DatesEntryDto dto = DatesEntryDto.builder().date("07.07.07").build();
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        dateService.setDate(anyLong(), 1, dto);

        assertThat(example.getDatesEntries().getFirst().getDate()).isEqualTo(dto.date());
        verify(dateMapper, times(1))
                .updateEntityFromDto(dto, example.getDatesEntries().getFirst());
    }
}
