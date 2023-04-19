package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.telegram.botexecution.BotExecutionObject;
import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public abstract class MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

    @Autowired
    protected SendMessageBuilder sendMessageBuilder;

    protected BotExecutionObject createBotExecutionObject(SendMessageParams sendMessageParams, String text){
        BotExecutionObject botExecutionObject = new BotExecutionObject();
        botExecutionObject.setExecution(sendMessageBuilder.createSimpleMessage(sendMessageParams, text));
        return botExecutionObject;
    }

    protected BotExecutionObject createBotExecutionObject(PartialBotApiMethod<Message> osbbSendMessages){
        BotExecutionObject botExecutionObject = new BotExecutionObject();
        botExecutionObject.setExecution(osbbSendMessages);
        return botExecutionObject;
    }

    protected BotExecutionObject createBotExecutionObjectDelay(SendMessageParams sendMessageParams, String text){
        BotExecutionObject botExecutionObject = new BotExecutionObject();
        botExecutionObject.setExecution(sendMessageBuilder.createMessageDelay(sendMessageParams, text));
        return botExecutionObject;
    }

}
