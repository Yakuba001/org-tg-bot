package com.orgtgbot.unit.bot.callback.registry.core.routers;

import com.orgtgbot.bot.TelegramSender;
import com.orgtgbot.bot.callback.registry.core.main.GeneralFields;
import com.orgtgbot.bot.callback.registry.core.routers.AbstractRouteCallbackHandler;
import com.orgtgbot.bot.keyboard.KeyboardFactory;
import com.orgtgbot.service.BotFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbstractRouteCallbackHandlerTest {

    @Mock private TelegramSender sender;
    @Mock private BotFacade botFacade;
    @Mock private CallbackQuery callbackQuery;
    @Mock private Message message;

    private TestRouteCallbackHandler handler;

    private static final Long CHAT_ID = 12345L;
    private static final Integer MESSAGE_ID = 67890;
    private static final String FAKE_AMOUNT = "100 km";

    private static class TestRouteCallbackHandler extends AbstractRouteCallbackHandler {

        private String savedText;

        protected TestRouteCallbackHandler(TelegramSender sender, BotFacade botFacade) {
            super(sender, botFacade);
        }

        @Override
        public GeneralFields callbackData() {
            return GeneralFields.SET_MORNING_MONDAY_KM;
        }

        @Override
        protected String getRouteAmount(Long chatId) {
            return FAKE_AMOUNT;
        }

        @Override
        protected void setRouteAmount(Long chatId, String text) {
            this.savedText = text;
        }
    }

    @BeforeEach
    void setUp() {
        handler = new TestRouteCallbackHandler(sender, botFacade);
    }

    @Test
    void handleCallbackQuery_ShouldEditMarkupWithDynamicBack() {
        when(callbackQuery.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getMessageId()).thenReturn(MESSAGE_ID);

        handler.handle(callbackQuery);

        String expectedText = GeneralFields.SET_MORNING_MONDAY_KM.getDescription() + ": " + FAKE_AMOUNT;
        InlineKeyboardMarkup expectedKeyboard = KeyboardFactory.dynamicBack(GeneralFields.SET_MORNING_MONDAY_KM);

        verify(sender).editMarkup(CHAT_ID, MESSAGE_ID, expectedText, expectedKeyboard);
    }

    @Test
    void handleTextAnswer_ShouldTrimTextSaveAndBuildGroupMenu() {
        String userInputWithSpaces = "   250 km   ";
        String expectedTrimmedText = "250 km";
        Integer botMenuId = 999;

        handler.handle(CHAT_ID, userInputWithSpaces, botMenuId, sender);

        assertThat(handler.savedText).isEqualTo(expectedTrimmedText);
        InlineKeyboardMarkup expectedGroupKeyboard =
                KeyboardFactory.buildMenuForGroup(GeneralFields.SET_MORNING_MONDAY_KM);
        verify(sender).editMarkup(CHAT_ID, botMenuId, "Данные приняты!\n", expectedGroupKeyboard);
    }
}
