package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.dto.telegrammessage.*;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.osbblevymista.telegram.utils.IteratorUtils.withCounter;

@Component
public class SendingMessageProcessor extends MessageProcessor {

    final Logger logger = LoggerFactory.getLogger(SendingMessageProcessor.class);

    @Autowired
    private SendMessageBuilder sendMessageBuilder;

    @Autowired
    private UserInfoFileReader userInfoFileReader;

    @Autowired
    private ChanelMessengerService chanelMessengerService;

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

            BotExecutionObject botExecutionObject = new BotExecutionObject();
            botExecutionObject.setExecution(sendMessageBuilder.createMessageDelay(sendMessageParams, messageStr));
            osbbSendMessages.add(createBotExecutionObjectDelay(sendMessageParams, messageStr));
            list.addAll(osbbSendMessages);
        });

        chanelMessengerService.sendMessageToTereveni(messageStr);
        list.add(createBotExecutionObject(sendMessageParam,
                Messages.SENT_MESSAGE.format((userInfoList.size() + 1 - errorChatIds.size()) + "", userInfoList.size() + 1 + ""))
        );

        return list;
    }

    public List<BotExecutionObject> sendMessage(SendMessageParams sendMessageParam, List<TelegramMessage> messages) throws IOException {

        List<BotExecutionObject> list = new ArrayList<>();
        list.add(new BotExecutionObject(sendMessagesToTereveni(sendMessageParam, messages)));

        List<String> errorChatIds = new ArrayList<>();

        List<UserInfo> userInfoList = userInfoFileReader
                .getAll()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        userInfoList.forEach(withCounter((i, item) -> {
                    SendMessageParams sendMessageParams = SendMessageParams
                            .builder()
                            .chatId(Long.valueOf(item.getChatId()))
                            .build();

                    int index = i + 1;
                    messages.forEach(message -> {
                        if (Objects.equals(message.getType(), StrTelegramMessage.TYPE)) {
                            list.add(createBotExecutionObjectDelay(sendMessageParams, (String) message.getContent()));
                        } else if (Objects.equals(message.getType(), PhotoTelegramMessage.TYPE)) {
                            list.add(
                                    new BotExecutionObject(
                                            createPhotoBotExecutionObject(sendMessageParams, (PhotoSize) message.getContent(), index)
                                    )
                            );
                        } else if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
                            list.add(
                                    new BotExecutionObject(
                                            createDocumentBotExecutionObject(sendMessageParams, (Document) message.getContent(), index)
                                    )
                            );
                        } else if (Objects.equals(message.getType(), VideoTelegramMessage.TYPE)) {
                            list.add(
                                    new BotExecutionObject(
                                            createVideoBotExecutionObject(sendMessageParams, (Video) message.getContent(), index)
                                    )
                            );
                        } else if (Objects.equals(message.getType(), AudioTelegramMessage.TYPE)) {
                            list.add(
                                    new BotExecutionObject(
                                            createAudioBotExecutionObject(sendMessageParams, (Audio) message.getContent(), index)
                                    )
                            );
                        }
                    });
                })
        );

        try {


            list.add(new BotExecutionObject(
                    sendAmountOfMessagesToBoard(sendMessageParam, userInfoList.size(), errorChatIds.size())
            ));

        } catch (UnsupportedEncodingException | URISyntaxException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        return list;
    }

    private Function<Message, PartialBotApiMethod<Message>> sendMessagesToTereveni(SendMessageParams sendMessageParam, List<TelegramMessage> messages) {
        return message -> {
            logger.info("Sending {} messages to tereveni.", messages.size());
            chanelMessengerService.sendMessageToTereveni(messages);
            return sendMessageBuilder.createSimpleMessage(sendMessageParam,Messages.STILL_SENDING.getMessage());
        };

    }

    private Function<Message, PartialBotApiMethod<Message>> sendAmountOfMessagesToBoard(SendMessageParams sendMessageParam, Integer userSize, Integer errorSize) throws UnsupportedEncodingException, URISyntaxException {
        return message -> {
            return sendMessageBuilder.createSimpleMessage(
                    sendMessageParam,
                    Messages.SENT_MESSAGE.format((userSize + 1 - errorSize) + "", userSize + 1 + ""));
        };
    }

    private Function<Message, PartialBotApiMethod<Message>> createPhotoBotExecutionObject(SendMessageParams sendMessageParams, PhotoSize photoSize, int number) {
        return message -> {
            try {
                logger.info("Sending photo to user # {}.", number);
                return sendMessageBuilder.createMessageDelay(sendMessageParams, photoSize);
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        };
    }

    private Function<Message, PartialBotApiMethod<Message>> createAudioBotExecutionObject(SendMessageParams sendMessageParams, Audio audio, int number) {
        return message -> {
            try {
                logger.info("Sending audio to user # {}.", number);
                return sendMessageBuilder.createMessageDelay(sendMessageParams, audio);
            } catch (IOException | URISyntaxException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        };
    }

    private Function<Message, PartialBotApiMethod<Message>> createVideoBotExecutionObject(SendMessageParams sendMessageParams, Video video, int number) {
        return message -> {
            try {
                logger.info("Sending video to user # {}.", number);
                return sendMessageBuilder.createMessageDelay(sendMessageParams, video);
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        };
    }

    private Function<Message, PartialBotApiMethod<Message>> createDocumentBotExecutionObject(SendMessageParams sendMessageParams, Document document, int number) {
        return message -> {
            try {
                logger.info("Sending document to user # {}.", number);
                return sendMessageBuilder.createMessageDelay(sendMessageParams, document);
            } catch (IOException | URISyntaxException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        };
    }

    List<BotExecutionObject> sendMessageInfoAboutReceipt(SendMessageParams sendMessageParam) throws IOException {
        return this.sendMessage(sendMessageParam, Messages.NEW_RECEIPT_INFO.getMessage());
    }

}
