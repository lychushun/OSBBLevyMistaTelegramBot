package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.dto.message.DocumentTelegramMessage;
import com.osbblevymista.telegram.dto.message.PhotoTelegramMessage;
import com.osbblevymista.telegram.dto.message.StrTelegramMessage;
import com.osbblevymista.telegram.dto.message.TelegramMessage;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.send.OSBBDocumentMessage;
import com.osbblevymista.telegram.send.OSBBPhotoMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.FileStorage;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

        list.add(osbbSendMessages);
        return list;
    }

    public List<BotExecutionObject> sendMessage(SendMessageParams sendMessageParam, List<TelegramMessage> messages) throws IOException {

        List<BotExecutionObject> list = new ArrayList<>();
        List<String> errorChatIds = new ArrayList<>();

        List<UserInfo> userInfoList = userInfoFileReader
                .getAll()
                .stream()
                .filter(UserInfo::isSentNotifications)
                .collect(Collectors.toList());

        userInfoList.forEach(item -> {
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
                        OSBBPhotoMessage osbbPhotoMessage = sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, (PhotoSize) message.getContent());

                        BotExecutionObject botExecutionObject = new BotExecutionObject();
                        botExecutionObject.setExecution(osbbPhotoMessage);
                        list.add(botExecutionObject);
                    } else if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
                        OSBBDocumentMessage osbbPhotoMessage = sendMessageBuilder.createMessageExecutingDelay(sendMessageParams, (Document) message.getContent());

                        BotExecutionObject botExecutionObject = new BotExecutionObject();
                        botExecutionObject.setExecution(osbbPhotoMessage);
                        list.add(botExecutionObject);
                    }
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    logger.warn("Notification was not sent to: " + item.getChatId());

                    logger.error(e.getMessage(), e);
                    errorChatIds.add(item.getChatId());
                    e.printStackTrace();
                }
            });
        });

        try {

            sendMessagesToTereveni(messages);
            BotExecutionObject osbbSendMessages = sendAmountOfMessagesToBoard(sendMessageParam, userInfoList.size(), errorChatIds.size());
            list.add(osbbSendMessages);

        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }

        return list;
    }

    public void sendMessagesToTereveni(List<TelegramMessage> messages) {
        messages.forEach(message -> {
            if (Objects.equals(message.getType(), StrTelegramMessage.TYPE)) {
                chanelMessengerService.sendMessageToTereveni((String) message.getContent());
            } else if (Objects.equals(message.getType(), PhotoTelegramMessage.TYPE)) {
                PhotoSize photoSizeList = (PhotoSize) message.getContent();

                if (photoSizeList != null) {
                    sendPhotoToTereveni(photoSizeList);
                }
            } else if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
                Document documentTelegramMessage = (Document) message.getContent();

                if (documentTelegramMessage != null) {
                    sendDocumentToTereveni(documentTelegramMessage);
                }
            }
        });
    }

    private void sendPhotoToTereveni(PhotoSize photo) {

        if (photo != null) {
            try {
                JSONObject jsonObject = chanelMessengerService.getFileInfo(photo.getFileId());
                String filePath = jsonObject.getString("file_path");
                InputStream inputStream = chanelMessengerService.getFileFromFilePath(filePath);

                String fileExtension = getExtensionByStringHandling(filePath);
                String tempFilePath = fileStorage.addFile(inputStream, photo.getFileUniqueId(), fileExtension);
                chanelMessengerService.sendPhotoToTereveni(tempFilePath);
                fileStorage.deleteFile(photo.getFileUniqueId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void sendDocumentToTereveni(Document document) {
        if (document != null) {
            try {
                JSONObject jsonObject = chanelMessengerService.getFileInfo(document.getFileId());
                String filePath = jsonObject.getString("file_path");
                InputStream inputStream = chanelMessengerService.getFileFromFilePath(filePath);

                String fileExtension = getExtensionByStringHandling(filePath);
                String tempFilePath = fileStorage.addFile(inputStream, document.getFileUniqueId(), fileExtension);
                chanelMessengerService.sendDocumentToTereveni(tempFilePath);
                fileStorage.deleteFile(document.getFileUniqueId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .get();
    }

    private BotExecutionObject sendAmountOfMessagesToBoard(SendMessageParams sendMessageParam, Integer userSize, Integer errorSize) throws UnsupportedEncodingException, URISyntaxException {
        BotExecutionObject osbbSendMessages = new BotExecutionObject();

        osbbSendMessages.setExecution(
                sendMessageBuilder.createSimpleMessage(
                        sendMessageParam,
                        Messages.SENT_MESSAGE.format((userSize + 1 - errorSize) + "", userSize + 1 + ""))
        );
        return osbbSendMessages;
    }

    public List<BotExecutionObject> sendMessageInfoAboutReceipt(SendMessageParams sendMessageParam) throws IOException {
        return this.sendMessage(sendMessageParam, Messages.NEW_RECEIPT_INFO.getMessage());
    }
}
