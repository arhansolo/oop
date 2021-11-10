package ru.amogus.bot;

import com.google.zxing.NotFoundException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.amogus.bot.parsers.KnigaBookParser;
import ru.amogus.bot.parsers.Poem;
import ru.amogus.bot.parsers.FindBookParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
        SendMessage textResponse = new SendMessage();
        SendPhoto photoResponse = new SendPhoto();
        EditMessageText editTextResponse = new EditMessageText();
        EditMessageCaption editCaptionResponse = new EditMessageCaption();

        String instruction = request.getInputText();
        List<PhotoSize> photo = request.getInputPhoto();
        CallbackQuery callbackQuery = request.getInputCallbackQuery();

        if (instruction!=null)
        {
            textResponse.setText(handleText(instruction));
        }
        else if(photo!=null)
        {
            getBookInf(textResponse, photoResponse, editTextResponse, editCaptionResponse, request);
        }
        else if (callbackQuery != null)
        {
            String callData = callbackQuery.getData();
            String messageId = Integer.toString(callbackQuery.getMessage().getMessageId());
            if (callData.equals("updateText"))
            {
                String answer = "Да, я всё видел - это ты нажал кнопку!";
                editTextResponse.setText(answer);
                editTextResponse.setMessageId(Integer.parseInt(messageId));
            }

            else if (callData.equals("updateCaption"))
            {
                String answer = "Да, я всё видел - это ты нажал кнопку!";
                editCaptionResponse.setCaption(answer);
                editCaptionResponse.setMessageId(Integer.parseInt(messageId));
            }
        }

        return new BotResponse(textResponse, photoResponse, editTextResponse, editCaptionResponse);
    }

    public void getBookInf(SendMessage textResponse, SendPhoto photoResponse, EditMessageText editTextResponse,
                           EditMessageCaption editCaptionResponse, BotRequest request) throws IOException {
        KnigaBookParser kb = new KnigaBookParser();

        try {
            String isbnCode = getIsbnFromBarcode(request);
            String bookInf = kb.getInformation(isbnCode);

            FindBookParser fb = new FindBookParser();
            String priceLink = fb.getInformation(isbnCode);

            try {
                BufferedImage bookCover = kb.getImage(isbnCode);
                File outputFile = new File("resources/cover.png");
                ImageIO.write(bookCover, "png", outputFile);
                photoResponse.setPhoto(new InputFile((outputFile)));
                photoResponse.setCaption(bookInf);
                setMarkupInline("updateCaption", "Показать цены!", priceLink,
                        textResponse, photoResponse);
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                textResponse.setText(bookInf);
                if (fb.isGoodRequest((new URL("https://knigabook.com/search?q="+isbnCode))))
                    setMarkupInline("updateText","Показать цены!", priceLink,
                            textResponse, photoResponse);
            }

        } catch (NotFoundException | IOException e) {
            textResponse.setText("К сожалению, не удалось распознать штрихкод\uD83D\uDE14\n" +
                    "Попробуй отправить ещё одно фото");
        }
    }

    public void getBookPrice (SendMessage textResponse, SendPhoto photoResponse, EditMessageText editTextResponse,
                              EditMessageCaption editCaptionResponse, BotRequest request) throws IOException {
        FindBookParser fb = new FindBookParser();
        try {
            String isbnCode = getIsbnFromBarcode(request);
            String result = fb.getInformation(isbnCode);
            textResponse.setText(result);
            setMarkupInline("updateText", "Показать информацию по книге!", "",
                    textResponse, photoResponse);

        } catch (NotFoundException | IOException e) {
            textResponse.setText("К сожалению, не удалось распознать штрихкод\uD83D\uDE14\n" +
                    "Попробуй отправить ещё одно фото");
        }
    }

    public String getIsbnFromBarcode (BotRequest request) throws IOException, NotFoundException {
        BarcodeReader br = new BarcodeReader();
        BufferedImage image = handlePhoto(request);
            return br.readBarcode(image);
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
                return poem.getInformation("");
            }
            case "/help":
                return help();
            default:
                return "Не понял тебя!";
        }
    }
    public BufferedImage handlePhoto (BotRequest request) throws IOException {
        List<PhotoSize> photo = request.getInputPhoto();

        String fileId = Objects.requireNonNull(photo.stream()
                .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                .findFirst()
                .orElse(null)).getFileId();
        FileDownloader fd = new FileDownloader();
        BufferedImage image =  fd.getFile(fileId);

        return image;
    }

    public void setMarkupInline (String callbackData, String buttonText, String url, SendMessage textResponse,
                                 SendPhoto photoResponse)
    {
        InlineKeyboardMarkup markupInline = getMarkupInline(callbackData, buttonText, url);

        switch (callbackData)
        {
            case "updateText":
            {
                textResponse.setReplyMarkup(markupInline);
                break;
            }
            case "updateCaption":
            {
                photoResponse.setReplyMarkup(markupInline);
                break;
            }
            default: break;
        }
    }
    public InlineKeyboardMarkup getMarkupInline (String callbackData, String buttonText, String url)
    {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton ikb = new InlineKeyboardButton();

        ikb.setCallbackData(callbackData);
        ikb.setText(buttonText);
        ikb.setUrl(url);

        rowInline.add(ikb);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}