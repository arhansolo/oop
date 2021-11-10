package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public class BotRequest {
    private final String inputText;
    private final List<PhotoSize> inputPhoto;
    private final CallbackQuery inputCallbackQuery;

    public BotRequest(String inputText, List<PhotoSize> inputPhoto, CallbackQuery inputCallbackQuery) {
        this.inputText = inputText;
        this.inputPhoto = inputPhoto;
        this.inputCallbackQuery = inputCallbackQuery;
    }

    public String getInputText() {
        return inputText;
    }
    public List<PhotoSize> getInputPhoto()
    {
        return inputPhoto;
    }
    public CallbackQuery getInputCallbackQuery() { return  inputCallbackQuery; }
}
