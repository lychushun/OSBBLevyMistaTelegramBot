package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class BoardProcessor {

    private final Logger logger = LoggerFactory.getLogger(BoardProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;

    @Value("${admin.cahtid}")
    private String boardBotId;

    public Function<Message, OSBBSendMessage> createBoardNotification(SendMessageParams sendMessageParam, String text) {
        return new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {
                try {
                    SendMessageParams sendMessageParamsBoarNotification = SendMessageParams
                            .builder()
                            .chatId(Long.valueOf(boardBotId))
                            .build();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder
                            .append(Messages.NEW_MESSAGE_NOTIFICATION_BOARD.getMessage())
                            .append("\n")
                            .append(sendMessageParam.getFirstName())
                            .append(" ")
                            .append(sendMessageParam.getLastName())
                            .append(":")
                            .append(sendMessageParam.getUserName())
                            .append("\n")
                            .append(text);

                    return sendMessageBuilder.createSimpleMessage(sendMessageParamsBoarNotification, stringBuilder.toString());
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                return null;
            }
        };
    }


}
