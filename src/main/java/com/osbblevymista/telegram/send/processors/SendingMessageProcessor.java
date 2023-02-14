package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SendingMessageProcessor {

    final Logger logger = LoggerFactory.getLogger(SendingMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final UserInfoFileReader userInfoFileReader;
    private final ChanelMessengerService chanelMessengerService;

    public List<BotExecutionObject> sendMessage(SendMessageParams sendMessageParam, String messageStr) throws IOException {

        List<BotExecutionObject> list = new ArrayList<>();
        List<String> errorChatIds = new ArrayList<>();

        List<UserInfo> userInfoList = userInfoFileReader
                .getAll()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        userInfoList.forEach(item -> {
            List<BotExecutionObject> osbbSendMessages = new ArrayList<>();
            SendMessageParams sendMessageParams = SendMessageParams
                    .builder()
                    .chatId(Long.valueOf(item.getChatId()))
                    .build();

            try {
                BotExecutionObject botExecutionObject = new BotExecutionObject();
                botExecutionObject.setExecution(sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, messageStr));
                osbbSendMessages.add(botExecutionObject);
            } catch (UnsupportedEncodingException | URISyntaxException e) {
                logger.warn("Notification was not sent to: " + item.getChatId());

                logger.error(e.getMessage(), e);
                errorChatIds.add(item.getChatId());
                e.printStackTrace();
            }
            list.addAll(osbbSendMessages);
        });

        BotExecutionObject osbbSendMessages = new BotExecutionObject();
        try {
            chanelMessengerService.sendMessageToTereveni(messageStr);
            osbbSendMessages.setExecution(sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.SENT_MESSAGE.format((userInfoList.size() + 1 - errorChatIds.size()) + "", userInfoList.size() + 1 + "")));
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

        list.add(osbbSendMessages);
        return list;
    }

    public List<BotExecutionObject> sendMessageInfoAboutReceipt(SendMessageParams sendMessageParam) throws IOException {
        return this.sendMessage(sendMessageParam, Messages.NEW_RECEIPT_INFO.getMessage());
    }

//    public List<BotExecutionObject> sendMessageInfoAboutReceipt( ) throws IOException {
//        List<BotExecutionObject> list = new ArrayList<>();
//
//        List<UserInfo> userInfoList = userInfoFileReader
//                .getAll()
//                .stream()
//                .filter(UserInfo::isSentNotifications)
//                .collect(Collectors.toList());
//
//        userInfoList.forEach(item -> {
//            List<BotExecutionObject> osbbSendMessages = new ArrayList<>();
//            SendMessageParams sendMessageParams = SendMessageParams
//                    .builder()
//                    .chatId(Long.valueOf(item.getChatId()))
//                    .build();
//
//            try {
//                BotExecutionObject botExecutionObject = new BotExecutionObject();
//                botExecutionObject.setExecution(sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, Messages.NEW_RECEIPT_INFO.getMessage()));
////                osbbSendMessages.add(sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, Messages.NEW_RECEIPT_INFO.getMessage()));
//                osbbSendMessages.add(botExecutionObject);
//            } catch (UnsupportedEncodingException | URISyntaxException e) {
//                e.printStackTrace();
//                logger.error(e.getMessage(), e);
//            }
//            list.addAll(osbbSendMessages);
//
//        });
//
//        BotExecutionObject osbbSendMessages = new BotExecutionObject();
//        try {
//            chanelMessengerService.sendMessageToTereveni(messageStr);
//            BotExecutionObject botExecutionObject = new BotExecutionObject();
//            botExecutionObject.setExecution(sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.SENT_MESSAGE.format((userInfoList.size() + 1 - errorChatIds.size()) + "", userInfoList.size() + 1 + "")));
//        } catch (UnsupportedEncodingException | URISyntaxException e) {
//            e.printStackTrace();
//            logger.error(e.getMessage(), e);
//        }
//
//        list.add(osbbSendMessages);
//
//        return list;
//    }

}
