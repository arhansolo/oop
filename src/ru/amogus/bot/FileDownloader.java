package ru.amogus.bot;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class FileDownloader {
    final String token = new Bot().getBotToken();

    public BufferedImage getFile(String fileId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot"+ token + "/getFile?file_id="+fileId);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String result = in.readLine();

        JSONObject jsonResult = new JSONObject(result);
        JSONObject path = jsonResult.getJSONObject("result");
        String filePath = path.getString("file_path");

        URL downloadUrl = new URL("https://api.telegram.org/file/bot" + token + "/" + filePath);
        return ImageIO.read(downloadUrl);
    }
}
