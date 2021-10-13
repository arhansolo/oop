package ru.amogus.bot;

import java.lang.*;
import java.io.IOException;

public class Handler {

    String hello() {
        return ("Привет!\n" +
                "Чтобы ознакомиться с функционалом бота, отправь /help!");
    }
    String help() {
        return ("Список доступных команд:\n" + "/randomPoem - случайный экземпляр из коллекции русской поэзии!\n");
    }
    String stop() { return "До встречи!"; }

    public String distributor(BotRequest request) throws IOException {
        String instruction = request.getInput();
        Dispatcher dispatcher = new Dispatcher();
        switch (instruction) {
            case "/start":
                return hello();
            case "/stop":
                return stop();
            case "/randomPoem":
            {
                Poem poem = new Poem();
                return poem.getPoem();
            }
            case "/help":
                return help();
            default:
                return "Не понял твою команду!";
        }
    }
}