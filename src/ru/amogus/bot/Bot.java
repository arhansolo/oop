package ru.amogus.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Bot extends TelegramLongPollingBot {
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
        Handler handler = new Handler();
        Dispatcher dispatcher = new Dispatcher();
        long chatId = update.getMessage().getChatId();
        if (update.hasMessage() && update.getMessage().hasText()) {
            BotRequest userMessage = dispatcher.input(update);

            SendMessage message = new SendMessage();
            message.setChatId(Long.toString(chatId));
            BotRequest request = new BotRequest(userMessage.getInput());
            message.setText(handler.distributor(request));

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

