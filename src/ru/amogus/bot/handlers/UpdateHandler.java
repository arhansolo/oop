package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.amogus.bot.BotResponse;

import java.io.IOException;

public interface UpdateHandler {
    boolean validate(Update update);
    BotResponse handle(Update update) throws IOException;
}
