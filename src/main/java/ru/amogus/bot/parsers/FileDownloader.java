package ru.amogus.bot.parsers;

import org.json.JSONObject;
import ru.amogus.bot.Bot;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;

@Nullable
public class FileDownloader {
    private static final Map<String, String> envParams = System.getenv();

    private final String token = new Bot(envParams.get("USERNAME"), envParams.get("TOKEN")).getBotToken();

    public BufferedImage getPhoto(String fileId) throws IOException {
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
