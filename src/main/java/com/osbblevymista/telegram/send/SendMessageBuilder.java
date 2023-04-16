package com.osbblevymista.telegram.send;

import com.osbblevymista.telegram.keyabords.SettingsKeyboard;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.FileStorage;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendMessageBuilder {

    private final ChanelMessengerService chanelMessengerService;
    private final FileStorage fileStorage;

    @Value("${executionDelay}")
    private int injectedProperty;

    public OSBBStrMessage createSimpleMessage(SendMessageParams sendMessageParams, String text) throws
            UnsupportedEncodingException, URISyntaxException {

        OSBBStrMessage sendMessage = createBaseMessage(sendMessageParams.getChatId());

        sendMessage.setText(text);
        return sendMessage;
    }

    public OSBBPhotoMessage createPhotoMessage(SendMessageParams sendMessageParams, PhotoSize photoSize) throws
            IOException, URISyntaxException {

        OSBBPhotoMessage sendMessage = createBasePhotoMessage(sendMessageParams.getChatId());

        InputFile file = new InputFile();
        file.setMedia(chanelMessengerService.getFile(photoSize.getFileId()), photoSize.getFileUniqueId());

        sendMessage.setPhoto(file);

        return sendMessage;
    }

    public OSBBDocumentMessage createPhotoMessage(SendMessageParams sendMessageParams, Document document) throws
            IOException, URISyntaxException {

        OSBBDocumentMessage sendMessage = createBaseDocumentMessage(sendMessageParams.getChatId());

        InputFile file = new InputFile();
        file.setMedia(chanelMessengerService.getFile(document.getFileId()), document.getFileUniqueId());

        sendMessage.setDocument(file);

        return sendMessage;
    }

    public OSBBStrMessage createMessageExecutingDelay(SendMessageParams sendMessageParams, String text) throws
            UnsupportedEncodingException, URISyntaxException {

        OSBBStrMessage sendMessage = createSimpleMessage(sendMessageParams, text);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBPhotoMessage createMessageExecutingDelay(SendMessageParams sendMessageParams, PhotoSize photoSize) throws
            UnsupportedEncodingException, URISyntaxException {
        if (photoSize != null) {
            try {
                OSBBPhotoMessage sendMessage = createPhotoMessage(sendMessageParams, photoSize);
                sendMessage.setExecutingDelay(injectedProperty);
                return sendMessage;
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public OSBBDocumentMessage createMessageExecutingDelay(SendMessageParams sendMessageParams, Document document) throws
            UnsupportedEncodingException, URISyntaxException {
        if (document != null) {
            try {
                OSBBDocumentMessage sendMessage = createPhotoMessage(sendMessageParams, document);
                sendMessage.setExecutingDelay(injectedProperty);
                return sendMessage;
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public OSBBStrMessage createBaseMessage(Long chatId) {
        OSBBStrMessage sendMessage = new OSBBStrMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBPhotoMessage createBasePhotoMessage(Long chatId) {
        OSBBPhotoMessage sendMessage = new OSBBPhotoMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBDocumentMessage createBaseDocumentMessage(Long chatId) {
        OSBBDocumentMessage sendMessage = new OSBBDocumentMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBStrMessage createEmptyMessage(Long chatId) {
        OSBBStrMessage sendMessage = createBaseMessage(chatId);

        sendMessage.setText(Messages.DEFAULT_ANSWER.getMessage());
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBStrMessage generateMiyDimNotLoginMessage(SendMessageParams sendMessageParam) {
        return generateMiyDimNotLoginMessage(sendMessageParam, "");
    }

    public OSBBStrMessage generateMiyDimNotLoginMessage(SendMessageParams sendMessageParam, String command) {
        OSBBStrMessage sendMessage = createBaseMessage(sendMessageParam.getChatId());

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(SettingsKeyboard.generateLoginButton(
                sendMessageParam.getClientIp(),
                sendMessageParam.getClientPort(),
                sendMessageParam.getChatId().toString(),
                command
        ));

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                .keyboardRow(inlineKeyboardButtons)
                .build());
        sendMessage.setText(Messages.MISSING_COOKIE.getMessage());
        return sendMessage;
    }

    public OSBBStrMessage generateMiyDimAppealMessage(SendMessageParams sendMessageParam, String link) {
        OSBBStrMessage sendMessage = createBaseMessage(sendMessageParam.getChatId());

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(SettingsKeyboard.generateOrderButton(link));

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                .keyboardRow(inlineKeyboardButtons)
                .build());
        sendMessage.setText(Messages.LAST_APPEAL.getMessage());
        return sendMessage;
    }

    public OSBBStrMessage goHomeMessage(SendMessageParams sendMessageParam, String message) {
        OSBBStrMessage sendMessage = createBaseMessage(sendMessageParam.getChatId());

        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(SettingsKeyboard.generateHomeButton());

        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                .keyboardRow(inlineKeyboardButtons)
                .build());
        sendMessage.setText(message);
        return sendMessage;
    }

}
