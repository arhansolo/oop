package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.BotResponse;
import ru.amogus.bot.Handler;

import java.io.IOException;

public class TextUpdate implements UpdateHandler{
    @Override
    public boolean validate(Update update) { return update.hasMessage() && update.getMessage().hasText();}

    @Override
    public BotResponse handle(Update update) throws IOException
    {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());
        String text = mesUpdate.getText();
        BotRequest userMessage = new BotRequest(text, null);
        text = userMessage.getInput();
        BotRequest request = new BotRequest(text, null);

        return buildResponse(chatId, request);
    }

    public BotResponse buildResponse (String chatId, BotRequest request) throws IOException
    {
        Handler handler = new Handler();
        BotResponse distributedData = handler.distribute(request);
        SendMessage textResponse = distributedData.getOutput(); textResponse.setChatId(chatId);
        return new BotResponse(textResponse, null);
    }
}
