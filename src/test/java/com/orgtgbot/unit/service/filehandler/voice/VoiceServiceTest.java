package com.orgtgbot.unit.service.filehandler.voice;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.config.TelegramBotProperties;
import com.orgtgbot.exception.exceptions.service.voice.FailedHandleVoiceException;
import com.orgtgbot.service.filehandler.voice.VoiceService;
import com.orgtgbot.service.services.reminder.ReminderService;
import com.orgtgbot.service.filehandler.TelegramFileDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoiceServiceTest {

    @Mock
    private TelegramClient telegramClient;

    @Mock
    private TelegramBotProperties telegramBotProperties;

    @Mock
    private ReminderService reminderService;

    @Mock
    private TelegramSender sender;

    @Mock
    private TelegramFileDownloader fileDownloader;

    @InjectMocks
    private VoiceService voiceService;

    @Test
    void handleVoiceAsync_success() throws Exception {
        Long chatId = 12345L;
        byte[] fakeAudioBytes = new byte[]{1, 2, 3};

        when(telegramBotProperties.token()).thenReturn("fake-token");

        Voice voice = mock(Voice.class);
        when(voice.getFileId()).thenReturn("voice_id");

        File telegramFile = mock(File.class);
        when(telegramFile.getFilePath()).thenReturn("path/to/file.ogg");
        when(telegramClient.execute(any(GetFile.class))).thenReturn(telegramFile);
        when(fileDownloader.downloadAsBytes(anyString())).thenReturn(fakeAudioBytes);

        voiceService.handleVoiceAsync(chatId, voice, 1, GeneralFields.MAIN_REMINDER);

        verify(reminderService, times(1)).addVoiceRemind(chatId, fakeAudioBytes);
        verify(sender, times(1)).editMarkup(eq(chatId), anyInt(), anyString(), any());
    }

    @Test
    void handleVoiceAsync_exception_shouldThrowFailedHandleVoiceException() throws Exception {
        Long chatId = 12345L;
        Voice voice = mock(Voice.class);
        when(voice.getFileId()).thenReturn("broken_id");

        when(telegramClient.execute(any(GetFile.class))).thenThrow(new RuntimeException("Telegram API Error"));

        FailedHandleVoiceException result = assertThrows(FailedHandleVoiceException.class,
                () -> voiceService.handleVoiceAsync(chatId, voice, 999, GeneralFields.MAIN_REMINDER));

        assertThat(result.getMessage()).contains("Failed handle voice");
        verifyNoInteractions(reminderService);
    }
}
