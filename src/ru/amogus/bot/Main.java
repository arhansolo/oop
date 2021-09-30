package ru.amogus.bot;

import java.io.IOException;

public class Main {
    public static void main(String [] args) throws IOException {
        String userMessage = "";
        Dispatcher dispatcher = new Dispatcher();
        Handler handler = new Handler();
        dispatcher.output(handler.distributor("/start")); //представим, что была отправлена команда /start, как это обычно бывает в Telegram при первом запуске бота
        while(!userMessage.equals("/stop")){
            userMessage = dispatcher.input();
            dispatcher.output(handler.distributor(userMessage));
        }
    }
}
