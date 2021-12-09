
package ru.amogus.bot;
import com.google.zxing.NotFoundException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.messageComposers.IsbnHandler;
import ru.amogus.bot.messageComposers.ResponseConstants;
import ru.amogus.bot.parsers.Poem;
import java.io.IOException;

public class IsbnHandlerTest {
    @Test
    public void ISBNFromTextMessageIsCorrect() throws IOException, NotFoundException {
        IsbnHandler ih = new IsbnHandler();
        String isbn = "526700054X";
        BotRequest request = new BotRequest(isbn, null, null,null);
        SendMessage textResponse = new SendMessage();
        String resultISBN = ih.validateISBN(request, textResponse);
        Assert.assertEquals(isbn, resultISBN);
        Assert.assertNull(textResponse.getText());
    }

    @Test
    public void ISBNFromTextMessageIsIncorrect() throws IOException, NotFoundException {
        IsbnHandler ih = new IsbnHandler();
        String isbn = "93534959352935390004050450";
        BotRequest request = new BotRequest(isbn, null, null,null);
        SendMessage textResponse = new SendMessage();
        String resultISBN = ih.validateISBN(request, textResponse);
        Assert.assertNull(resultISBN);
        Assert.assertEquals(ResponseConstants.INVALID_ISBN.getContent(), textResponse.getText());
    }

    @Test
    @Ignore
    public void ISBNFromBarcodeDocumentIsCorrect() throws IOException, NotFoundException {
        IsbnHandler ih = new IsbnHandler();
        String isbn = "9785981241475";

        PhotoSize ps = new PhotoSize("AAMCAgADGQEAAgx1YZZpbmWy7mmLrZOFcCGGF1mFOiAAAhkVAAL2QbBI61knfmJuogQBAAdtAAMiBA", "AQADGRUAAvZBsEhy", 180, 320, 19433, null);
        Document document = new Document("BQACAgIAAxkBAAIMdWGWaW5lsu5pi62ThXAhhhdZhTogAAIZFQAC9kGwSOtZJ35ibqIEIgQ", "AgADGRUAAvZBsEg", ps, "barcodeTest.jpg", "image/jpeg", 115760);

        BotRequest request = new BotRequest(null, null, document,null);
        SendMessage textResponse = new SendMessage();
        String resultIsbn = ih.validateISBN(request, textResponse);

        Assert.assertEquals(isbn, resultIsbn);
        Assert.assertNull(textResponse.getText());
    }

    @Test
    @Ignore
    public void PhotoFromDocumentHasUnknownFormat() throws IOException, NotFoundException {
        IsbnHandler ih = new IsbnHandler();

        PhotoSize ps = new PhotoSize("AAMCAgADGQEAAgx5YZZsb8O4dtd4Y8ldzYa3NOGZa7IAAh8SAAJfobFIXr_9Ow6vlTsBAAdtAAMiBA", "AQADHxIAAl-hsUhy", 227, 320, 17471, null);
        Document document = new Document("BQACAgIAAxkBAAIMeWGWbG_DuHbXeGPJXc2GtzThmWuyAAIfEgACX6GxSF6__TsOr5U7IgQ", "AgADHxIAAl-hsUg", ps , "Регламент практик по ООП (Java) 2020.pdf", "application/pdf", 104090);

        BotRequest request = new BotRequest(null, null, document,null);
        SendMessage textResponse = new SendMessage();
        String resultIsbn = ih.validateISBN(request, textResponse);
        Assert.assertNull(resultIsbn);
        Assert.assertEquals(ResponseConstants.WRONG_PHOTO_FORMAT.getContent(), textResponse.getText());
    }

    @Test
    @Ignore
    public void getPoemTest() throws IOException {
        Poem poem = new Poem();
        String str = poem.getInformation("");
        Assert.assertEquals("Я помню чудное мгновенье!", str);
    }
}
