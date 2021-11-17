package ru.amogus.bot.handlers;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.amogus.bot.BotRequest;
import ru.amogus.bot.BotResponse;

import java.io.IOException;

public class CallbackQueryUpdate implements UpdateHandler {

    @Override
    public boolean validate(Update update) { return update.hasCallbackQuery(); }

    @Override
    public BotResponse handle(Update update) throws IOException, TelegramApiException {
        String chatId = Long.toString(update.getCallbackQuery().getMessage().getChatId());
        CallbackQuery callbackQuery = update.getCallbackQuery();
        BotRequest userAction = new BotRequest(null, null, null, callbackQuery);
        callbackQuery = userAction.getInputCallbackQuery();
        BotRequest request = new BotRequest(null, null, null, callbackQuery);

        return buildResponse(chatId, request);
    }
}
