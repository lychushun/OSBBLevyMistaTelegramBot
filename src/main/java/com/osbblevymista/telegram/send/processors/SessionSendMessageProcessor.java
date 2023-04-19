package com.osbblevymista.telegram.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.OSBBLevyMista45;
import com.osbblevymista.miydim.services.MiyDimService;
import com.osbblevymista.telegram.botexecution.BotExecution;
import com.osbblevymista.telegram.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.dto.SendMessageInfo;
import com.osbblevymista.telegram.dto.telegrammessage.*;
import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import com.osbblevymista.telegram.system.*;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.osbblevymista.telegram.system.Actions.*;
import static com.osbblevymista.telegram.system.Commands.SIMPLE_APPEAL;
import static com.osbblevymista.telegram.system.Commands.URGENT_APPEAL;
import static com.osbblevymista.telegram.system.Messages.*;
import static com.osbblevymista.telegram.system.SessionProperties.*;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.hasText;
import static org.thymeleaf.util.StringUtils.isEmpty;

@Component
public class SessionSendMessageProcessor extends MessageProcessor {

    final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

    @Autowired
    private SendingMessageProcessor sendingMessageProcessor;

    @Autowired
    private AppealSendMessageProcessor appealSendMessageProcessor;

    @Autowired
    private SendMessageBuilder sendMessageBuilder;

    @Autowired
    private MiyDimService miyDimService;

    public BotExecution processSession(SendMessageInfo sendMessageInfo, SendMessageParams sendMessageParam, Optional<Session> optional) throws IOException, URISyntaxException, CsvException {

        if (optional.isPresent()) {
            Session session = optional.get();
            if (nonNull(session.getAttribute("reading"))) {
                return reading(sendMessageInfo, sendMessageParam, session);
            } else {
                return markingAsReading(sendMessageInfo.getCommand(), sendMessageParam, session);
            }
        }
        return BotExecution.empty();
    }

    private BotExecution markingAsReading(String message, SendMessageParams sendMessageParam, Session session) throws IOException {
        BotExecution botExecutionData = new BotExecution();
        if (Objects.equals(message, BUTTON_APPEAL_SIMPLE_CREATE.getText())) {
            session.setAttribute("reading", CREATING_SIMPLE_APPEAL);
            return null;
        } else if (Objects.equals(message, BUTTON_APPEAL_URGENT_CREATE.getText())) {
            session.setAttribute("reading", CREATING_URGENT_APPEAL);
            return null;
        } else if (Objects.equals(message, BUTTON_ADMIN_SEND.getText())) {
            session.setAttribute("reading", SENDING_MESSAGE_TO_ALL);
            return null;
        } else if (Objects.equals(message, BUTTON_ADMIN_NEW_RECEIPT.getText())) {
            botExecutionData.addExecutionsForMessage(sendingMessageProcessor.sendMessageInfoAboutReceipt(sendMessageParam));
            return botExecutionData;
        }
        return null;
    }

    private BotExecution reading(SendMessageInfo sendMessageInfo, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException, CsvException {
        if (isClickBack(sendMessageInfo.getCommand())) {
            session.setAttribute("reading", null);
            return null;
        } else if (isClickCancel(sendMessageInfo.getCommand())) {
            if (session.getAttribute("reading").equals(CREATING_SIMPLE_APPEAL)) {
                session.setAttribute("reading", CREATING_SIMPLE_APPEAL);
                session.setAttribute(MIY_DIM_APPEAL_INFO, null);

                String command = SIMPLE_APPEAL.getCommand();
                BotExecution botExecution = displayMiyDimLoginMessage(sendMessageParam, command);
                if (botExecution == null) {
                    botExecution = processCancelMessageAppeal(sendMessageParam, AppealTypes.SIMPLE);
                }
                return botExecution;
            } else if (session.getAttribute("reading").equals(CREATING_URGENT_APPEAL)) {
                session.setAttribute("reading", CREATING_URGENT_APPEAL);
                session.setAttribute(MIY_DIM_APPEAL_INFO, null);

                String command = URGENT_APPEAL.getCommand();
                BotExecution botExecution = displayMiyDimLoginMessage(sendMessageParam, command);
                if (botExecution == null) {
                    botExecution = processCancelMessageAppeal(sendMessageParam, AppealTypes.URGENT);
                }
                return botExecution;
            } else if (session.getAttribute("reading").equals(SENDING_MESSAGE_TO_ALL)) {
                session.setAttribute("reading", SENDING_MESSAGE_TO_ALL);
                session.setAttribute(SEND_MESSAGE_INFO, null);

                return processCancelMessageToAll(sendMessageParam);
            }
        } else {
            if (session.getAttribute("reading") == CREATING_SIMPLE_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, sendMessageInfo, AppealTypes.SIMPLE);
            } else if (session.getAttribute("reading") == CREATING_URGENT_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, sendMessageInfo, AppealTypes.URGENT);
            } else if (session.getAttribute("reading") == SENDING_MESSAGE_TO_ALL) {
                return processSendMessageToAll(session, sendMessageParam, sendMessageInfo);
            }
        }
        return BotExecution.empty();
    }

    private BotExecution displayMiyDimLoginMessage(SendMessageParams sendMessageParam, String message) {
        String cookie = miyDimService.getCookie(sendMessageParam.getChatIdAsString());
        if (!hasText(cookie)) {
            BotExecution botExecutionData = new BotExecution();
            botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam, message)));
            return botExecutionData;
        }
        return null;
    }

    private BotExecution processCancelMessageToAll(SendMessageParams sendMessageParam) {
        BotExecution botExecutionData = new BotExecution();
        botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, CANCEL_SENDING_MESSAGE_GENERAL.getMessage()));
        botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, CANCEL_SENDING_MESSAGE_2.getMessage()));
        return botExecutionData;
    }

    private BotExecution processCancelMessageAppeal(SendMessageParams sendMessageParam, AppealTypes type) {
        BotExecution botExecutionData = new BotExecution();
        botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, CANCEL_SENDING_MESSAGE_GENERAL.getMessage()));
        if (AppealTypes.SIMPLE.equals(type)) {
            botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, CANCEL_SENDING_MESSAGE_APPEAL_SIMPLE.getMessage()));
        } else if (AppealTypes.URGENT.equals(type)) {
            botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, CANCEL_SENDING_MESSAGE_APPEAL_URGENT.getMessage()));
        }
        return botExecutionData;
    }

    private BotExecution processSendMessageToAll(Session session,
                                                 SendMessageParams sendMessageParam,
                                                 SendMessageInfo sendMessageInfo) throws IOException {

        BotExecution botExecutionData = new BotExecution();

        List<TelegramMessage> messages = new ArrayList<>();
        if (session.getAttribute(SEND_MESSAGE_INFO) != null) {
            messages = (List<TelegramMessage>) session.getAttribute(SEND_MESSAGE_INFO);
        }
        if (isComplete(sendMessageInfo.getCommand())) {
            if (messages.size() > 0) {
                session.setAttribute(SEND_MESSAGE_INFO, null);
                botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, Messages.SENDING_MESSAGE.getMessage()));
                botExecutionData.addExecutionsForMessage(sendingMessageProcessor.sendMessage(sendMessageParam, messages));
            } else {
                botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
            }
            return botExecutionData;
        } else {
            messages.addAll(getMessage(sendMessageInfo));
            session.setAttribute(SEND_MESSAGE_INFO, messages);
        }
        return botExecutionData;
    }

    private BotExecution processAppeal(Session session,
                                       SendMessageParams sendMessageParam,
                                       SendMessageInfo sendMessageInfo,
                                       AppealTypes appealTypes) {

        BotExecution botExecutionData = new BotExecution();
        List<TelegramMessage> messages = new ArrayList<>();

        if (session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO) != null) {
            messages = (List<TelegramMessage>) session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO);
        }
        if (isComplete(sendMessageInfo.getCommand())) {
            String cookie = miyDimService.getCookie(sendMessageParam.getChatIdAsString());
            if (hasText(cookie)) {
                if (messages.size() > 0) {
                    session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, null);

                    botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, Messages.CREATING_APPEAL.getMessage()));
                    BotExecutionObject botExecution = new BotExecutionObject();

                    botExecution.setExecution(appealSendMessageProcessor.createAppeal(sendMessageParam, messages, appealTypes));
                    botExecutionData.addExecutionsForMessage(botExecution);
                } else {
                    botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
                }
            } else {
                String command = appealTypes == AppealTypes.URGENT ? URGENT_APPEAL.getCommand() : SIMPLE_APPEAL.getCommand();
                botExecutionData.addExecutionsForMessage(createBotExecutionObject(sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam, command)));
            }


            return botExecutionData;
        } else {
            messages.addAll(getMessage(sendMessageInfo));
            session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, messages);
        }
        return BotExecution.empty();
    }

    private boolean isClickBack(String message) {
        if (isEmpty(message)) {
            return false;
        }
        return Arrays.stream(InvisibleCharacters.values()).anyMatch(it ->
                Objects.equals(Actions.BUTTON_BACK.getText(), message.replaceAll(it.getVal(), ""))
        );
    }

    private boolean isClickCancel(String message) {
        if (isEmpty(message)) {
            return false;
        }
        return Arrays.stream(InvisibleCharacters.values()).anyMatch(it ->
                Objects.equals(BUTTON_SEND_MESSAGE_CANCEL.getText(), message.replaceAll(it.getVal(), ""))
        );
    }

    private boolean isClickedOnAppeal(String message) {
        return Objects.equals(message, Actions.BUTTON_APPEAL_URGENT_CREATE.getText())
                || Objects.equals(message, Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText())
                || Objects.equals(message, Actions.BUTTON_APPEAL_REVIEW.getText());
    }

    private boolean isComplete(String message) {
        return Objects.equals(message, Actions.BUTTON_APPEAL_SIMPLE_APPROVE.getText())
                || Objects.equals(message, Actions.BUTTON_APPEAL_URGENT_APPROVE.getText())
                || Objects.equals(message, Actions.BUTTON_SEND_MESSAGE_APPROVE.getText());
    }

    private List<TelegramMessage> getMessage(SendMessageInfo sendMessageInfo) {
        List<TelegramMessage> messages = new ArrayList<>();

        if (isNotEmpty(sendMessageInfo.getCommand())) {
            messages.add(new StrTelegramMessage(sendMessageInfo.getCommand()));
        }

        if (sendMessageInfo.hasPhoto()) {
            messages.add(new PhotoTelegramMessage(sendMessageInfo.getPhotoSize()));
        }

        if (sendMessageInfo.hasDocument()) {
            messages.add(new DocumentTelegramMessage(sendMessageInfo.getDocument()));
        }

        if (sendMessageInfo.hasVideo()) {
            messages.add(new VideoTelegramMessage(sendMessageInfo.getVideo()));
        }

        if (sendMessageInfo.hasAudio()) {
            messages.add(new AudioTelegramMessage(sendMessageInfo.getAudio()));
        }
        return messages;
    }
}
