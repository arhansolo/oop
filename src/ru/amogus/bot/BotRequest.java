package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public class BotRequest {
    private final String input;
    private final List<PhotoSize> inputPhoto;

    public BotRequest(String input,List<PhotoSize> inputPhoto) {
        this.input = input;
        this.inputPhoto = inputPhoto;
    }

    public String getInput() {
        return input;
    }
    public List<PhotoSize> getInputPhoto()
    {
        return inputPhoto;
    }
}
