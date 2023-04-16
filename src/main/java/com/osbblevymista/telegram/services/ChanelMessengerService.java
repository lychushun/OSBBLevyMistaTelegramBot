package com.osbblevymista.telegram.services;

import com.osbblevymista.telegram.miydim.MiyDimProcessor;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.io.InputStream;

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

    public void sendMessageToBord(SendMessageParams sendMessageParams, String messageStr) {

        String stringBuilder = Messages.NEW_MESSAGE_NOTIFICATION_BOARD.getMessage() +
                "\n" +
                sendMessageParams.getFirstName() +
                " " +
                sendMessageParams.getLastName() +
                ":" +
                sendMessageParams.getUserName() +
                "\n" +
                messageStr;

        String url = generateHTMLMessageUrl(token, bordChatId, stringBuilder);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageByChatIdAsMessenger(String messageStr, String chatId) {
        String url = generateHTMLMessageUrl(token, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageByChatIdAsBot(String messageStr, String chatId) {
        String url = generateHTMLMessageUrl(bot, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendCommandByChatIdAsBot(String messageStr, String chatId) {
        String url = generateSingleMessageUrl(bot, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageToTereveni(String messageStr) {
        String url = generateHTMLMessageUrl(token, terevenyChatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendPhotoToTereveni(String filePath) {
        String url = generatePhotoUrl(token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("photo", new FileSystemResource(filePath));
        body.add("chat_id", terevenyChatId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

    public void sendDocumentToTereveni(String filePath) {
        String url = generateSendDocumentUrl(token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("document", new FileSystemResource(filePath));
        body.add("chat_id", terevenyChatId);

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

    public void sendMessageAppealToBord(SendMessageParams sendMessageParam, String messageStr, AppealTypes appealTypes) {

        String stringBuilder = Messages.NEW_MESSAGE_NOTIFICATION_BOARD.getMessage() +
                "\n" +
                (appealTypes == AppealTypes.URGENT ? "<b>ТЕРМІНОВО!!!</b>" : "") +
                "\n" +
                sendMessageParam.getFirstName() +
                " " +
                sendMessageParam.getLastName() +
                ":" +
                sendMessageParam.getUserName() +
                "\n" +
                messageStr;

        String url = generateHTMLMessageUrl(token, bordChatId, stringBuilder);
        restTemplate.getForObject(url, String.class);
    }
}
