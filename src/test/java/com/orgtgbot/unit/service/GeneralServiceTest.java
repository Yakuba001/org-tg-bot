package com.orgtgbot.unit.service;

import com.orgtgbot.dto.GeneralEntryDto;
import com.orgtgbot.entity.GeneralEntry;
import com.orgtgbot.entity.user.UserWorkspace;
import com.orgtgbot.mapper.GeneralMapper;
import com.orgtgbot.repository.UserWorkspaceRepository;
import com.orgtgbot.service.services.GeneralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeneralServiceTest {

    @Mock
    private UserWorkspaceRepository userWorkspaceRepository;

    @Spy
    private GeneralMapper generalMapper = Mappers.getMapper(GeneralMapper.class);

    @InjectMocks
    private GeneralService generalService;

    private UserWorkspace example;

    @BeforeEach
    void setUp() {
        example = UserWorkspace.builder()
                .generalEntry(GeneralEntry.builder().name("Boss").build())
                .build();
    }

    @Test
    void getSingleEntry_returnCorrectResult() {
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        GeneralEntryDto result = generalService.getSingleDto(anyLong());

        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
        assertThat(result.name()).isEqualTo(example.getGeneralEntry().getName());
    }

    @Test
    void updateGeneralInfo() {
        GeneralEntryDto dto = GeneralEntryDto.builder().name("Worker").build();
        when(userWorkspaceRepository.findByUser_TelegramChatId(anyLong())).thenReturn(Optional.of(example));

        generalService.updateGeneralInfo(anyLong(), dto);

        verify(generalMapper, times(1)).updateEntityFromDto(dto, example.getGeneralEntry());
        verify(userWorkspaceRepository, times(1)).findByUser_TelegramChatId(anyLong());
        assertThat(example.getGeneralEntry().getName()).isEqualTo(dto.name());
    }
}
