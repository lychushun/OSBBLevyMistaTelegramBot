package com.osbblevymista;

import com.osbblevymista.telegram.send.OSBBSendMessage;
import org.apache.commons.collections.Closure;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BotExecutionData {

    private final List<Function<Message, List<OSBBSendMessage>>> executionObjects;

    {
        executionObjects = new ArrayList<>();
    }

    public boolean isEmpty(){
        return executionObjects.size() == 0;
    }

    public void addExecutionsForMessage(Function<Message, List<OSBBSendMessage>> executor) {
        executionObjects.add(executor);
    }

    public void addExecutionsForMessage(List<OSBBSendMessage> osbbSendMessages) {
        executionObjects.add(message -> osbbSendMessages);
    }

    public void addExecutionsForMessage(OSBBSendMessage osbbSendMessages) {
        executionObjects.add(message -> {
            List<OSBBSendMessage> list = new ArrayList<>();
            list.add(osbbSendMessages);
            return list;
        });
    }

    public void execute(Message message, Consumer<OSBBSendMessage> closure) {
        executionObjects.forEach(executionObject -> {

            List<OSBBSendMessage> list = executionObject.apply(message);

            list.forEach(it -> {
                if (it.getExecutingDelay() > 0) {
                    Date startDate = new Date();
                    while (startDate.getTime() + it.getExecutingDelay() >= new Date().getTime()) {
                    }
                }
                closure.accept(it);
            });

        });
    }

    public static BotExecutionData empty(){
        return new BotExecutionData();
    }

}
