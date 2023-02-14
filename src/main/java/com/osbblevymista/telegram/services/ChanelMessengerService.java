package com.osbblevymista.telegram.services;

import com.osbblevymista.telegram.miydim.MiyDimProcessor;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

        String url = generateMessageUrl(token, bordChatId, stringBuilder);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageByChatIdAsMessenger(String messageStr, String chatId) {
        String url = generateMessageUrl(token, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageByChatIdAsBot(String messageStr, String chatId) {
        String url = generateMessageUrl(bot, chatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    public void sendMessageToTereveni(String messageStr) {
        String url = generateMessageUrl(token, terevenyChatId, messageStr);
        restTemplate.getForObject(url, String.class);
    }

    private String generateMessageUrl(String bot, String chatId, String message) {
        return "https://api.telegram.org/bot" + bot + "/sendMessage?chat_id=" + chatId + "&parse_mode=HTML&text=" + message;
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

        String url = generateMessageUrl(token, bordChatId, stringBuilder);
        restTemplate.getForObject(url, String.class);
    }
}
