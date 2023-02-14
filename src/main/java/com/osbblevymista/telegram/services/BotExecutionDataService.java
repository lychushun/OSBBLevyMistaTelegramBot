package com.osbblevymista.telegram.services;

import com.osbblevymista.botexecution.BotExecution;
import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.MainPage;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.send.processors.ActionSendMessageProcessor;
import com.osbblevymista.telegram.system.InvisibleCharacters;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotExecutionDataService {

    private final ActionSendMessageProcessor actionSendMessageProcessor;

    public BotExecution generateStartPage(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, Messages.START_NEW_PERSON.getMessage());
    }

    public BotExecution generateMainPage(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, InvisibleCharacters._200B.getVal());
    }

    public BotExecution generateSuccessLogin(SendMessageParams sendMessageParams, MainPage mainPage) throws IOException, URISyntaxException {
        return generateSinglePage(sendMessageParams, mainPage, Messages.SENT_MESSAGE.getMessage());
    }

    public BotExecution generatePageOrAction(SendMessageParams sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {
        BotExecution botExecutionData = new BotExecution();
        botExecutionData.addExecutionsForMessage(processMessage(sendMessageParam, osbbKeyboardButton));
        return botExecutionData;
    }

    public BotExecution generateSinglePage(SendMessageParams sendMessageParams, MainPage mainPage, String message) throws UnsupportedEncodingException, URISyntaxException {
        OSBBKeyboardButton osbbKeyboardButton = new OSBBKeyboardButton();
        osbbKeyboardButton.setNextPage(
                mainPage
        );
        osbbKeyboardButton.messages.add(message);

        BotExecution botExecutionData = new BotExecution();
        botExecutionData.addExecutionsForMessage(processMessage(sendMessageParams, osbbKeyboardButton));
        return botExecutionData;
    }

    private List<BotExecutionObject> processMessage(SendMessageParams sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {

        List<BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>>> biFunctionList = actionSendMessageProcessor.createSendMessageList(osbbKeyboardButton);

        return biFunctionList.stream().map( el -> {
            BotExecutionObject botExecutionObject = new BotExecutionObject();
            botExecutionObject.setExecution(sendMessageParam, osbbKeyboardButton, el);
            return botExecutionObject;
        }).collect(Collectors.toList());

    }

}
