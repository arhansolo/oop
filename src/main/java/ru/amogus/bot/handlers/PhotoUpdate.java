package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.botObjects.BotResponse;

import java.io.IOException;
import java.util.List;

public class PhotoUpdate implements UpdateHandler {
    @Override
    public boolean validate(Update update) {
        return update.hasMessage() && update.getMessage().hasPhoto();
    }

    @Override
    public BotResponse handle(Update update) throws IOException {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());
        List<PhotoSize> photo = mesUpdate.getPhoto();

        BotRequest userMessage = new BotRequest(null, photo, null,null);
        photo = userMessage.getInputPhoto();
        BotRequest request = new BotRequest(null, photo, null,null);

        return buildResponse(chatId, request);
    }
}
