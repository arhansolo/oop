package ru.amogus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Poem {
    public String getPoem() throws IOException
    {
        Document doc = Jsoup.connect("http://russian-poetry.ru/Random.php").get();
        Elements poem = doc.select("pre");
        String title = doc.title();
        String res = title+ "\n\n" + poem.text().replace("<pre>", "");
        return res;
    }
}
