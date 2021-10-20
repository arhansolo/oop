package ru.amogus.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.amogus.bot.handlers.MessageUpdate;
import ru.amogus.bot.handlers.UpdateHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {
    List<UpdateHandler> handlers = new ArrayList<>();
    public Bot() {
        handlers.add(new MessageUpdate());
    }

    private static final String USERNAME = "optimumprice_bot";
    public String getBotUsername(){return USERNAME;}

    @SneakyThrows
    public String getBotToken(){return readToken("token.txt");}
    public static String readToken(String fileName) throws IOException {
        String content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
        return content;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        BotResponse message = new BotResponse(null, null);

        for (UpdateHandler handler : handlers) {
            if (handler.validate(update)) {
                message = handler.handle(update);
                break;
            }
        }

        if (message.getOutputPhoto() == null) { //Нужно, чтобы проверял все типы данных
            return;
        }
        try {
            execute(message.getOutputPhoto());
        } catch (TelegramApiException e) {
            //e.printStackTrace();
        }
        if (message.getOutput() == null) { //Нужно, чтобы проверял все типы данных
            return;
        }
        try {
            execute(message.getOutput());
        } catch (TelegramApiException e) {
            //e.printStackTrace();
        }
    }
}