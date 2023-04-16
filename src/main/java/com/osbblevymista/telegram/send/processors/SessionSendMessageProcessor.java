package com.osbblevymista.telegram.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.OSBBLevyMista45;
import com.osbblevymista.api.services.MiyDimService;
import com.osbblevymista.botexecution.BotExecution;
import com.osbblevymista.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.dto.MiyDimAppealInfo;
import com.osbblevymista.telegram.dto.SendMessageInfo;
import com.osbblevymista.telegram.dto.message.DocumentTelegramMessage;
import com.osbblevymista.telegram.dto.message.PhotoTelegramMessage;
import com.osbblevymista.telegram.dto.message.StrTelegramMessage;
import com.osbblevymista.telegram.dto.message.TelegramMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.*;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

import static com.osbblevymista.telegram.system.Commands.SIMPLE_APPEAL;
import static com.osbblevymista.telegram.system.Commands.URGENT_APPEAL;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.hasText;
import static org.thymeleaf.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class SessionSendMessageProcessor {

    final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

    private final SendingMessageProcessor sendingMessageProcessor;
    private final AppealSendMessageProcessor appealSendMessageProcessor;
    private final ActionSendMessageProcessor actionSendMessageProcessor;
    private final SendMessageBuilder sendMessageBuilder;
    private final FileStorage fileStorage;

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
            botExecutionData.addExecutionsForMessage(sendingMessageProcessor.sendMessageInfoAboutReceipt(sendMessageParam));
            return botExecutionData;
        } else if (Objects.equals(message, Actions.BUTTON_SEND_MESSAGE_CANCEL.getText())) {
            session.setAttribute(SessionProperties.SEND_MESSAGE_INFO, null);
            sendMessageBuilder.goHomeMessage(sendMessageParam, Messages.UNRECOGNIZED_COMMAND.getMessage());
            return botExecutionData;
        }
        return null;
    }

    private BotExecution reading(SendMessageInfo sendMessageInfo, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException, CsvException {
        if (isClickBack(sendMessageInfo.getCommand())) {
            session.setAttribute("reading", null);
            return null;
        } else {
            if (session.getAttribute("reading") == SessionProperties.CREATING_SIMPLE_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, sendMessageInfo.getCommand(), AppealTypes.SIMPLE);
            } else if (session.getAttribute("reading") == SessionProperties.CREATING_URGENT_APPEAL
            ) {
                return processAppeal(session, sendMessageParam, sendMessageInfo.getCommand(), AppealTypes.URGENT);
            } else if (session.getAttribute("reading") == SessionProperties.SENDING_MESSAGE_TO_ALL) {
                return processSendMessage(session, sendMessageParam, sendMessageInfo);
            }
        }
        return BotExecution.empty();
    }

    private final MiyDimService miyDimService;

    private BotExecution processSendMessage(Session session,
                                            SendMessageParams sendMessageParam,
                                            SendMessageInfo sendMessageInfo) throws IOException, URISyntaxException {

        BotExecution botExecutionData = new BotExecution();

        List<TelegramMessage> messages = new ArrayList<>();
        if (session.getAttribute(SessionProperties.SEND_MESSAGE_INFO) != null) {
            messages = (List<TelegramMessage>) session.getAttribute(SessionProperties.SEND_MESSAGE_INFO);
        }
        if (isComplete(sendMessageInfo.getCommand())) {

            if (messages.size() > 0) {
                session.setAttribute(SessionProperties.SEND_MESSAGE_INFO, null);

                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.SENDING_MESSAGE.getMessage()));
                botExecutionData.addExecutionsForMessage(sendingMessageProcessor.sendMessage(sendMessageParam, messages));
            } else {
                botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
            }
            return botExecutionData;
        } else {
            if (isNotEmpty(sendMessageInfo.getCommand())){
                messages.add(new StrTelegramMessage(sendMessageInfo.getCommand()));
            }

            if (sendMessageInfo.hasPhoto()){
                messages.add(new PhotoTelegramMessage(sendMessageInfo.getPhotoSize()));
            }

            if (sendMessageInfo.hasDocument()){
                messages.add(new DocumentTelegramMessage(sendMessageInfo.getDocument()));
            }

            session.setAttribute(SessionProperties.SEND_MESSAGE_INFO, messages);
        }
        return botExecutionData;
    }

    private BotExecution processAppeal(Session session,
                                       SendMessageParams sendMessageParam,
                                       String message,
                                       AppealTypes appealTypes) throws IOException, URISyntaxException {

        BotExecution botExecutionData = new BotExecution();
        MiyDimAppealInfo miyDimAppealInfo = new MiyDimAppealInfo();
        if (session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO) != null) {
            miyDimAppealInfo = (MiyDimAppealInfo) session.getAttribute(SessionProperties.MIY_DIM_APPEAL_INFO);
        }
        if (isComplete(message)) {

            String cookie = miyDimService.getCookie(sendMessageParam.getChatIdAsString());
            if (hasText(cookie)) {
                if (miyDimAppealInfo.getMessages().size() > 0) {
                    session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, null);

                    botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.CREATING_APPEAL.getMessage()));
                    BotExecutionObject botExecution = new BotExecutionObject();
                    botExecution.setExecution(appealSendMessageProcessor.createAppeal(sendMessageParam, miyDimAppealInfo.formatMessages(), appealTypes));
                    botExecutionData.addExecutionsForMessage(botExecution);
                } else {
                    botExecutionData.addExecutionsForMessage(createSimpleMessageList(sendMessageParam, Messages.INSERT_APPEAL.getMessage()));
                }
            } else {
                BotExecutionObject botExecutionObject = new BotExecutionObject();
                String command = appealTypes == AppealTypes.URGENT ? URGENT_APPEAL.getCommand() : SIMPLE_APPEAL.getCommand();
                botExecutionObject.setExecution(sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam, command));
                botExecutionData.addExecutionsForMessage(botExecutionObject);
            }


            return botExecutionData;
        } else {
            miyDimAppealInfo.getMessages().add(message);
            session.setAttribute(SessionProperties.MIY_DIM_APPEAL_INFO, miyDimAppealInfo);
        }
        return BotExecution.empty();
    }

    private BotExecutionObject createSimpleMessageList(SendMessageParams sendMessageParam, String text) {
        try {
            BotExecutionObject botExecutionObject = new BotExecutionObject();
            botExecutionObject.setExecution(sendMessageBuilder.createSimpleMessage(sendMessageParam, text));
            return botExecutionObject;
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private boolean isClickBack(String message) {
        if (isEmpty(message)){
            return false;
        }
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
