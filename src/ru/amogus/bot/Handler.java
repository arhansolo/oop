package ru.amogus.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.File;
import java.lang.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Handler {

    String hello() {
        return ("Привет!\n" +
                "Чтобы ознакомиться с функционалом бота, отправь /help!");
    }
    String help() {
        return ("Список доступных команд:\n" + "/randompoem - случайный экземпляр из коллекции русской поэзии!\n");
    }
    String stop() { return "До встречи!"; }

    public BotResponse distribute(BotRequest request) throws IOException {
        SendMessage textResponse = new SendMessage(); SendPhoto photoResponse = new SendPhoto();
        BotResponse response = new BotResponse(null, null);

        String instruction = request.getInput();
        List<PhotoSize> photo = request.getInputPhoto();
        if (instruction!=null)
        {
            textResponse.setText(handleText(instruction));
        }
        else if(photo!=null)
        {
            photoResponse.setPhoto(new InputFile(handlePhoto(request)));
        }
        response = new BotResponse(textResponse, photoResponse);
        return response;
    }

    public String handleText (String instruction) throws IOException {
        switch (instruction) {
            case "/start":
                return hello();
            case "/stop":
                return stop();
            case "/randompoem":
            {
                Poem poem = new Poem();
                return poem.getPoem();
            }
            case "/help":
                return help();
            default:
                return "Не понял тебя!";
        }
    }
    public File handlePhoto (BotRequest request)
    {
        return new File("photo.png");
    }
}