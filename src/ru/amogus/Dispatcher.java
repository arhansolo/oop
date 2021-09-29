package ru.amogus;
import java.util.Scanner;

public class Dispatcher {
    public void output(String instruction)
    {
        System.out.println(instruction);
    }

    public String input()
    {
        Scanner scan = new Scanner(System.in); return scan.next();
    }


}
