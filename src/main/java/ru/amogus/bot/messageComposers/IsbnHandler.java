package ru.amogus.bot.messageComposers;

import com.google.zxing.NotFoundException;
import org.apache.commons.validator.routines.ISBNValidator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.parsers.FileDownloader;
import ru.amogus.bot.parsers.KnigaBookParser;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ru.amogus.bot.messageComposers.Response.INVALID_ISBN;
import static ru.amogus.bot.messageComposers.Response.WRONG_PHOTO_FORMAT;

public class IsbnHandler {

    @Nullable
    public String validateISBN(BotRequest request, SendMessage textResponse) throws IOException, NotFoundException {
        String isbn;

        if (request.getInputText()!=null) {
            isbn = request.getInputText();
        }

        else {
            isbn = getIsbnFromBarcode(request);
        }

        if (isbn == null) {
            textResponse.setText(WRONG_PHOTO_FORMAT.getContent());
            return null;
        }

        if (!isValidISBN(isbn)) {
            textResponse.setText(INVALID_ISBN.getContent());
            isbn = null;
        }

        return isbn;
    }

    private boolean isValidISBN(String isbn) {
        ISBNValidator iv = new ISBNValidator();
        return iv.isValid(isbn);
    }

    @Nullable
    private String getIsbnFromBarcode (BotRequest request) throws IOException, NotFoundException {
        BarcodeReader br = new BarcodeReader();
        BufferedImage image = handlePhoto(request);

        try {
            return br.readBarcode(image);
        }

        catch (NullPointerException e) {
            return null;
        }
    }

    @Nullable
    private BufferedImage handlePhoto(BotRequest request) throws IOException {
        List<PhotoSize> compressedPhoto = request.getInputPhoto();
        Document photo = request.getInputDocument();
        FileDownloader fd = new FileDownloader();

        if (compressedPhoto != null) {
            String fileId = Objects.requireNonNull(compressedPhoto.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .orElse(null)).getFileId();
            return fd.getPhoto(fileId);
        }

        if (photo != null) {
            String fileId = photo.getFileId();
            return fd.getPhoto(fileId);
        }

        return null;
    }
}
