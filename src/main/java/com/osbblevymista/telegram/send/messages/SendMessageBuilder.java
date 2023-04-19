package com.osbblevymista.telegram.send.messages;

import com.osbblevymista.telegram.view.keyabords.SettingsKeyboard;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

@Component
@RequiredArgsConstructor
public class SendMessageBuilder {

    private final ChanelMessengerService chanelMessengerService;

    @Value("${executionDelay}")
    private int injectedProperty;

    public OSBBStrMessage createSimpleMessage(SendMessageParams sendMessageParams, String text){
        OSBBStrMessage sendMessage = createBaseMessage(sendMessageParams.getChatId());
        sendMessage.setText(text);
        return sendMessage;
    }

    public OSBBStrMessage createMessageDelay(SendMessageParams sendMessageParams, String text){

        OSBBStrMessage sendMessage = createSimpleMessage(sendMessageParams, text);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBPhotoMessage createMessageDelay(SendMessageParams sendMessageParams, PhotoSize photoSize) throws IOException {
        if (photoSize != null) {
            OSBBPhotoMessage sendMessage = createBasePhotoMessage(sendMessageParams.getChatId());
            sendMessage.setPhoto(getInputFile(photoSize.getFileId(), photoSize.getFileUniqueId()));
            return sendMessage;
        }
        return null;
    }

    public OSBBAudioMessage createMessageDelay(SendMessageParams sendMessageParams, Audio audio) throws
            IOException, URISyntaxException {
        if (audio != null) {
            OSBBAudioMessage sendMessage = createBaseAudioMessage(sendMessageParams.getChatId());
            sendMessage.setAudio(getInputFile(audio.getFileId(), audio.getFileName()));
            return sendMessage;
        }
        return null;
    }

    public OSBBDocumentMessage createMessageDelay(SendMessageParams sendMessageParams, Document document) throws
            IOException, URISyntaxException {
        if (document != null) {
            OSBBDocumentMessage sendMessage = createBaseDocumentMessage(sendMessageParams.getChatId());
            sendMessage.setDocument(getInputFile(document.getFileId(), document.getFileName()));
            return sendMessage;
        }
        return null;
    }

    public OSBBVideoMessage createMessageDelay(SendMessageParams sendMessageParams, Video video) throws
            IOException {
        if (video != null) {
            OSBBVideoMessage sendMessage = createBaseVideoMessage(sendMessageParams.getChatId());
            sendMessage.setVideo(getInputFile(video.getFileId(), video.getFileName()));
            sendMessage.setWidth(video.getWidth());
            sendMessage.setHeight(video.getHeight());
            return sendMessage;
        }
        return null;
    }

    public OSBBStrMessage createBaseMessage(Long chatId) {
        OSBBStrMessage sendMessage = new OSBBStrMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);

        return sendMessage;
    }

    public OSBBPhotoMessage createBasePhotoMessage(Long chatId) {
        OSBBPhotoMessage sendMessage = new OSBBPhotoMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBAudioMessage createBaseAudioMessage(Long chatId) {
        OSBBAudioMessage sendMessage = new OSBBAudioMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBVideoMessage createBaseVideoMessage(Long chatId) {
        OSBBVideoMessage sendMessage = new OSBBVideoMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setExecutingDelay(injectedProperty);

        return sendMessage;
    }

    public OSBBDocumentMessage createBaseDocumentMessage(Long chatId) {
        OSBBDocumentMessage sendMessage = new OSBBDocumentMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setExecutingDelay(injectedProperty);

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

    private InputFile getInputFile(String fileId, String fileName) throws IOException {
        InputFile file = new InputFile();
        file.setMedia(chanelMessengerService.getFile(fileId), fileName);
        return file;
    }

}
