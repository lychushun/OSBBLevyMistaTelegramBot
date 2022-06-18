package com.osbblevymista.send.processors;

import com.osbblevymista.send.OSBBSendMessage;
import com.osbblevymista.send.SendMessageParams;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Messages;
import com.osbblevymista.system.SessionAttributes;
import com.osbblevymista.system.SessionProperties;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SessionSendMessageProcessor {

    private final SendingMessageProcessor sendingMessageProcessor;
    private final AppealSendMessageProcessor appealSendMessageProcessor;

    public List<Function<Message, OSBBSendMessage>> processSession(String message, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException {

        if (Objects.nonNull(session.getAttribute("reading"))){
            return reading(message, sendMessageParam, session);
        } else {
            return markingAsReading(message, sendMessageParam, session);
        }

    }

    private List<Function<Message, OSBBSendMessage>> markingAsReading(String message, SendMessageParams sendMessageParam, Session session){
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
        }
        return null;
    }

    private List<Function<Message, OSBBSendMessage>> reading(String message, SendMessageParams sendMessageParam, Session session) throws IOException, URISyntaxException {

        if (session.getAttribute("reading") == SessionProperties.INSERTING_LOGIN) {
            session.setAttribute(SessionAttributes.LOGIN, message);
            session.setAttribute("reading", null);
            return appealSendMessageProcessor.createSimpleAppeal(sendMessageParam, Messages.SUCCESS_INSERT_LOGIN.getMessage());
        } else if (session.getAttribute("reading") == SessionProperties.INSERTING_PASS) {
            session.setAttribute(SessionAttributes.PASS, message);
            session.setAttribute("reading", null);
            return appealSendMessageProcessor.createSimpleAppeal(sendMessageParam, Messages.SUCCESS_INSERT_PASS.getMessage());
        } else if (session.getAttribute("reading") == SessionProperties.CREATING_SIMPLE_APPEAL) {
            var res = appealSendMessageProcessor.createSimpleAppeal(sendMessageParam, message);
            session.setAttribute("reading", null);
            return res;
        } else if (session.getAttribute("reading") == SessionProperties.CREATING_URGENT_APPEAL) {
            var res = appealSendMessageProcessor.createUrgentAppeal(sendMessageParam, message);
            session.setAttribute("reading", null);
            return res;
        } else if (session.getAttribute("reading") == SessionProperties.SENDING_MESSAGE_TO_ALL) {
            var res = sendingMessageProcessor.sendMessage(sendMessageParam, message);
            session.setAttribute("reading", null);
            return res;
        }
        return null;

    }


}
