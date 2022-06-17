package com.osbblevymista.send.processors;

import com.osbblevymista.filereaders.UserInfoFileReader;
import com.osbblevymista.models.UserInfo;
import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageBuilder;
import com.osbblevymista.send.SendMessageParams;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendingMessageProcessor {

    private final SendMessageBuilder sendMessageBuilder;

    public List<Function<Message, OSBBSendMessage>> sendMessage(SendMessageParams sendMessageParam, String message) throws IOException {

        List<Function<Message, OSBBSendMessage>> messages = new ArrayList<>();

        UserInfoFileReader userInfoFileReader = UserInfoFileReader.createInstance();
        List<UserInfo> userInfoList = userInfoFileReader
                .get()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        userInfoList.forEach(item -> {
            Function<Message, OSBBSendMessage> function = new Function<Message, OSBBSendMessage>() {
                @SneakyThrows
                @Override
                public OSBBSendMessage apply(Message message) {
                    SendMessageParams sendMessageParams = SendMessageParams
                            .builder()
                            .chatId(Long.valueOf(item.getChatId()))
                            .build();

                    return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, message.getText());
                }
            };
            messages.add(function);
        });

        return messages;
    }

}
