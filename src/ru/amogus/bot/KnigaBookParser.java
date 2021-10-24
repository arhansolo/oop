package ru.amogus.bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class KnigaBookParser {
    public String getInformation(String isbn) throws IOException {
        URL url = new URL("https://knigabook.com/search?q="+isbn);
        if (!isGoodRequest(url)) return "К сожалению, по предоставленному штрихкоду не удалось найти никакой книги.\nЕсли на изображении со штрихкодом код ISBN отличен от самого штрихкода, то попробуй отправить мне ISBN!";
        Document doc = Jsoup.connect(url.toString()).get();
        String bookLink = "https://knigabook.com" + doc.getElementsByClass("product__title ellipsis").attr("href");
        doc = Jsoup.connect(bookLink).get();
        String bookName = doc.getElementsByClass("product__info product__info_right").select("h1").text();
        List<String> bookParams = new ArrayList<>();
        bookParams.add(bookName);
        Elements divs = doc.getElementsByClass("product__text product__text_big");
        for (Element div : divs)
            bookParams.add(div.text());
        String result = "";

        for(int i = 0; i<bookParams.size()-2;i++)
            result += bookParams.get(i) + "\n\n";
        result += bookParams.get(bookParams.size()-2);
        return result;
    }

    public String downloadCover(String isbn) throws IOException {
        Document doc = Jsoup.connect("https://knigabook.com/search?q="+isbn).get();
        String bookLink = "https://knigabook.com" + doc.getElementsByClass("product__title ellipsis").attr("href");

        doc = Jsoup.connect(bookLink).get();
        Elements cover = doc.getElementsByClass("product-cover__image lazy-img product-cover__image_big");
        String imageLink = cover.select("img").attr("data-src");
        String imagePath = "resources/cover" + imageLink.substring(imageLink.lastIndexOf("."), imageLink.length());
        try
        {
            URL downloadUrl = new URL(imageLink);
            FileOutputStream fos = new FileOutputStream(imagePath);

            ReadableByteChannel rbc = Channels.newChannel(downloadUrl.openStream());
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return imagePath;
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            return null;
        }
    }

    public boolean isGoodRequest(URL url) throws IOException {
        HttpURLConnection hook = (HttpURLConnection) url.openConnection();
        hook.setRequestMethod("HEAD");
        int responseCode = hook.getResponseCode();
        return responseCode == 200;
    }

}
