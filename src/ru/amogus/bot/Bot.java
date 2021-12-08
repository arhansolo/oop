package ru.amogus.bot;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.amogus.bot.handlers.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    List<UpdateHandler> handlers = new ArrayList<>();
    public Bot()
    {
        handlers.add(new TextUpdate());
        handlers.add(new PhotoUpdate());
        handlers.add(new DocumentUpdate());
        handlers.add(new CallbackQueryUpdate());
    }

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private static final String USERNAME = "optimumprice_bot";
    public String getBotUsername() { return USERNAME; }

    @SneakyThrows
    public String getBotToken(){return readToken("token.txt");}
    public static String readToken(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName)).reduce("", String::concat);
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        BotResponse message = new BotResponse(null, null, null, null);
        logger.info(getInformationAboutUser(update));

        for (UpdateHandler handler : handlers) {
            if (handler.validate(update)) {
                message = handler.handle(update);
                break;
            }
        }

        SendMessage messageText =  message.getOutputText();
        SendPhoto messagePhoto = message.getOutputPhoto();
        EditMessageText editText = message.getOutputEditText();
        EditMessageCaption editCaption = message.getOutputEditCaption();

        if (messageText == null && messagePhoto == null && editText == null && editCaption == null) {
            return;
        }
        try {
                if(messageText.getText() != null)
                    execute(messageText);
                if (messagePhoto.getPhoto() != null)
                    execute(messagePhoto);
                if (editText.getText()!= null)
                    execute(editText);
                if (editCaption.getCaption()!=null)
                    execute(editCaption);
        } catch (TelegramApiException e) {
            logger.error("TelegramApiException:" + e);
        }
    }

    public String getInformationAboutUser (Update update)
    {
        String userName = update.getMessage().getChat().getUserName();
        long userID = update.getMessage().getChat().getId();

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();

        return "Received message from @" +  userName + ", id = " + userID + " at " + dateFormat.format(date);
    }
}