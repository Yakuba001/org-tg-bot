package com.orgtgbot.bot.command;

import com.orgtgbot.service.ProbegService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MondayCommandTest {

    @Mock
    private ProbegService probegService;

    @InjectMocks
    private MondayCommand command;

    private Update buildUpdate(String text) {
        Message message = mock(Message.class);
        when(message.getText()).thenReturn(text);

        Update update = mock(Update.class);
        when(update.getMessage()).thenReturn(message);
        return update;
    }

    @Test
    void execute_returnsCorrectResults() {
        Update update = buildUpdate("/monday 12");

        String response = command.execute(update);

        assertThat(response).contains("Monday: 12 km.");
    }
}
