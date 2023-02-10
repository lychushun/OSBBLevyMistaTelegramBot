package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendingMessageProcessor {

    final Logger logger = LoggerFactory.getLogger(SendingMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final UserInfoFileReader userInfoFileReader;

    @Value("${tereveni.cahtid}")
    private String terevenyBotId;

    public List<Function<Message, OSBBSendMessage>> sendMessage(SendMessageParams sendMessageParam, String messageStr) throws IOException {
        List<Function<Message, OSBBSendMessage>> messages = new ArrayList<>();

        List<String> errorChatIds = new ArrayList<>();

        List<UserInfo> userInfoList = userInfoFileReader
                .getAll()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        UserInfo userInfoTereveny = new UserInfo();
        userInfoTereveny.setChatId(terevenyBotId);

        userInfoList.add(userInfoTereveny);

        userInfoList.forEach(item -> {
            Function<Message, OSBBSendMessage> function = new Function<Message, OSBBSendMessage>() {
                @Override
                public OSBBSendMessage apply(Message message) {
                    SendMessageParams sendMessageParams = SendMessageParams
                            .builder()
                            .chatId(Long.valueOf(item.getChatId()))
                            .build();

                    try {
                        return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, messageStr);
                    } catch (UnsupportedEncodingException | URISyntaxException e) {
                        logger.warn("Notification was not sent to: " + item.getChatId());

                        logger.error(e.getMessage(), e);
                        errorChatIds.add(item.getChatId());
                        e.printStackTrace();
                    }
                    return null;
                }
            };
            messages.add(function);
        });

        messages.add(  new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {

                try {
                    return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.SENT_MESSAGE.format((userInfoList.size() - errorChatIds.size()) + "", userInfoList.size() + ""));
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
                return null;
            }
        });


        return messages;
    }

    public List<Function<Message, OSBBSendMessage>> sendMessageInfoAboutReceipt() throws IOException {
        List<Function<Message, OSBBSendMessage>> messages = new ArrayList<>();

        List<UserInfo> userInfoList = userInfoFileReader
                .getAll()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        userInfoList.forEach(item -> {
            Function<Message, OSBBSendMessage> function = new Function<Message, OSBBSendMessage>() {
                @Override
                public OSBBSendMessage apply(Message message) {
                    SendMessageParams sendMessageParams = SendMessageParams
                            .builder()
                            .chatId(Long.valueOf(item.getChatId()))
                            .build();

                    try {
                        return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, Messages.NEW_RECEIPT_INFO.getMessage());
                    } catch (UnsupportedEncodingException | URISyntaxException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    }
                    return null;
                }
            };

            messages.add(function);
        });

        return messages;
    }

}
