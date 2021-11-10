package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.BotResponse;
import ru.amogus.bot.Handler;

import java.io.IOException;

public abstract class UpdateHandler {
    public abstract boolean validate(Update update);
    public abstract BotResponse handle(Update update) throws IOException, TelegramApiException;

    public BotResponse buildResponse (String chatId, BotRequest request) throws IOException {
        Handler handler = new Handler();

        BotResponse distributedData = handler.distribute(request);

        SendPhoto photoResponse = distributedData.getOutputPhoto();
        photoResponse.setChatId(chatId);

        SendMessage textResponse = distributedData.getOutputText();
        textResponse.setChatId(chatId);

        EditMessageText editTextResponse = distributedData.getOutputEditText();
        editTextResponse.setChatId(chatId);

        EditMessageCaption editCaptionResponse = distributedData.getOutputEditCaption();
        editCaptionResponse.setChatId(chatId);

        return new BotResponse(textResponse, photoResponse, editTextResponse, editCaptionResponse);
    }
}
