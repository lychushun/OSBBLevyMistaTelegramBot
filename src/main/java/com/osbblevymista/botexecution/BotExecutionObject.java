package com.osbblevymista.botexecution;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageParams;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BotExecutionObject {

    private Function<Message, List<OSBBSendMessage>> execution;
    private BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>> biExecution;

    private SendMessageParams sendMessageParams;
    private OSBBKeyboardButton osbbKeyboardButton;

    public void setExecution(Function<Message, List<OSBBSendMessage>> ex) {
        this.execution = ex;
        this.biExecution = null;
        this.sendMessageParams = null;
        this.osbbKeyboardButton = null;
    }

    public void setExecution(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton, BiFunction<SendMessageParams, OSBBKeyboardButton, List<OSBBSendMessage>> bi) {
        this.biExecution = bi;
        this.sendMessageParams = sendMessageParams;
        this.osbbKeyboardButton = osbbKeyboardButton;
        this.execution = null;
    }

    public void setExecution(OSBBSendMessage osbbSendMessages) {
        execution = new Function<Message, List<OSBBSendMessage>>() {
            @Override
            public List<OSBBSendMessage> apply(Message message) {
                List<OSBBSendMessage> list = new ArrayList<>();
                list.add(osbbSendMessages);
                return list;
            }
        };
    }

    public List<OSBBSendMessage> execute(Message message) {

        if (execution != null) {
            return execution.apply(message);
        }

        if (biExecution != null) {
            return biExecution.apply(sendMessageParams, osbbKeyboardButton);
        }
        return null;
    }

}
