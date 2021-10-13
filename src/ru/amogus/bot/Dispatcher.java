package ru.amogus.bot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Scanner;

public class Dispatcher {
    private final Scanner scanner = new Scanner(System.in);

    public void output(Long chatId, String instruction)
    {
        System.out.println(instruction);
    }

    public BotRequest input(Update update)
    {
        return new BotRequest(update.getMessage().getText());

    }
}