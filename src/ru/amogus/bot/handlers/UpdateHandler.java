package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotRequest;

import java.io.IOException;

public interface UpdateHandler {
    boolean validate(Update update);
    SendMessage handle(Update update) throws IOException;
}
