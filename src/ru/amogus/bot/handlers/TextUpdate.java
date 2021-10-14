package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.Handler;

import java.io.IOException;

public class TextUpdate implements UpdateHandler {
    @Override
    public boolean validate(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public SendMessage handle(Update update) throws IOException {
        Handler handler = new Handler();
        long chatId = update.getMessage().getChatId();
        BotRequest userMessage = new BotRequest(update.getMessage().getText());
        SendMessage message = new SendMessage();
        message.setChatId(Long.toString(chatId));
        BotRequest request = new BotRequest(userMessage.getInput());
        message.setText(handler.distributor(request));
        return message;
    }
}
