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
import static ru.amogus.bot.Response.*;
import javax.annotation.Nullable;
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

    public BotResponse distribute(BotRequest request) throws IOException {
        SendMessage textResponse = new SendMessage();
        SendPhoto photoResponse = new SendPhoto();
        EditMessageText editTextResponse = new EditMessageText();
        EditMessageCaption editCaptionResponse = new EditMessageCaption();

        String instruction = request.getInputText();
        List<PhotoSize> photo = request.getInputPhoto();
        Document document = request.getInputDocument();
        CallbackQuery callbackQuery = request.getInputCallbackQuery();

        if (instruction != null)
            handleText(textResponse, photoResponse, request);

        else if(photo != null)
            getBookInf(textResponse, photoResponse, request);

        else if (document != null)
            getBookInf(textResponse, photoResponse, request);

        else if (callbackQuery != null)
        {
            String callData = callbackQuery.getData();
            String messageId = Integer.toString(callbackQuery.getMessage().getMessageId());
            if (callData.equals("updateText"))
            {
                String answer = "Сообщение было изменено!";
                editTextResponse.setText(answer);
                editTextResponse.setMessageId(Integer.parseInt(messageId));
            }

            else if (callData.equals("updateCaption"))
            {
                String answer = "Описание было изменено!";
                editCaptionResponse.setCaption(answer);
                editCaptionResponse.setMessageId(Integer.parseInt(messageId));
            }
        }

        return new BotResponse(textResponse, photoResponse, editTextResponse, editCaptionResponse);
    }

    public void getBookInf(SendMessage textResponse, SendPhoto photoResponse, BotRequest request) {
        KnigaBookParser kb = new KnigaBookParser();

        try {
            String isbnCode = validateISBN(request, textResponse);
            if (isbnCode == null) return;

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
                textResponse.setText(bookInf);
                if (fb.isGoodRequest((new URL("https://knigabook.com/search?q="+isbnCode))))
                    setMarkupInline("updateText","Показать цены!", priceLink,
                            textResponse, photoResponse);
            }

        } catch (NotFoundException | IOException e) {
            textResponse.setText(UNREADABLE_BARCODE.getContent());
        }
    }

    @Nullable
    public String validateISBN(BotRequest request, SendMessage textResponse) throws IOException, NotFoundException {
        KnigaBookParser kb = new KnigaBookParser();

        String isbn;
        if (request.getInputText()!=null)
            isbn = request.getInputText();
        else
            isbn = getIsbnFromBarcode(request);

        if (isbn == null)
        {
            textResponse.setText(WRONG_PHOTO_FORMAT.getContent());
            return null;
        }

        if (!kb.isValidISBN(isbn))
        {
            textResponse.setText(INVALID_ISBN.getContent());
            isbn = null;
        }
        return isbn;
    }

    public void getBookPrice (SendMessage textResponse, SendPhoto photoResponse, BotRequest request) {
        FindBookParser fb = new FindBookParser();
        try {
            String isbnCode = getIsbnFromBarcode(request);
            String result = fb.getInformation(isbnCode);
            textResponse.setText(result);
            setMarkupInline("updateText", "Показать информацию по книге!", "",
                    textResponse, photoResponse);

        } catch (NotFoundException | IOException e) {
            textResponse.setText(UNREADABLE_BARCODE.getContent());
        }
    }

    @Nullable
    public String getIsbnFromBarcode (BotRequest request) throws IOException, NotFoundException {
        BarcodeReader br = new BarcodeReader();
        BufferedImage image = handlePhoto(request);
        try
        {
            br.readBarcode(image);
            return br.readBarcode(image);
        }

        catch (NullPointerException e)
        {
            return null;
        }
    }

    public void handleText (SendMessage textResponse, SendPhoto photoResponse, BotRequest request) throws IOException {
        switch (request.getInputText()) {
            case "/start" -> textResponse.setText(HELLO.getContent());
            case "/randompoem" -> {
                Poem poem = new Poem();
                textResponse.setText(poem.getInformation(""));
            }
            case "/help" -> textResponse.setText(HELP.getContent());
            default -> getBookInf(textResponse, photoResponse, request);
        }
    }

    @Nullable
    public BufferedImage handlePhoto(BotRequest request) throws IOException {
        List<PhotoSize> compressedPhoto = request.getInputPhoto();
        Document photo = request.getInputDocument();
        FileDownloader fd = new FileDownloader();

        if(compressedPhoto != null)
        {
            String fileId = Objects.requireNonNull(compressedPhoto.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            return fd.getPhoto(fileId);
        }

        if (photo != null)
        {
            String fileId = photo.getFileId();
            return fd.getPhoto(fileId);
        }
        return null;
    }

    public void setMarkupInline (String callbackData, String buttonText, String url, SendMessage textResponse,
                                 SendPhoto photoResponse)
    {
        InlineKeyboardMarkup markupInline = getMarkupInline(callbackData, buttonText, url);

        switch (callbackData) {
            case "updateText" ->
                textResponse.setReplyMarkup(markupInline);
            case "updateCaption" ->
                photoResponse.setReplyMarkup(markupInline);
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