package ru.amogus.bot.messageComposers;

import com.google.zxing.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.amogus.bot.botObjects.BotRequest;
import ru.amogus.bot.parsers.FindBookParser;
import ru.amogus.bot.parsers.KnigaBookParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static ru.amogus.bot.messageComposers.Response.UNREADABLE_BARCODE;

public class MessageComposer {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    private final InlineKeyboardEditor ikEditor = new InlineKeyboardEditor();

    private final String buttonPriceText = "Показать цены";

    public void getBookInf(SendMessage textResponse, SendPhoto photoResponse, BotRequest request) {
        KnigaBookParser kb = new KnigaBookParser();
        IsbnHandler ih = new IsbnHandler();

        try {
            String isbnCode = ih.validateISBN(request, textResponse);

            if (isbnCode == null) {
                return;
            }

            String bookInf = kb.getInformation(isbnCode);
            FindBookParser fb = new FindBookParser();
            String priceLink = fb.getInformation(isbnCode);

            try {
                BufferedImage bookCover = kb.getImage(isbnCode);
                File outputFile = new File("resources/cover.png");
                ImageIO.write(bookCover, "png", outputFile);
                photoResponse.setPhoto(new InputFile((outputFile)));
                photoResponse.setCaption(bookInf);
                ikEditor.setMarkupInline("updateCaption", buttonPriceText, priceLink, textResponse, photoResponse);
            }

            catch (Exception e) {
                logger.error(e.toString());
                textResponse.setText(bookInf);

                if (fb.isGoodRequest((new URL("https://knigabook.com/search?q="+isbnCode)))) {
                    ikEditor.setMarkupInline("updateText", buttonPriceText, priceLink, textResponse, photoResponse);
                }
            }
        }

        catch (NotFoundException | IOException e) {
            logger.error(e.toString());
            textResponse.setText(UNREADABLE_BARCODE.getContent());
        }
    }
}
