package ru.amogus.bot.parsers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class Parser {
    public abstract String getInformation (String instruction) throws IOException;
    public abstract BufferedImage getImage (String instruction) throws IOException;

    public boolean isGoodRequest(URL url) throws IOException {
        HttpURLConnection hook = (HttpURLConnection) url.openConnection();
        hook.setRequestMethod("HEAD");
        int responseCode = hook.getResponseCode();
        return responseCode == 200;
    }
}
