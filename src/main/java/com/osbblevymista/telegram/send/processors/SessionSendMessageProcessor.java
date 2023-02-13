package com.osbblevymista.telegram.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.BotExecutionData;
import com.osbblevymista.OSBBLevyMista45;
import com.osbblevymista.telegram.dto.MiyDimAppealInfo;
import com.osbblevymista.telegram.dto.SendMessageInfo;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.*;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class SessionSendMessageProcessor {

    final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

    private final SendingMessageProcessor sendingMessageProcessor;
    private final AppealSendMessageProcessor appealSendMessageProcessor;
    private final ActionSendMessageProcessor actionSendMessageProcessor;
    private final SendMessageBuilder sendMessageBuilder;

    public BotExecutionData processSession(Message message, SendMessageParams sendMessageParam, Optional<Session> optional) throws IOException, URISyntaxException, CsvException {
        String messageStr = message.getText();
        List<PhotoSize> photoSizeList = message.getPhoto();

        if (isNotEmpty(messageStr)) {
            return processSessionAsMessageString(messageStr, sendMessageParam, optional);
        } else {
            return processSessionAsPhoto(photoSizeList, sendMessageParam, optional);
        }
    }

    protected BotExecutionData processSessionAsMessageString(String message, SendMessageParams sendMessageParam, Optional<Session> optional) throws IOException, URISyntaxException, CsvException {
        if (optional.isPresent()) {
            Session session = optional.get();
            if (nonNull(session.getAttribute("reading"))) {
                return reading(message, sendMessageParam, session);
            } else {
                return markingAsReading(message, sendMessageParam, session);
            }
        }
        return null;
    }

    protected BotExecutionData processSessionAsPhoto(List<PhotoSize> photoSizeList, SendMessageParams sendMessageParam, Optional<Session> optional) throws IOException, URISyntaxException, CsvException {
        if (optional.isPresent()) {
            Session session = optional.get();
            if (nonNull(session.getAttribute("reading"))) {
                return reading(photoSizeList, sendMessageParam, session);
            }
        }
        return null;
    }

    private BotExecutionData markingAsReading(String message, SendMessageParams sendMessageParam, Session session) throws IOException {
        BotExecutionData botExecutionData = new BotExecutionData();
        if (Objects.equals(message, Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText())) {
            session.setAttribute("reading", SessionProperties.CREATING_SIMPLE_APPEAL);
            return null;
        } else if (Objects.equals(message, Actions.BUTTON_APPEAL_URGENT_CREATE.getText())) {
            session.setAttribute("reading", SessionProperties.CREATING_URGENT_APPEAL);
            return null;
        } else if (Objects.equals(message, Actions.BUTTON_ADMIN_SEND.getText())) {
            session.setAttribute("reading", SessionProperties.SENDING_MESSAGE_TO_ALL);
            return null;
        } else if (Objects.equals(message, Actions.BUTTON_ADMIN_NEW_RECEIPT.getText())) {
            botExecutionData.addExecutionsForMessage( sendingMessageProcessor.sendMessageInfoAboutReceipt());
        }
        return null;
    }

    private BotExecutionData reading(String message, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException, CsvException {
        if (isClickBack(message)) {
            session.setAttribute("reading", null);
            return null;
        } else {
            if (session.getAttribute("reading") == SessionProperties.CREATING_SIMPLE_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, message, AppealTypes.SIMPLE);
            } else if (session.getAttribute("reading") == SessionProperties.CREATING_URGENT_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, message, AppealTypes.URGENT);
            } else if (session.getAttribute("reading") == SessionProperties.SENDING_MESSAGE_TO_ALL) {
                return processSendMessage(session, sendMessageParam, message);
            }
        }
        return BotExecutionData.empty();
    }

    private BotExecutionData processSendMessage(Session session,
                                                SendMessageParams sendMessageParam,
                                                String message) throws IOException, URISyntaxException {

        BotExecutionData botExecutionData = new BotExecutionData();

        SendMessageInfo sendMessageInfo = new SendMessageInfo();
        if (session.getAttribute(SessionProperties.SEND_MESSAGE_INFO) != null) {
            sendMessageInfo = (SendMessageInfo) session.getAttribute(SessionProperties.SEND_MESSAGE_INFO);
        }
        if (isComplete(message)) {
            if (sendMessageInfo.getMessages().size() > 0) {
                session.setAttribute(SessionProperties.SEND_MESSAGE_INFO, null);

                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.SENDING_MESSAGE.getMessage()));
                botExecutionData.addExecutionsForMessage(sendingMessageProcessor.sendMessage(sendMessageParam, sendMessageInfo.formatMessages()));
            } else {
                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
            }
            return botExecutionData;
        } else {
            sendMessageInfo.getMessages().add(message);
            session.setAttribute(SessionProperties.SEND_MESSAGE_INFO, sendMessageInfo);
        }
        return botExecutionData;
    }

    private BotExecutionData processAppeal(Session session,
                                           SendMessageParams sendMessageParam,
                                           String message,
                                           AppealTypes appealTypes) throws IOException, URISyntaxException {

        BotExecutionData botExecutionData = new BotExecutionData();
        MiyDimAppealInfo miyDimAppealInfo = new MiyDimAppealInfo();
        if (session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO) != null) {
            miyDimAppealInfo = (MiyDimAppealInfo) session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO);
        }
        if (isComplete(message)) {
            if (miyDimAppealInfo.getMessages().size() > 0) {
                session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, null);

                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.CREATING_APPEAL.getMessage()));
                botExecutionData.addExecutionsForMessage(appealSendMessageProcessor.createAppeal(sendMessageParam, miyDimAppealInfo.formatMessages(), appealTypes));
            } else {
                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
            }
            return botExecutionData;
        } else {
            miyDimAppealInfo.getMessages().add(message);
            session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, miyDimAppealInfo);
        }
        return BotExecutionData.empty();
    }

    private OSBBSendMessage createSimpleMessageList(SendMessageParams sendMessageParam, String text) {
        try {
            return sendMessageBuilder.createSimpleMessage(sendMessageParam, text);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private BotExecutionData reading(List<PhotoSize> photoSizeList, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException, CsvException {

//        if (session.getAttribute("reading") == SessionProperties.CREATING_SIMPLE_APPEAL
////                    && !isClickedOnAppeal(message)) {
//        ) {
//            var res = appealSendMessageProcessor.createSimpleAppeal(sendMessageParam, message);
//            return res;
//        } else if (session.getAttribute("reading") == SessionProperties.CREATING_URGENT_APPEAL
////                    && !isClickedOnAppeal(message)) {
//        ) {
//            var res = appealSendMessageProcessor.createUrgentAppeal(sendMessageParam, message);
//            return res;
//        }

        return BotExecutionData.empty();

    }

    private boolean isClickBack(String message) {
        return Arrays.stream(InvisibleCharacters.values()).anyMatch(it ->
                Objects.equals(Actions.BUTTON_BACK.getText(), message.replaceAll(it.getVal(), ""))
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
}
