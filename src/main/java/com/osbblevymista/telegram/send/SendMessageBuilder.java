package com.osbblevymista.telegram.send;

import com.osbblevymista.telegram.system.Messages;
import com.osbblevymista.telegram.system.MessagesParseMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

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

}
