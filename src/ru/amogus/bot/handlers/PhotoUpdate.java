package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.BotResponse;
import ru.amogus.bot.Handler;

import java.io.IOException;
import java.util.List;

public class PhotoUpdate implements UpdateHandler{
    @Override
    public boolean validate(Update update) { return update.hasMessage() && update.getMessage().hasPhoto();}

    @Override
    public BotResponse handle(Update update) throws IOException
    {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());

        List<PhotoSize> photo = mesUpdate.getPhoto();
        BotRequest userMessage = new BotRequest(null, photo);
        photo = userMessage.getInputPhoto();
        BotRequest request = new BotRequest(null, photo);

        Handler handler = new Handler();
        SendPhoto photoResponse = handler.distribute(request).getOutputPhoto(); photoResponse.setChatId(chatId);
        photoResponse.setCaption("В ответ на твою фотографию я отправлю это чудесное лого Телеграма!");
        return new BotResponse(null, photoResponse);
    }
}
