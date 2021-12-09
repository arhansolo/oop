package ru.amogus.bot.messageComposers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.botObjects.BotResponse;
import ru.amogus.bot.parsers.Poem;
import static ru.amogus.bot.messageComposers.ResponseConstants.*;
import static ru.amogus.bot.messageComposers.Constants.*;

import java.io.File;
import java.lang.*;
import java.io.IOException;
import java.util.List;

public class Handler {

    private final MessageComposer mc = new MessageComposer();

    private final static String IMAGE_PATH = "resources/example.jpg";

    public BotResponse distribute(BotRequest request) throws IOException {
        SendMessage textResponse = new SendMessage();
        SendPhoto photoResponse = new SendPhoto();

        textResponse.enableMarkdown(true);

        EditMessageText editTextResponse = new EditMessageText();
        EditMessageCaption editCaptionResponse = new EditMessageCaption();

        String instruction = request.getInputText();
        List<PhotoSize> photo = request.getInputPhoto();
        Document document = request.getInputDocument();
        CallbackQuery callbackQuery = request.getInputCallbackQuery();

        if (instruction != null) {
            handleText(textResponse, photoResponse, request);
        }

        else if(photo != null) {
            mc.getBookInf(textResponse, photoResponse, request);
        }

        else if (document != null) {
            mc.getBookInf(textResponse, photoResponse, request);
        }

        else if (callbackQuery != null) {
            setNewContentToMessage(callbackQuery, editTextResponse, editCaptionResponse);
        }

        return new BotResponse(textResponse, photoResponse, editTextResponse, editCaptionResponse);
    }

    private void setNewContentToMessage (CallbackQuery callbackQuery, EditMessageText editTextResponse, EditMessageCaption editCaptionResponse){
        String callData = callbackQuery.getData();
        String messageId = Integer.toString(callbackQuery.getMessage().getMessageId());

        if (callData.equals(UPDATE_TEXT)) {
            editTextResponse.setMessageId(Integer.parseInt(messageId));
        }

        else if (callData.equals(UPDATE_CAPTION)) {
            editCaptionResponse.setMessageId(Integer.parseInt(messageId));
        }
    }

    private void handleText (SendMessage textResponse, SendPhoto photoResponse, BotRequest request) throws IOException {
        switch (request.getInputText()) {
            case "/start" -> textResponse.setText(HELLO.getContent());

            case "/example" -> {
                photoResponse.setCaption(EXAMPLE.getContent());
                photoResponse.setPhoto(new InputFile(new File(IMAGE_PATH)));
            }

            case "/randompoem" -> {
                Poem poem = new Poem();
                textResponse.setText(poem.getInformation(""));
            }

            case "/help" -> textResponse.setText(HELP.getContent());

            default -> mc.getBookInf(textResponse, photoResponse, request);
        }
    }
}