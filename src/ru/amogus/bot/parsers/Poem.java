package ru.amogus.bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.annotation.Nullable;
import javax.validation.constraints.Null;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Poem extends Parser {
    @Override
    public String getInformation(String instruction) throws IOException
    {
        Document doc = Jsoup.connect("http://russian-poetry.ru/Random.php").get();
        Elements poem = doc.select("pre");
        String title = doc.title().replace("Русская поэзия. Случайное стихотворение. ","");
        return title+ "\n\n" + poem.text();
    }

    @Override
    @Nullable
    public BufferedImage getImage(String instruction) {
        return null;
    }
}