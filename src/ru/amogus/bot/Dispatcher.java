package ru.amogus.bot;
import java.util.Scanner;

public class Dispatcher {
    private final Scanner scanner = new Scanner(System.in);

    public void output(String instruction)
    {
        System.out.println(instruction);
    }

    public BotRequest input()
    {
        return new BotRequest(scanner.next());
    }
}