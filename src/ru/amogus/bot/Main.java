package ru.amogus.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Map;

public class Main {
    private static final Map<String, String> envParams = System.getenv();

    public static void main(String [] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(new Bot(envParams.get("USERNAME"), envParams.get("TOKEN")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}