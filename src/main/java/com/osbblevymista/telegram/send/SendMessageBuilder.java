package com.osbblevymista.telegram.send;

import com.osbblevymista.telegram.keyabords.SettingsKeyboard;
import com.osbblevymista.telegram.system.Messages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SendMessageBuilder {

    @Value("${executionDelay}")
    private int injectedProperty;

    public OSBBSendMessage createSimpleMessage(SendMessageParams sendMessageParams, String text) throws
            UnsupportedEncodingException, URISyntaxException {

        OSBBSendMessage sendMessage = createBaseMessage(sendMessageParams.getChatId());

        sendMessage.setText(text);
        return sendMessage;
    }

    public OSBBSendMessage createMessageExecutingDelay(SendMessageParams sendMessageParams, String text) throws
            UnsupportedEncodingException, URISyntaxException {

        OSBBSendMessage sendMessage = createSimpleMessage(sendMessageParams, text);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBSendMessage createBaseMessage(Long chatId) {
        OSBBSendMessage sendMessage = new OSBBSendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBSendMessage createEmptyMessage(Long chatId) {
        OSBBSendMessage sendMessage = createBaseMessage(chatId);

        sendMessage.setText(Messages.DEFAULT_ANSWER.getMessage());
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBSendMessage generateMiyDimNotLoginMessage(SendMessageParams sendMessageParam ){
        OSBBSendMessage sendMessage = createBaseMessage(sendMessageParam.getChatId());

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(SettingsKeyboard.generateLoginButton(
                sendMessageParam.getClientIp(),
                sendMessageParam.getClientPort(),
                sendMessageParam.getChatId().toString()
        ));

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                .keyboardRow(inlineKeyboardButtons)
                .build());
        sendMessage.setText(Messages.MISSING_COOKIE.getMessage());
        return sendMessage;
    }

    public OSBBSendMessage generateMiyDimAppealMessage(SendMessageParams sendMessageParam, String message, String link){
        OSBBSendMessage sendMessage = createBaseMessage(sendMessageParam.getChatId());

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(SettingsKeyboard.generateOrderButton(
                link
        ));

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                .keyboardRow(inlineKeyboardButtons)
                .build());
        sendMessage.setText(message + "\n\n" + Messages.LAST_APPEAL.getMessage());
        return sendMessage;
    }

}
