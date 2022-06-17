package com.osbblevymista.send.processors;

import com.osbblevymista.miydim.AppealMiyDimProcessor;
import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageBuilder;
import com.osbblevymista.send.SendMessageParams;
import com.osbblevymista.system.Messages;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AppealSendMessageProcessor {

    private final SendMessageBuilder sendMessageBuilder;

    @Value("${admin.cahtid}")
    private String boardBotId;

    public List<Function<Message, OSBBSendMessage>> createSimpleAppeal(SendMessageParams sendMessageParam, String message) throws IOException, URISyntaxException {
        return createAppeal(sendMessageParam, message);
    }

    public List<Function<Message, OSBBSendMessage>> createUrgentAppeal(SendMessageParams sendMessageParam, String message) throws IOException, URISyntaxException {
        message = "ТЕРМІНОВО!!!\n" + message;
        return createAppeal(sendMessageParam, message);
    }

    private List<Function<Message, OSBBSendMessage>> createAppeal(SendMessageParams sendMessageParam, String message) throws IOException, URISyntaxException {
        AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(sendMessageParam.getLogin(), sendMessageParam.getPass());

        if (arrearsMiyDim.getIsLogin()) {
            boolean response = arrearsMiyDim.createAppeal(message);
            List<Function<Message, OSBBSendMessage>> list;
            if (response) {
                list = new ArrayList<>(createResponse(sendMessageParam, Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage()));
                list.add(createBoardNotification(sendMessageParam, message));
            } else {
                list = new ArrayList<>(createResponse(sendMessageParam, Messages.RESPONSE_ERROR_REQUEST_DATA_FOR_MYIDIM.getMessage()));
                list.add(createBoardNotification(sendMessageParam, message));
            }
            return list;
        } else {
            return createResponse(sendMessageParam,
                    arrearsMiyDim.getErrorMessage() + "\n" +
                            Messages.MISSING_LOG_AND_PASS.getMessage() + "\n");
        }
    }

    private Function<Message, OSBBSendMessage> createBoardNotification(SendMessageParams sendMessageParam, String text){
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
                }
                return null;
            }
        };
    }

    private List<Function<Message, OSBBSendMessage>> createResponse(SendMessageParams sendMessageParam, String text) throws UnsupportedEncodingException, URISyntaxException {

        List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
        list.add(new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {
                try {
                    return sendMessageBuilder.createSimpleMessage(sendMessageParam, text);
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        return list;
    }

}
