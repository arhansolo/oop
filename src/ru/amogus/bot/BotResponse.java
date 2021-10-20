package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class BotResponse extends SendMessage {
    private final SendMessage output;
    private final SendPhoto outputPhoto;
    public BotResponse(SendMessage output, SendPhoto outputPhoto)
    {
        this.output = output;
        this.outputPhoto = outputPhoto;
    }
    public SendMessage getOutput() {
        return output;
    }

    public SendPhoto getOutputPhoto()
    {
        return outputPhoto;
    }


}
