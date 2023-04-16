package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.dto.message.*;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.FileStorage;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.osbblevymista.telegram.utils.IteratorUtils.withCounter;

@Component
@RequiredArgsConstructor
public class SendingMessageProcessor {

    final Logger logger = LoggerFactory.getLogger(SendingMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final UserInfoFileReader userInfoFileReader;
    private final ChanelMessengerService chanelMessengerService;
    private final FileStorage fileStorage;

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
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

        list.add(osbbSendMessages);
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


                    messages.forEach(message -> {
                        try {
                            if (Objects.equals(message.getType(), StrTelegramMessage.TYPE)) {
                                BotExecutionObject botExecutionObject = new BotExecutionObject();
                                botExecutionObject.setExecution(sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, (String) message.getContent()));
                                list.add(botExecutionObject);
                            } else if (Objects.equals(message.getType(), PhotoTelegramMessage.TYPE)) {
                                list.add(
                                        new BotExecutionObject(
                                                createPhotoBotExecutionObject(sendMessageParams, (PhotoSize) message.getContent(), i)
                                        )
                                );
                            } else if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
                                list.add(
                                        new BotExecutionObject(
                                                createDocumentBotExecutionObject(sendMessageParams, (Document) message.getContent(), i)
                                        )
                                );
                            } else if (Objects.equals(message.getType(), VideoTelegramMessage.TYPE)) {
                                list.add(
                                        new BotExecutionObject(
                                                createVideoBotExecutionObject(sendMessageParams, (Video) message.getContent(), i)
                                        )
                                );
                            } else if (Objects.equals(message.getType(), AudioTelegramMessage.TYPE)) {
                                list.add(
                                        new BotExecutionObject(
                                                createAudioBotExecutionObject(sendMessageParams, (Audio) message.getContent(), i)
                                        )
                                );
                            }
                        } catch (URISyntaxException | IOException e) {
                            logger.warn("Notification was not sent to: " + item.getChatId());
                            logger.error(e.getMessage(), e);
                            errorChatIds.add(item.getChatId());
                            e.printStackTrace();
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

            messages.forEach(ms -> {
                try {
                    if (Objects.equals(ms.getType(), StrTelegramMessage.TYPE)) {
                        chanelMessengerService.sendMessageToTereveni((String) ms.getContent());
                    }
                    sendPhotoToTereveni(ms);
                    sendDocumentToTereveni(ms);
                    sendVideoToTereveni(ms);
                    sendAudioToTereveni(ms);

                } catch (IOException e) {
                    logger.error(e.getMessage(), "Can not send file to tereveny");
                    e.printStackTrace();
                }
            });
            try {
                return sendMessageBuilder.createSimpleMessage(sendMessageParam,Messages.STILL_SENDING.getMessage());
            } catch (UnsupportedEncodingException | URISyntaxException e) {
                logger.error(e.getMessage(), "Can not send info to bot about sending file to tereveny");
                e.printStackTrace();
            }
            return null;
        };

    }

    private void sendVideoToTereveni(TelegramMessage<Video> message) throws IOException {
        if (Objects.equals(message.getType(), VideoTelegramMessage.TYPE)) {
            Video video = message.getContent();

            if (video != null) {
                sendFileToTereveni(video.getFileId(), video.getFileUniqueId(), video.getFileName());
            }
        }
    }

    private void sendAudioToTereveni(TelegramMessage<Audio> message) throws IOException {
        if (Objects.equals(message.getType(), AudioTelegramMessage.TYPE)) {
            Audio audio = message.getContent();

            if (audio != null) {
                sendFileToTereveni(audio.getFileId(), audio.getFileUniqueId(), audio.getFileName());
            }
        }
    }

    private void sendPhotoToTereveni(TelegramMessage<PhotoSize> message) throws IOException {
        if (Objects.equals(message.getType(), PhotoTelegramMessage.TYPE)) {
            PhotoSize photoSize = message.getContent();

            if (photoSize != null) {
                sendFileToTereveni(photoSize.getFileId(), photoSize.getFileUniqueId(), null);
            }
        }
    }

    private void sendDocumentToTereveni(TelegramMessage<Document> message) throws IOException {
        if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
            Document documentTelegramMessage = message.getContent();

            if (documentTelegramMessage != null) {
                sendFileToTereveni(documentTelegramMessage.getFileId(),
                        documentTelegramMessage.getFileUniqueId(),
                        documentTelegramMessage.getFileName()
                );
            }
        }
    }

    private void sendFileToTereveni(String fileId, String uniqueId, String fileName) throws IOException {
        JSONObject jsonObject = chanelMessengerService.getFileInfo(fileId);
        String filePath = jsonObject.getString("file_path");
        InputStream inputStream = chanelMessengerService.getFileFromFilePath(filePath);

        String fileExtension = getExtensionByStringHandling(filePath);
        if (StringUtils.isEmpty(fileName)) {
            fileName = FilenameUtils.getName(filePath);
        }
        String tempFilePath = fileStorage.addFile(
                inputStream,
                uniqueId,
                getNameWithoutExtension(fileName),
                fileExtension
        );
        chanelMessengerService.sendDocumentToTereveni(tempFilePath);
        fileStorage.deleteFile(uniqueId);
    }

    private Function<Message, PartialBotApiMethod<Message>> sendAmountOfMessagesToBoard(SendMessageParams sendMessageParam, Integer userSize, Integer errorSize) throws UnsupportedEncodingException, URISyntaxException {
        return message -> {

            try {
                return sendMessageBuilder.createSimpleMessage(
                        sendMessageParam,
                        Messages.SENT_MESSAGE.format((userSize + 1 - errorSize) + "", userSize + 1 + ""));
            } catch (UnsupportedEncodingException | URISyntaxException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            return null;
        };
    }

    private Function<Message, PartialBotApiMethod<Message>> createPhotoBotExecutionObject(SendMessageParams sendMessageParams, PhotoSize photoSize, int number) {
        return message -> {
            try {
                logger.info("Sending photo to user # {}.", number);
                return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, photoSize);
            } catch (IOException | URISyntaxException e) {
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
                return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, audio);
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
                return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, video);
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
                return sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, document);
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

    private static String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .get();
    }

    private static String getNameWithoutExtension(String file) {
        int dotIndex = file.lastIndexOf('.');
        return (dotIndex == -1) ? file : file.substring(0, dotIndex);
    }
}
