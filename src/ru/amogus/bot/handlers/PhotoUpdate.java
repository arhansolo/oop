package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.amogus.bot.*;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PhotoUpdate implements UpdateHandler{
    @Override
    public boolean validate(Update update) { return update.hasMessage() && update.getMessage().hasPhoto();}

    @Override
    public BotResponse handle(Update update) throws IOException {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());
        List<PhotoSize> photo = mesUpdate.getPhoto();

        BotRequest userMessage = new BotRequest(null, photo);
        photo = userMessage.getInputPhoto();
        BotRequest request = new BotRequest(null, photo);

        return buildResponse(chatId, request);
    }

    public BotResponse buildResponse (String chatId, BotRequest request) throws IOException {
        Handler handler = new Handler();

        BotResponse distributedData = handler.distribute(request);

        SendPhoto photoResponse = distributedData.getOutputPhoto();
        photoResponse.setChatId(chatId);

        SendMessage textResponse = distributedData.getOutput();
        textResponse.setChatId(chatId);
        return new BotResponse(textResponse, photoResponse);
    }
}
