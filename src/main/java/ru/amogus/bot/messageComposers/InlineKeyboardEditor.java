package ru.amogus.bot.messageComposers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardEditor {
    public void setMarkupInline (String callbackData, String buttonText, String url, SendMessage textResponse, SendPhoto photoResponse) {
        InlineKeyboardMarkup markupInline = getMarkupInline(callbackData, buttonText, url);

        switch (callbackData) {
            case "updateText" ->
                    textResponse.setReplyMarkup(markupInline);
            case "updateCaption" ->
                    photoResponse.setReplyMarkup(markupInline);
        }
    }

    private InlineKeyboardMarkup getMarkupInline (String callbackData, String buttonText, String url) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton ikb = new InlineKeyboardButton();

        ikb.setCallbackData(callbackData);
        ikb.setText(buttonText);
        ikb.setUrl(url);

        rowInline.add(ikb);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }
}
