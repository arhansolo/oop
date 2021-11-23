package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public void logUserInformation(Update update) {
        String userName = update.getMessage().getChat().getUserName();
        long user_id = update.getMessage().getChat().getId();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        System.out.println(dateFormat.format(date));
        System.out.println("Message from @" +  userName + ", id = " + user_id + "\n");
    }

}
