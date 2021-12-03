package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.botObjects.BotResponse;

import java.io.IOException;

public class TextUpdate implements UpdateHandler {
    @Override
    public boolean validate(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public BotResponse handle(Update update) throws IOException {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());
        String text = mesUpdate.getText();

        BotRequest userMessage = new BotRequest(text, null, null,null);
        text = userMessage.getInputText();
        BotRequest request = new BotRequest(text, null, null,null);

        return buildResponse(chatId, request);
    }
}
