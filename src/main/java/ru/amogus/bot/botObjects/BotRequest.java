package ru.amogus.bot.botObjects;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public class BotRequest {
    private final String inputText;

    private final List<PhotoSize> inputPhoto;

    private final Document inputDocument;

    private final CallbackQuery inputCallbackQuery;

    public BotRequest(String inputText, List<PhotoSize> inputPhoto, Document inputDocument, CallbackQuery inputCallbackQuery) {
        this.inputText = inputText;
        this.inputPhoto = inputPhoto;
        this.inputDocument = inputDocument;
        this.inputCallbackQuery = inputCallbackQuery;
    }

    public String getInputText() {
        return inputText;
    }

    public List<PhotoSize> getInputPhoto() {
        return inputPhoto;
    }

    public Document getInputDocument () {
        return inputDocument;
    }

    public CallbackQuery getInputCallbackQuery() {
        return  inputCallbackQuery;
    }
}
