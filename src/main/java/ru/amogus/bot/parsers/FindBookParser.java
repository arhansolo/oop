package ru.amogus.bot.parsers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class FindBookParser extends Parser{
    @Override
    public String getInformation(String isbn) throws IOException {

        return parseFindBook(isbn);
    }

    @Override
    @Nullable
    public BufferedImage getImage(String instruction) {
        return null;
    }

    public String parseFindBook (String isbn) throws IOException {
        URL url = new URL ("https://findbook.ru/search/d1?isbn=" + isbn);
        //Document doc = Jsoup.connect(url.toString()).get();
        return url.toString();
    }

}
