package ru.amogus.bot;

import java.io.IOException;

public class Main {
    public static void main(String [] args) throws IOException {
        BotRequest request = new BotRequest("/start");
        Dispatcher dispatcher = new Dispatcher();
        Handler handler = new Handler();
        dispatcher.output(handler.distributor(request)); //представим, что была отправлена команда /start, как это обычно бывает в Telegram при первом запуске бота
        while(!request.getInput().equals("/stop")){
            request = dispatcher.input();
            dispatcher.output(handler.distributor(request));
        }
    }
}