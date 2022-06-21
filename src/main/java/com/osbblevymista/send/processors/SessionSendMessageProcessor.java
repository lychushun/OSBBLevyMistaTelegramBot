package com.osbblevymista.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.filereaders.AuthFileReader;
import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageParams;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Messages;
import com.osbblevymista.system.SessionAttributes;
import com.osbblevymista.system.SessionProperties;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SessionSendMessageProcessor {

    private final SendingMessageProcessor sendingMessageProcessor;
    private final AppealSendMessageProcessor appealSendMessageProcessor;
    private final ActionSendMessageProcessor actionSendMessageProcessor;

    private final AuthFileReader authFileReader;

    public List<Function<Message, OSBBSendMessage>> processSession(String message, SendMessageParams sendMessageParam, Optional<Session> optional) throws IOException, URISyntaxException, CsvException {

        if (optional.isPresent()) {
            Session session = optional.get();
            if (Objects.nonNull(session.getAttribute("reading"))) {
                return reading(message, sendMessageParam, session);
            } else {
                return markingAsReading(message, sendMessageParam, session);
            }
        }
        return null;
    }

    private List<Function<Message, OSBBSendMessage>> markingAsReading(String message, SendMessageParams sendMessageParam, Session session) throws IOException {
        if (Objects.equals(message, Actions.BUTTON_LOGIN.getText())) {
            session.setAttribute("reading", SessionProperties.INSERTING_LOGIN);
            return new ArrayList<>();
        } else if (Objects.equals(message, Actions.BUTTON_PASS.getText())) {
            session.setAttribute("reading", SessionProperties.INSERTING_PASS);
            return new ArrayList<>();
        } else if (Objects.equals(message, Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText())) {
            session.setAttribute("reading", SessionProperties.CREATING_SIMPLE_APPEAL);
            return null;
        } else if (Objects.equals(message, Actions.BUTTON_APPEAL_URGENT_CREATE.getText())) {
            session.setAttribute("reading", SessionProperties.CREATING_URGENT_APPEAL);
            return null;
        } else if (Objects.equals(message, Actions.BUTTON_ADMIN_SEND.getText())) {
            session.setAttribute("reading", SessionProperties.SENDING_MESSAGE_TO_ALL);
            return new ArrayList<>();
        } else if (Objects.equals(message, Actions.BUTTON_ADMIN_NEW_RECEIPT.getText())) {
            return sendingMessageProcessor.sendMessageInfoAboutReceipt();
        }
        return null;
    }

    private List<Function<Message, OSBBSendMessage>> reading(String message, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException, CsvException {

        if (isClickBack(message)) {
            session.setAttribute("reading", null);
        } else {
            if (session.getAttribute("reading") == SessionProperties.INSERTING_LOGIN
                    && !Objects.equals(message, Actions.BUTTON_LOGIN.getText())) {
                String pass = (String) session.getAttribute(SessionAttributes.PASS);
                if (StringUtils.isNotEmpty(pass)) {
                    authFileReader.add(sendMessageParam.getUserId(), pass, message);
                }

                session.setAttribute(SessionAttributes.LOGIN, message);
                session.setAttribute("reading", null);
                List<Function<Message, OSBBSendMessage>> list = new ArrayList<Function<Message, OSBBSendMessage>>();
                list.add(actionSendMessageProcessor.createSimpleMessage(sendMessageParam, Messages.SUCCESS_INSERT_LOGIN.getMessage()));
                return list;
            } else if (session.getAttribute("reading") == SessionProperties.INSERTING_PASS
                    && !Objects.equals(message, Actions.BUTTON_PASS.getText())) {
                String login = (String) session.getAttribute(SessionAttributes.LOGIN);
                if (StringUtils.isNotEmpty(login)) {
                    authFileReader.add(sendMessageParam.getUserId(), message, login);
                }

                session.setAttribute(SessionAttributes.PASS, message);
                session.setAttribute("reading", null);
                List<Function<Message, OSBBSendMessage>> list = new ArrayList<Function<Message, OSBBSendMessage>>();
                list.add(actionSendMessageProcessor.createSimpleMessage(sendMessageParam, Messages.SUCCESS_INSERT_PASS.getMessage()));
                return list;
            } else if (session.getAttribute("reading") == SessionProperties.CREATING_SIMPLE_APPEAL
                    && !isClickedOnAppeal(message)) {
                var res = appealSendMessageProcessor.createSimpleAppeal(sendMessageParam, message);
                session.setAttribute("reading", null);
                return res;
            } else if (session.getAttribute("reading") == SessionProperties.CREATING_URGENT_APPEAL
                    && !isClickedOnAppeal(message)) {
                var res = appealSendMessageProcessor.createUrgentAppeal(sendMessageParam, message);
                session.setAttribute("reading", null);
                return res;
            } else if (session.getAttribute("reading") == SessionProperties.SENDING_MESSAGE_TO_ALL
                    && !isClickedOnAppeal(message)) {
                var res = sendingMessageProcessor.sendMessage(sendMessageParam, message);
                session.setAttribute("reading", null);
                return res;
            }
        }
        return null;

    }

    private boolean isClickBack(String message) {
        return Objects.equals(message, Actions.BUTTON_BACK.getText());
    }

    private boolean isClickedOnAppeal(String message) {
        return Objects.equals(message, Actions.BUTTON_APPEAL_URGENT_CREATE.getText())
                || Objects.equals(message, Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText())
                || Objects.equals(message, Actions.BUTTON_APPEAL_REVIEW.getText());
    }

}
