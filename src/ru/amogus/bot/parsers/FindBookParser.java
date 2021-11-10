package ru.amogus.bot.parsers;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class FindBookParser extends Parser{
    @Override
    public String getInformation(String isbn) throws IOException {

        return parseFindBook(isbn);
    }
    @Override
    public BufferedImage getImage(String instruction) throws IOException {
        return null;
    }

    public String parseFindBook (String isbn) throws IOException {
        URL url = new URL ("https://findbook.ru/search/d1?isbn=" + isbn); //9785170878888
       /* URL url = new URL("https://findbook.ru/search/d1?title=&authors=%D2%EE%EB%F1%F2%EE%E9&publisher=&isbn=&s=1");
        Document doc = Jsoup.connect(url.toString()).get();
        Elements trs = doc.select("body").select("center").get(1).select("table").select("tr").select("tbody").get(3).select("tr");

        for (Element tr : trs)
        {
            System.out.println(tr.text() + "\n");
        }*/
        return url.toString();
    }

}
