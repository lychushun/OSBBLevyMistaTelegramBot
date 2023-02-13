package com.osbblevymista.telegram.services;

import com.osbblevymista.BotExecutionData;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.MainPage;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.send.processors.ActionSendMessageProcessor;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class BotExecutionDataService {

    private final ActionSendMessageProcessor actionSendMessageProcessor;

    public BotExecutionData generateStartPage(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, Messages.START_NEW_PERSON.getMessage());
    }

    public BotExecutionData generateMainPage(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, "");
    }

    public BotExecutionData generateSuccessLogin(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, Messages.SENT_MESSAGE.getMessage());
    }

    public BotExecutionData generatePageOrAction(SendMessageParams sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {
        BotExecutionData botExecutionData = new BotExecutionData();
        botExecutionData.addExecutionsForMessage(processMessage(sendMessageParam, osbbKeyboardButton));
        return botExecutionData;
    }

    public BotExecutionData generateSinglePage(SendMessageParams sendMessageParams, MainPage mainPage, String message) throws UnsupportedEncodingException, URISyntaxException {
        OSBBKeyboardButton osbbKeyboardButton = new OSBBKeyboardButton();
        osbbKeyboardButton.setNextPage(
                mainPage
        );
        osbbKeyboardButton.messages.add(message);

        BotExecutionData botExecutionData = new BotExecutionData();
        botExecutionData.addExecutionsForMessage(processMessage(sendMessageParams, osbbKeyboardButton));
        return botExecutionData;
    }

    private List<OSBBSendMessage> processMessage(SendMessageParams sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {

        List<BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>> biFunctionList = actionSendMessageProcessor.createSendMessageList(osbbKeyboardButton);

        List<OSBBSendMessage> list = new ArrayList<>();
        biFunctionList.forEach(item -> {
            list.addAll(item.apply(sendMessageParam, osbbKeyboardButton));
        });

        return list;
    }


}
