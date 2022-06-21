package com.osbblevymista.send.processors;

import com.osbblevymista.OSBBLevyMista45;
import com.osbblevymista.filereaders.UserInfoFileReader;
import com.osbblevymista.models.UserInfo;
import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageBuilder;
import com.osbblevymista.send.SendMessageParams;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Messages;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    public List<Function<Message, OSBBSendMessage>> sendMessage(SendMessageParams sendMessageParam, String message) throws IOException {
        List<Function<Message, OSBBSendMessage>> messages = new ArrayList<>();

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
                        return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, message.getText());
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
