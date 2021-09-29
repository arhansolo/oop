package ru.amogus;

import java.lang.*;
import java.io.IOException;

public class Handler {

    public String hello() {
        return ("Привет!\n" +
                "Чтобы ознакомиться с функционалом бота, отправь /help!");
    }
    public String help() {
        return ("Список доступных команд:\n" + "/randomPoem - случайный экземпляр из коллекции русской поэзии!\n" +
                "/stop - завершить обработку команд.");
    }

    public void distributor(String instruction) throws IOException {
        Dispatcher dispatcher = new Dispatcher();
        switch (instruction) {
            case "/start"->
                dispatcher.output(hello());
            case "/stop"->
                dispatcher.output("До встречи!");
            case "/randomPoem"->
            {
                Poem poem = new Poem();
                dispatcher.output(poem.getPoem());
            }
            case "/help"->
                dispatcher.output(help());
            default->
                dispatcher.output("Не понял твою команду!");
        }
    }
}
