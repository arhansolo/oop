package ru.amogus.bot.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class KnigaBookParser extends Parser {
    @Override
    public String getInformation(String isbn) throws IOException {
        URL url = new URL("https://knigabook.com/search?q="+isbn);
        if (!isGoodRequest(url)) return "К сожалению, по предоставленному штрихкоду не удалось найти никакой книги.\nЕсли на изображении со штрихкодом код ISBN отличен от самого штрихкода, то попробуй отправить мне ISBN!";

        Document doc = Jsoup.connect(url.toString()).get();
        String bookLink = "https://knigabook.com" + doc.getElementsByClass("product__title ellipsis").attr("href");

        doc = Jsoup.connect(bookLink).get();

        StringBuilder resultBuilder = new StringBuilder();
        List<String> result = getBookParams(doc);

        for(int i = 0; i<result.size()-1; i++)
            resultBuilder.append(result.get(i)).append("\n\n");
        resultBuilder.append(result.get(result.size()-1));
        return resultBuilder.toString();
    }
    @Override
    public BufferedImage getImage(String isbn) throws IOException {
        Document doc = Jsoup.connect("https://knigabook.com/search?q="+isbn).get();
        String bookLink = "https://knigabook.com" + doc.getElementsByClass("product__title ellipsis").attr("href");

        doc = Jsoup.connect(bookLink).get();
        Elements cover = doc.getElementsByClass("product-cover__image lazy-img product-cover__image_big");
        URL imageLink = new URL(cover.select("img").attr("data-src"));
        return ImageIO.read(imageLink);
    }

    public List<String> getBookPrices (Document doc)
    {
        String shopStatus = doc.getElementsByClass("product__prices_big lead-mini").text();
        List<String> resultList = new ArrayList<>();
        resultList.add(shopStatus);
        if (shopStatus.equals("Нет в наличии ни в одном магазине"))
            return resultList;
        Elements bookNames = doc.getElementsByClass("shops-item__name");
        Elements bookPrices = doc.getElementsByClass("shops-item__price");
        for (int i = 0; i<bookNames.size();i++)
            resultList.add(bookNames.get(i).text() + " - " + bookPrices.get(i).text() + "\n" + bookPrices.select("a").attr("href"));
        return resultList;
    }
    public List<String> getBookParams (Document doc)
    {
        String bookName = doc.getElementsByClass("product__info product__info_right").select("h1").text();
        List<String> bookParams = new ArrayList<>();
        bookParams.add(bookName);

        Elements divs = doc.getElementsByClass("product__text product__text_big");
        for (Element div : divs)
            bookParams.add(div.text());
        return bookParams.subList(0, bookParams.size()-1);
    }
}
