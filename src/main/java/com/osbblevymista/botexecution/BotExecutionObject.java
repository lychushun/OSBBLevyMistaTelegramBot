package com.osbblevymista.botexecution;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.send.SendMessageParams;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor
public class BotExecutionObject {

    private Function<Message, List<PartialBotApiMethod<Message>>> execution;
    private BiFunction<SendMessageParams, OSBBKeyboardButton, List<PartialBotApiMethod<Message>>> biExecution;

    private SendMessageParams sendMessageParams;
    private OSBBKeyboardButton osbbKeyboardButton;

    public BotExecutionObject(PartialBotApiMethod<Message> osbbSendMessages){
        execution = new Function<Message, List<PartialBotApiMethod<Message>>>() {
            @Override
            public List<PartialBotApiMethod<Message>> apply(Message message) {
                List<PartialBotApiMethod<Message>> list = new ArrayList<>();
                list.add(osbbSendMessages);
                return list;
            }
        };
    }

    public BotExecutionObject(Function<Message, PartialBotApiMethod<Message>> ex){
        setExecution(new Function<Message, List<PartialBotApiMethod<Message>>>() {
            @Override
            public List<PartialBotApiMethod<Message>> apply(Message message) {
                List<PartialBotApiMethod<Message>> list = new ArrayList<>();
                list.add(ex.apply(message));
                return list;
            }
        });
    }

    public void setExecution(Function<Message, List<PartialBotApiMethod<Message>>> ex) {
        this.execution = ex;
        this.biExecution = null;
        this.sendMessageParams = null;
        this.osbbKeyboardButton = null;
    }

    public void setExecution(SendMessageParams sendMessageParams, OSBBKeyboardButton osbbKeyboardButton, BiFunction<SendMessageParams, OSBBKeyboardButton, List<PartialBotApiMethod<Message>>> bi) {
        this.biExecution = bi;
        this.sendMessageParams = sendMessageParams;
        this.osbbKeyboardButton = osbbKeyboardButton;
        this.execution = null;
    }

    public void setExecution(PartialBotApiMethod<Message> osbbSendMessages) {
        execution = new Function<Message, List<PartialBotApiMethod<Message>>>() {
            @Override
            public List<PartialBotApiMethod<Message>> apply(Message message) {
                List<PartialBotApiMethod<Message>> list = new ArrayList<>();
                list.add(osbbSendMessages);
                return list;
            }
        };
    }

    public List<PartialBotApiMethod<Message>> execute(Message message) {

        if (execution != null) {
            return execution.apply(message);
        }

        if (biExecution != null) {
            return biExecution.apply(sendMessageParams, osbbKeyboardButton);
        }
        return null;
    }

}
