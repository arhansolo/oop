package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.botObjects.BotResponse;

import java.io.IOException;

public class DocumentUpdate implements UpdateHandler{
    @Override
    public boolean validate(Update update) {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    @Override
    public BotResponse handle(Update update) throws IOException {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());
        Document document = mesUpdate.getDocument();

        BotRequest userMessage = new BotRequest(null, null, document,null);
        document = userMessage.getInputDocument();
        BotRequest request = new BotRequest(null, null, document,null);

        return buildResponse(chatId, request);
    }
}
