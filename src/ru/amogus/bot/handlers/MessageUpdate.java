package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.BotResponse;
import ru.amogus.bot.Handler;

import java.io.IOException;
import java.util.List;

public class MessageUpdate extends UpdateHandler {
    @Override
    public boolean validate(Update update) { return update.hasMessage() && (update.getMessage().hasText() || update.getMessage().hasPhoto()); }

    @Override
    public BotResponse handle(Update update) throws IOException {
        Message mesUpdate = update.getMessage();
        String chatId = Long.toString(mesUpdate.getChatId());

        BotRequest request = getRequest(mesUpdate);

        return getResponse(chatId, request);
    }

    public BotRequest getRequest (Message mesUpdate)
    {
        String text = mesUpdate.getText();
        List<PhotoSize> photo = mesUpdate.getPhoto();
        CallbackQuery callData = new Update().getCallbackQuery();

        BotRequest userMessage = new BotRequest(text, photo, callData);
        text = userMessage.getInputText(); photo = userMessage.getInputPhoto();


        return new BotRequest(text, photo, callData);
    }

    public BotResponse getResponse (String chatId, BotRequest request) throws IOException {
        Handler handler = new Handler();

        SendMessage textResponse = handler.distribute(request).getOutputText();
        textResponse.setChatId(chatId);

        SendPhoto photoResponse = handler.distribute(request).getOutputPhoto();
        photoResponse.setChatId(chatId);

        EditMessageText editTextResponse = new EditMessageText();
        editTextResponse.setChatId(chatId);

        EditMessageCaption editCaptionResponse = new EditMessageCaption();
        editCaptionResponse.setChatId(chatId);
        //photoResponse.setCaption("В ответ на твою фотографию я отправлю это чудесное лого Телеграма!");

        return new BotResponse(textResponse, photoResponse, editTextResponse, editCaptionResponse);

    }
    public SendMessage handleText(Update update) throws IOException
    {
        return handle(update).getOutputText();
    }

    public SendPhoto handlePhoto(Update update) throws IOException
    {
        return handle(update).getOutputPhoto();
    }
}
