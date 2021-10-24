package ru.amogus.bot;

import com.google.zxing.NotFoundException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.File;
import java.lang.*;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

        String instruction = request.getInput();
        List<PhotoSize> photo = request.getInputPhoto();
        if (instruction!=null)
        {
            textResponse.setText(handleText(instruction));
        }
        else if(photo!=null)
        {
            getBookInf(textResponse, photoResponse, request);
        }
        return new BotResponse(textResponse, photoResponse);
    }

    public void getBookInf(SendMessage textResponse, SendPhoto photoResponse, BotRequest request) throws IOException {
        BarcodeReader br = new BarcodeReader();
        KnigaBookParser kb = new KnigaBookParser();

        File resultPhoto = handlePhoto(request);
        String photoPath = resultPhoto.getPath();
        try {
            String isbnCode = br.readBarcode(photoPath);
            String bookInf = kb.getInformation(isbnCode);
            try {
                String pathCover = kb.downloadCover(isbnCode);
                photoResponse.setPhoto(new InputFile(new File(pathCover)));
                photoResponse.setCaption(bookInf);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                textResponse.setText(bookInf);
            }

        } catch (NotFoundException | IOException e) {
            textResponse.setText("К сожалению, не удалось распознать штрихкод\uD83D\uDE14\n" +
                    "Попробуй отправить ещё одно фото");
        }
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
    public File handlePhoto (BotRequest request) throws IOException {
        List<PhotoSize> photo = request.getInputPhoto();

        String fileId = Objects.requireNonNull(photo.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null)).getFileId();
        FileDownloader fd = new FileDownloader();
        fd.uploadFile("", fileId);

        return new File(fd.getFileName());
    }
}