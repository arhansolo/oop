package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public class BotResponse extends SendMessage {
    private final SendMessage outputText;
    private final SendPhoto outputPhoto;
    private final EditMessageText outputEditText;
    private final EditMessageCaption outputEditCaption;
    public BotResponse(SendMessage outputText, SendPhoto outputPhoto, EditMessageText outputEditText, EditMessageCaption outputEditCaption)
    {
        this.outputText = outputText;
        this.outputPhoto = outputPhoto;
        this.outputEditText = outputEditText;
        this.outputEditCaption = outputEditCaption;
    }
    public SendMessage getOutputText() {
        return outputText;
    }

    public SendPhoto getOutputPhoto()
    {
        return outputPhoto;
    }

    public EditMessageText getOutputEditText() {return outputEditText;}

    public EditMessageCaption getOutputEditCaption () {return  outputEditCaption;}


}
