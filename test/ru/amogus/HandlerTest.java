package ru.amogus;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class HandlerTest {
    @Test
    public void helloTest() throws IOException {
        Handler handler = new Handler();
        String str = handler.hello();
        Assert.assertEquals("Привет!\n" +
                "Чтобы ознакомиться с функционалом бота, отправь /help!", str);
    }
    @Test
    public void helpTest() throws IOException {
        Handler handler = new Handler();
        String str = handler.help();
        Assert.assertEquals("Список доступных команд:\n" + "/randomPoem - случайный экземпляр из коллекции русской поэзии!\n" +
                "/stop - завершить обработку команд.", str);
    }
    @Test
    public void stopTest() throws IOException {
        Handler handler = new Handler();
        String str = handler.stop();
        Assert.assertEquals("До встречи!", str);
    }
    @Test
    public void getPoemTest() throws IOException {
        Poem poem = new Poem();
        String str = poem.getPoem();
        Assert.assertEquals("Я помню чудное мгновенье!", str);
    }



}
