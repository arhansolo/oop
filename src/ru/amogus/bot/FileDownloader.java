package ru.amogus.bot;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class FileDownloader {
    final String token = new Bot().getBotToken();
    private String fileName;

    public void setFileName(String fileName)
    {
        this.fileName = "resources/barcode" + fileName.substring(fileName.indexOf("."), fileName.length());
    }
    public String getFileName()
    {
        return fileName;
    }

    public void uploadFile(String fileName, String fileId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot"+ token + "/getFile?file_id="+fileId);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String result = in.readLine();

        JSONObject jsonResult = new JSONObject(result);
        JSONObject path = jsonResult.getJSONObject("result");
        String filePath = path.getString("file_path");

        if (fileName.equals(""))
        {
            setFileName(filePath);
            fileName = getFileName();
        }

        URL downloadUrl = new URL("https://api.telegram.org/file/bot" + token + "/" + filePath);
        FileOutputStream fos = new FileOutputStream(fileName);

        ReadableByteChannel rbc = Channels.newChannel(downloadUrl.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
