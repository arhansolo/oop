package ru.amogus.bot;

import java.lang.*;
import java.io.IOException;

public class Handler {

    String hello() {
        return ("Привет!\n" +
                "Чтобы ознакомиться с функционалом бота, отправь /help!");
    }
    String help() {
        return ("Список доступных команд:\n" + "/randomPoem - случайный экземпляр из коллекции русской поэзии!\n" +
                "/stop - завершить обработку команд.");
    }
    String stop()
    {
        return "До встречи!";
    }

    public String distributor(String instruction) throws IOException {
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
