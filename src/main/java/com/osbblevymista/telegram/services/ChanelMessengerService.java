package com.osbblevymista.telegram.services;

import com.osbblevymista.telegram.dto.telegrammessage.*;
import com.osbblevymista.miydim.MiyDimProcessor;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.FileStorage;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChanelMessengerService {

    private final Logger logger = LoggerFactory.getLogger(MiyDimProcessor.class);

    @Value("${tereveni.cahtid}")
    private String terevenyChatId;

    @Value("${admin.cahtid}")
    private String bordChatId;

    @Value("${telegram.messenger.bot.token}")
    private String token;

    @Value("${telegram.bot.token}")
    private String bot;

    @Value("${telegram.bot.name}")
    private String name;

    private final RestTemplate restTemplate;
    private final FileStorage fileStorage;

    public void sendMessageByChatIdAsBot(String messageStr, String chatId) {
        String url = generateHTMLMessageUrl(bot, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageToChatId(String messageStr, String chatId) {
        String url = generateHTMLMessageUrl(token, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendFileToChatId(String filePath, String chatId) {
        String url = generateSendDocumentUrl(token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", new FileSystemResource(filePath));
        body.add("chat_id", chatId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public InputStream getFile(String fileId) throws IOException {
        JSONObject jsonObject = new JSONObject(restTemplate.getForObject(getFileInfoUrl(bot, fileId), String.class));
        String filePath = jsonObject.getJSONObject("result").getString("file_path");
        return restTemplate.getForObject(getFileUrl(bot, filePath), Resource.class).getInputStream();
    }

    public InputStream getFileFromFilePath(String filePath) throws IOException {
        return restTemplate.getForObject(getFileUrl(bot, filePath), Resource.class).getInputStream();
    }

    public JSONObject getFileInfo(String fileId) {
        JSONObject jsonObject = new JSONObject(restTemplate.getForObject(getFileInfoUrl(bot, fileId), String.class));
        return jsonObject.getJSONObject("result");
    }

    private String getFileUrl(String bot, String filePath) {
        return "https://api.telegram.org/file/bot" + bot + "/" + filePath;
    }

    private String getFileInfoUrl(String bot, String fileId) {
        return "https://api.telegram.org/bot" + bot + "/getFile?file_id=" + fileId;
    }

    private String generateHTMLMessageUrl(String bot, String chatId, String message) {
        return "https://api.telegram.org/bot" + bot + "/sendMessage?chat_id=" + chatId + "&parse_mode=HTML&text=" + message;
    }

    private String generatePhotoUrl(String bot) {
        return "https://api.telegram.org/bot" + bot + "/sendPhoto";
    }

    private String generateSendDocumentUrl(String bot) {
        return "https://api.telegram.org/bot" + bot + "/sendDocument";
    }

    private String generateSingleMessageUrl(String bot, String chatId, String command) {
        return "https://api.telegram.org/bot" + bot + "/sendMessage?chat_id=" + chatId + "&text=" + command;
    }

    public void sendMessageToTereveni(String messages) {
        sendMessageToChatId(messages, terevenyChatId);
    }

    public void sendMessageToTereveni(List<TelegramMessage> messages) {
        messages.forEach(ms -> {
            try {
                if (Objects.equals(ms.getType(), StrTelegramMessage.TYPE)) {
                    sendMessageToChatId((String) ms.getContent(), terevenyChatId);
                }
                sendPhotoToChatId(ms, terevenyChatId);
                sendDocumentToChatId(ms, terevenyChatId);
                sendVideoToChatId(ms, terevenyChatId);
                sendAudioToChatId(ms, terevenyChatId);

            } catch (IOException e) {
                logger.error(e.getMessage(), "Can not send file to tereveny");
                e.printStackTrace();
            }
        });
    }

    public void sendMessageAppealToBord(SendMessageParams sendMessageParam, List<TelegramMessage> messages, AppealTypes appealTypes) {
        Map<String, Long> counted = messages.stream()
                .filter(el -> !Objects.equals(el.getTypeShort(), StrTelegramMessage.SHORT_TYPE))
                .collect(Collectors.groupingBy(TelegramMessage::getTypeShort, Collectors.counting()));

        sendMessageToChatId(appealBoardHeader(sendMessageParam, appealTypes, counted), bordChatId);

        messages.forEach(ms -> {
            try {
                if (Objects.equals(ms.getType(), StrTelegramMessage.TYPE)) {
                    sendMessageToChatId((String) ms.getContent(), bordChatId);
                }
                sendPhotoToChatId(ms, bordChatId);
                sendDocumentToChatId(ms, bordChatId);
                sendVideoToChatId(ms, bordChatId);
                sendAudioToChatId(ms, bordChatId);

            } catch (IOException e) {
                logger.error(e.getMessage(), "Can not send file to tereveny");
                e.printStackTrace();
            }
        });
    }

    public String appealBoardHeader(SendMessageParams sendMessageParam, AppealTypes appealTypes, Map<String, Long> counters) {
        return Messages.NEW_MESSAGE_NOTIFICATION_BOARD.getMessage() +
                "\n" +
                (appealTypes == AppealTypes.URGENT ? "<b>ТЕРМІНОВО!!!</b>" : "") +
                "\n" +
                sendMessageParam.getFirstName() +
                " " +
                sendMessageParam.getLastName() +
                ":" +
                sendMessageParam.getUserName() +
                "\nПрикріплені файли: \n" +
                counters
                        .entrySet()
                        .stream()
                        .map(el -> el.getKey() + ": " + el.getValue())
                        .collect(Collectors.joining("\n"));
    }

    private void sendVideoToChatId(TelegramMessage<Video> message, String chatid) throws IOException {
        if (Objects.equals(message.getType(), VideoTelegramMessage.TYPE)) {
            Video video = message.getContent();

            if (video != null) {
                sendFileToChatId(video.getFileId(), video.getFileUniqueId(), video.getFileName(), chatid);
            }
        }
    }

    private void sendAudioToChatId(TelegramMessage<Audio> message, String chatid) throws IOException {
        if (Objects.equals(message.getType(), AudioTelegramMessage.TYPE)) {
            Audio audio = message.getContent();

            if (audio != null) {
                sendFileToChatId(audio.getFileId(), audio.getFileUniqueId(), audio.getFileName(), chatid);
            }
        }
    }

    private void sendPhotoToChatId(TelegramMessage<PhotoSize> message, String chatid) throws IOException {
        if (Objects.equals(message.getType(), PhotoTelegramMessage.TYPE)) {
            PhotoSize photoSize = message.getContent();

            if (photoSize != null) {
                sendFileToChatId(photoSize.getFileId(), photoSize.getFileUniqueId(), null, chatid);
            }
        }
    }

    private void sendDocumentToChatId(TelegramMessage<Document> message, String chatId) throws IOException {
        if (Objects.equals(message.getType(), DocumentTelegramMessage.TYPE)) {
            Document documentTelegramMessage = message.getContent();

            if (documentTelegramMessage != null) {
                sendFileToChatId(documentTelegramMessage.getFileId(),
                        documentTelegramMessage.getFileUniqueId(),
                        documentTelegramMessage.getFileName(),
                        chatId
                );
            }
        }
    }

    private void sendFileToChatId(String fileId, String uniqueId, String fileName, String chatId) throws IOException {
        JSONObject jsonObject = getFileInfo(fileId);
        String filePath = jsonObject.getString("file_path");
        InputStream inputStream = getFileFromFilePath(filePath);

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
        sendFileToChatId(tempFilePath, chatId);
        fileStorage.deleteFile(uniqueId);
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
