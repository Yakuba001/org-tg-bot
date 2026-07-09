package com.orgtgbot.unit.exception;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.exception.GlobalExceptionHandler;
import com.orgtgbot.exception.exceptions.service.ExcelGeneratorException;
import com.orgtgbot.exception.exceptions.service.gemini.GeminiParseTextException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock private TelegramSender telegramSender;
    private GlobalExceptionHandler exceptionHandler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 999;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler(telegramSender);
    }

    @Test
    void shouldFindExactMessage_WhenExceptionIsRegistered() {
        Exception exception = new GeminiParseTextException(
                "Текст не распарсился",
                new RuntimeException("Ошибка сети или кривой JSON")
        );

        exceptionHandler.handle(exception, CHAT_ID, MESSAGE_ID);

        verify(telegramSender).editMarkup(
                eq(CHAT_ID),
                eq(MESSAGE_ID),
                eq("Fail, try create remind again."),
                any()
        );
    }

    @Test
    void shouldFindParentMessage_WhenSubclassExceptionIsThrown() {
        class SpecificExcelException extends ExcelGeneratorException {
            public SpecificExcelException(String msg) { super(CHAT_ID, msg); }
        }
        Exception subException = new SpecificExcelException("Сломалась конкретная ячейка");

        exceptionHandler.handle(subException, CHAT_ID, MESSAGE_ID);

        verify(telegramSender).editMarkup(
                eq(CHAT_ID),
                eq(MESSAGE_ID),
                eq("Fail, create excel file."),
                any()
        );
    }

    @Test
    void shouldReturnDefaultMessage_WhenExceptionIsNotRegistered() {
        Exception unknownException = new NullPointerException("NPE на ровном месте");

        exceptionHandler.handle(unknownException, CHAT_ID, MESSAGE_ID);

        verify(telegramSender).editMarkup(
                eq(CHAT_ID),
                eq(MESSAGE_ID),
                eq("Something went wrong. Please try again later."),
                any()
        );
    }
}
